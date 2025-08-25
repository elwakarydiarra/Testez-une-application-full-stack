/// <reference types="cypress" />

describe('Testing session component as user', () => {
  beforeEach(() => {
    cy.visit('/login')


cy.intercept('GET', '**/api/**', (req) => {

  console.log('API GET =>', req.url)
}).as('anyApi')


cy.intercept('POST', '**/auth/login', {
  statusCode: 200,
  body: { id: 6, username: 'userName', firstName: 'firstName', lastName: 'lastName', admin: false },
}).as('login')


const SESSIONS_LIST = /\/api\/sessions?(\/)?(\?.*)?$/
cy.intercept('GET', SESSIONS_LIST, {
  statusCode: 200,
  body: [
    { id: 1, name: 'Session 1', description: 'Description 1', date: '2024-06-10', teacher_id: 1, users: [1,2] },
    { id: 2, name: 'Session 2', description: 'Description 2', date: '2024-06-10', teacher_id: 2, users: [2,3] },
  ],
}).as('getSessions')

cy.get('input[formControlName=email]').type('test@mail.com')
cy.get('input[formControlName=password]').type('test123')
cy.get('button[type=submit]').click()

cy.wait('@login', { timeout: 10000 })


cy.wait('@getSessions', { timeout: 10000 }).catch(() => {

  cy.wait('@anyApi', { timeout: 2000 })
  .then((interception) => {

    console.log('First API seen by spy:', interception.request.url)

  })
})

cy.url().should('include', '/sessions')


  })
})


describe('Testing session component as admin', () => {
  beforeEach(() => {
    cy.visit('/login')

    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1, username: 'userName', firstName: 'firstName', lastName: 'lastName', admin: true
      },
    }).as('login')

    cy.intercept('GET', '**/api/session*', {
      statusCode: 200,
      body: [
        { id: 1, name: 'Session 1', description: 'Description 1', date: '2024-06-10', teacher_id: 1, users: [1, 2], createdAt: '2024-06-10', updatedAt: '2024-06-10' },
        { id: 2, name: 'Session 2', description: 'Description 2', date: '2024-06-10', teacher_id: 2, users: [2, 3], createdAt: '2024-06-10', updatedAt: '2024-06-10' },
      ],
    }).as('getSessions')

    cy.get('input[formControlName=email]').type('test@mail.com')
    cy.get('input[formControlName=password]').type('test123')
    cy.get('button[type=submit]').click()

    cy.wait('@login', { timeout: 10000 })
    cy.wait('@getSessions', { timeout: 10000 })
  })

  it('See all sessions as admin', () => {
    cy.contains('button', 'Edit').should('exist')
    cy.contains('button', 'Create').should('exist')
  })
})

