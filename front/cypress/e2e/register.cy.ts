/// <reference types="cypress" />

describe('Register spec', () => {

  it('Register successfull', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'new@test.com',
        admin: false
      }
    }).as('registerSuccess');

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("new@test.com");
    cy.get('input[formControlName=password]').type("test!1234{enter}");

    cy.wait('@registerSuccess');
    cy.url().should('include', '/login'); // ✅ redirection après succès
  });

  it('Register failed (server error)', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: { message: "Registration failed" }
    }).as('registerFail');

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("invalid@test.com");
    cy.get('input[formControlName=password]').type("test!1234{enter}");

    cy.wait('@registerFail');


    cy.contains(/an error occurred/i).should('exist');
  });

  it('Register validation error (invalid email)', () => {
    cy.visit('/register');

    cy.get('input[formControlName=email]').type("notanemail");


    cy.get('button[type=submit]').should('be.disabled');
  });

});
