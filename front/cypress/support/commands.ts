/// <reference types="cypress" />
export {} // Pour éviter le conflit avec les autres commandes

declare global {
  namespace Cypress {
    interface Chainable<Subject = any> {
      login(): Chainable<void>
    }
  }
}

Cypress.Commands.add('login', () => {
  cy.intercept('POST', '**/api/auth/login', {
    statusCode: 200,
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true,
    },
  }).as('loginRequest')

  cy.visit('/login')
  cy.get('input[formControlName=email]').clear().type('yoga@studio.com')
  cy.get('input[formControlName=password]').clear().type('test!1234')
  cy.get('button[type=submit]').click()

  cy.wait('@loginRequest')
  cy.url().should('include', '/sessions')
})
