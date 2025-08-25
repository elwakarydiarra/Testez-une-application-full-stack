/// <reference types="cypress" />

describe('AuthGuard', () => {
  it('redirige vers /login si non connecté', () => {
    cy.visit('/sessions/detail/1');
    cy.url().should('include', '/login');
  });
});
