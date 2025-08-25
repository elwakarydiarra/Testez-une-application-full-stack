/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable {
    /**
     * Custom command pour se connecter rapidement
     * @example cy.login()
     */
    login(): Chainable;
  }
}
