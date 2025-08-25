/// <reference types="cypress" />

const LOGIN_URL         = '**/auth/login';
const SESSIONS_LIST     = '**/api/session*';
const SESSION_DETAIL_1  = '**/api/session/1*';
const TEACHERS_LIST     = '**/api/teacher*';
const TEACHER_1         = '**/api/teacher/1*';
const CREATE_SESSION    = '**/api/session';
const UPDATE_SESSION_1  = '**/api/session/1';

function interceptListWithOne() {
  cy.intercept('GET', SESSIONS_LIST, {
    statusCode: 200,
    body: [
      { id: 1, name: 'Session 1', description: 'Description 1', date: '2024-06-10', teacher_id: 1, users: [1, 2] },
    ],
  }).as('list');
}

function interceptTeachersList() {
  cy.intercept('GET', TEACHERS_LIST, {
    statusCode: 200,
    body: [
      { id: 1, lastName: 'teacherLastName', firstName: 'teacherFirstName', createdAt: '2024-06-10', updatedAt: '2024-06-10' },
      { id: 2, lastName: 'Doe', firstName: 'John', createdAt: '2024-06-10', updatedAt: '2024-06-10' },
    ],
  }).as('teachers');
}

function interceptDetailOkForForm() {
  cy.intercept('GET', SESSION_DETAIL_1, {
    statusCode: 200,
    body: {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: '2024-06-12',
      teacher_id: 1,
      users: [1, 2, 3],
      createdAt: '2024-06-11',
      updatedAt: '2024-06-10',
    },
  }).as('detail');
  cy.intercept('GET', TEACHER_1, {
    statusCode: 200,
    body: { id: 1, lastName: 'teacherLastName', firstName: 'teacherFirstName', createdAt: '2024-06-10', updatedAt: '2024-06-10' },
  }).as('teacher1');
}

function loginAsAdmin() {
  cy.visit('/login');

  cy.intercept('POST', LOGIN_URL, {
    statusCode: 200,
    body: { id: 1, username: 'userName', firstName: 'firstName', lastName: 'lastName', admin: true },
  }).as('login');

  cy.get('input[formControlName=email]').type('test@mail.com');
  cy.get('input[formControlName=password]').type('test123');
  cy.get('button[type=submit]').click();

  cy.wait('@login', { timeout: 10000 });
  cy.url().should('include', '/sessions');
}

function goToCreate() {
  cy.url().should('include', '/sessions');
  cy.get('body', { timeout: 4000 }).then(($b) => {
    const selectors = [
      '[routerLink*="create"]',
      '[routerLink*="new"]',
      'a[href*="create"]',
      'a[href*="new"]',
      'button:contains("Create")',
      'button:contains("Créer")',
    ];
    for (const s of selectors) {
      const found = $b.find(s);
      if (found.length) {
        cy.wrap(found.first()).click();
        return;
      }
    }

    cy.visit('/sessions/create');
  });
}

function goToEdit() {
  cy.url().should('include', '/sessions');
  cy.contains(/Session 1/i, { timeout: 4000 }).should('exist');

  cy.get('body', { timeout: 4000 }).then(($b) => {
    const selectors = [
      '[routerLink*="edit"]',
      '[routerLink*="update"]',
      'a[href*="edit"]',
      'a[href*="update"]',
      'button:contains("Edit")',
      'button:contains("Modifier")',
    ];
    for (const s of selectors) {
      const found = $b.find(s);
      if (found.length) {
        cy.wrap(found.first()).click();
        return;
      }
    }
    cy.visit('/sessions/update/1');
  });
}

function fillForm({ name, date, teacherFullName, description }: { name: string; date: string; teacherFullName: string; description: string }) {
  cy.get('input[formControlName="name"]').clear().type(name);
  cy.get('input[formControlName="date"]').clear().type(date);
  cy.get('textarea[formControlName="description"]').clear().type(description);


  cy.get('mat-select[formControlName="teacher_id"]').click();
  cy.contains('mat-option', teacherFullName, { matchCase: false }).click();
}


describe('Sessions form — CREATE (ADMIN)', () => {
  beforeEach(() => {
    interceptListWithOne();
    interceptTeachersList();
    loginAsAdmin();
    cy.wait('@list', { timeout: 10000 });
  });

  it('création réussie', () => {
    cy.intercept('POST', CREATE_SESSION, {
      statusCode: 201,
      body: { id: 3 },
    }).as('create');

    goToCreate();

    cy.wait('@teachers', { timeout: 10000 });

    fillForm({
      name: 'New Session',
      date: '2024-06-20',
      teacherFullName: 'teacherFirstName teacherLastName',
      description: 'New description',
    });

    cy.contains('button', 'Save').click();

    cy.wait('@create', { timeout: 10000 });
    cy.url().should('include', '/sessions');
  });

  it('création en erreur 500 (on reste sur la page)', () => {
    cy.intercept('POST', CREATE_SESSION, {
      statusCode: 500,
      body: { message: 'boom' },
    }).as('createFail');

    goToCreate();

    cy.wait('@teachers', { timeout: 10000 });

    fillForm({
      name: 'New Session',
      date: '2024-06-20',
      teacherFullName: 'teacherFirstName teacherLastName',
      description: 'New description',
    });

    cy.contains('button', 'Save').click();

    cy.wait('@createFail', { timeout: 10000 });
    // rester sur la page de création (create OU new)
    cy.url().should('match', /\/sessions\/(create|new)$/);
  });
});

describe('Sessions form — EDIT (ADMIN)', () => {
  beforeEach(() => {
    interceptListWithOne();
    interceptTeachersList();
    interceptDetailOkForForm();
    loginAsAdmin();
    cy.wait('@list', { timeout: 10000 });
  });

  it('édition réussie', () => {
    cy.intercept('PUT', UPDATE_SESSION_1, {
      statusCode: 200,
      body: { id: 1 },
    }).as('update');

    goToEdit();


    cy.wait('@detail', { timeout: 10000 });

    fillForm({
      name: 'Session 1 updated',
      date: '2024-06-22',
      teacherFullName: 'teacherFirstName teacherLastName',
      description: 'Description 1 updated',
    });

    cy.contains('button', 'Save').click();

    cy.wait('@update', { timeout: 10000 });
    cy.url().should('include', '/sessions');
  });

  it('édition en erreur 500 (on reste sur la page)', () => {
    cy.intercept('PUT', UPDATE_SESSION_1, {
      statusCode: 500,
      body: { message: 'boom' },
    }).as('updateFail');

    goToEdit();

    cy.wait('@detail', { timeout: 10000 });

    fillForm({
      name: 'Session 1 updated',
      date: '2024-06-22',
      teacherFullName: 'teacherFirstName teacherLastName',
      description: 'Description 1 updated',
    });

    cy.contains('button', 'Save').click();

    cy.wait('@updateFail', { timeout: 10000 });
    // rester sur la page d'édition (edit OU update)
    cy.url().should('match', /\/sessions\/(edit|update)\/1$/);
  });
});
