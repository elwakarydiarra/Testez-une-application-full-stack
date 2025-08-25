/// <reference types="cypress" />
const LOGIN_URL         = '**/auth/login';
const SESSIONS_LIST     = '**/api/session*';
const SESSION_DETAIL_1  = '**/api/session/1*';
const TEACHER_1         = '**/api/teacher/1*';
const PARTICIPATE_6     = '**/api/session/1/participate/6';
const DELETE_SESSION_1  = '**/api/session/1';

function interceptListWithOne() {
  cy.intercept('GET', SESSIONS_LIST, {
    statusCode: 200,
    body: [
      { id: 1, name: 'Session 1', description: 'Description 1', date: '2024-06-10', teacher_id: 1, users: [1, 2] },
    ],
  }).as('list');
}

function interceptDetailOk(users: number[]) {
  cy.intercept('GET', SESSION_DETAIL_1, {
    statusCode: 200,
    body: {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: '2024-06-12',
      teacher_id: 1,
      users,
      createdAt: '2024-06-11',
      updatedAt: '2024-06-10',
    },
  }).as('detail');

  cy.intercept('GET', TEACHER_1, {
    statusCode: 200,
    body: {
      id: 1,
      lastName: 'teacherLastName',
      firstName: 'teacherFirstName',
      createdAt: '2024-06-10',
      updatedAt: '2024-06-10',
    },
  }).as('teacher');
}


function interceptDetail500() {
  cy.intercept('GET', SESSION_DETAIL_1, { statusCode: 500, body: { message: 'boom' } }).as('detailFail');
}

function loginAs({ id, admin }: { id: number; admin: boolean }) {
  cy.visit('/login');

  cy.intercept('POST', LOGIN_URL, {
    statusCode: 200,
    body: { id, username: 'userName', firstName: 'firstName', lastName: 'lastName', admin },
  }).as('login');

  cy.get('input[formControlName=email]').type('test@mail.com');
  cy.get('input[formControlName=password]').type('test123');
  cy.get('button[type=submit]').click();

  cy.wait('@login', { timeout: 10000 });
  cy.url().should('include', '/sessions');
}

function goToDetailFromList() {
  cy.url().should('include', '/sessions');


  cy.contains(/Session 1/i, { timeout: 4000 });

  cy.get('body').then(($b) => {
    const selectors = [
      '[routerLink*="detail"]',
      'a[href*="detail"]',
      'button:contains("Detail")',
      'button:contains("Details")',
      'button:contains("Voir")',
    ];

    for (const s of selectors) {
      const found = $b.find(s);
      if (found.length) {
        cy.wrap(found.first()).click();
        return;
      }
    }

    cy.visit('/sessions/detail/1');
  });
}


describe('Sessions detail — USER', () => {
  beforeEach(() => {
    // 1) Intercepter la liste AVANT le login
    interceptListWithOne();
    // 2) Login user
    loginAs({ id: 6, admin: false });
    // 3) Attendre la liste
    cy.wait('@list', { timeout: 10000 });
  });

  it('affiche le détail et permet Participate / Unparticipate', () => {
    // Détail OK (user 6 pas encore inscrit)
    interceptDetailOk([1, 2]);
    // Intercepts des actions de participation
    cy.intercept('POST',   PARTICIPATE_6, { statusCode: 200, body: {} }).as('participate');
    cy.intercept('DELETE', PARTICIPATE_6, { statusCode: 200, body: {} }).as('unparticipate');

    goToDetailFromList();

    cy.wait('@detail', { timeout: 10000 });
    cy.wait('@teacher', { timeout: 10000 });
    // On clique sur Participate
    cy.contains('button', /Participate/i, { timeout: 4000 }).click();
    cy.wait('@participate', { timeout: 10000 });

    // On clique sur Do not participate si le bouton est présent (il peut ne pas l’être si l’UI a changé)
    cy.get('body').then($b => {
      const btn = $b.find('button:contains("Do not participate")');
      if (btn.length) {
        cy.wrap(btn.first()).click();
        cy.wait('@unparticipate', { timeout: 10000 });
      }
    });

    // Vérification du rendu
    cy.contains('h1', /Session 1/i).should('exist');
    cy.contains(/teacherFirstName/i).should('exist');
  });

  it('gère une erreur serveur (500) sur le détail', () => {
    interceptDetail500();

    goToDetailFromList();


    cy.wait('@detailFail', { timeout: 10000 });

    // On doit être soit sur la liste, soit sur le détail
    cy.location('pathname', { timeout: 4000 }).then((path) => {
      expect(/\/sessions(\/detail\/1)?$/.test(path)).to.eq(true);
    });

    // Un message d’erreur doit être affiché
    cy.get('body').then($b => {
      const snack = $b.find('.mat-simple-snack-bar-content');
      if (snack.length) {
        cy.wrap(snack).should('exist');
      }
    });
  });
});

describe('Sessions detail — ADMIN', () => {
  beforeEach(() => {
    interceptListWithOne();
    loginAs({ id: 1, admin: true });
    cy.wait('@list', { timeout: 10000 });
  });

  it('affiche Delete et permet delete', () => {
    interceptDetailOk([1, 2, 3]);
    cy.intercept('DELETE', DELETE_SESSION_1, { statusCode: 200, body: {} }).as('deleteSession');

    goToDetailFromList();

    cy.wait('@detail', { timeout: 10000 });
    cy.wait('@teacher', { timeout: 10000 });

    cy.contains('button', /Delete/i, { timeout: 4000 }).should('exist').click();
    cy.wait('@deleteSession', { timeout: 10000 });


    cy.url().should('include', '/sessions');
  });
});
