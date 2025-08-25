/// <reference types="cypress" />

const SESSIONS_LIST = /\/api\/sessions?(\/)?(\?.*)?$/i
const USER_DETAIL   = /\/api\/(user|users)\/\d+(\?.*)?$/i


const navigateToMeSpa = () => {
  cy.window().then((win: any) => {
    const root = win.document.querySelector('app-root')
    const ng = win.ng

    if (ng && ng.getInjector && ng.coreTokens && ng.coreTokens.Router && root) {
      const injector = ng.getInjector(root)
      const router = injector.get(ng.coreTokens.Router)
      return router.navigateByUrl('/me')
    }

    // fallback si Angular non dispo (tests “hors” Angular)
    win.history.pushState({}, '', '/me')
    win.dispatchEvent(new win.PopStateEvent('popstate'))
  })

  cy.url({ timeout: 10000 }).should('include', '/me')
}

describe('Testing me component', () => {
  const userPayload = {
    id: 2,
    email: 'test@mail.com',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: false,
    createdAt: '2024-06-10',
    updatedAt: '2024-06-10',
  }

  beforeEach(() => {
    cy.visit('/login')


    cy.intercept('GET', '**/api/**', (req) => {

      console.log('API GET =>', req.url)
    }).as('anyApi')

    cy.intercept('POST', '**/auth/login*', {
      statusCode: 200,
      body: {
        id: userPayload.id,
        username: 'userName',
        firstName: userPayload.firstName,
        lastName: userPayload.lastName,
        admin: userPayload.admin,
      },
    }).as('login')


    cy.intercept('GET', SESSIONS_LIST, { statusCode: 200, body: [] }).as('getSessions')

    cy.get('input[formControlName=email]').type(userPayload.email)
    cy.get('input[formControlName=password]').type('test123')
    cy.get('button[type=submit]').click()

    cy.wait('@login', { timeout: 10000 })


    cy.wait('@getSessions', { timeout: 3000 }).then(
      () => {},
      () => {}
    )
  })

  it('should display the user informations', () => {
    cy.intercept('GET', USER_DETAIL, {
      statusCode: 200,
      body: userPayload,
    }).as('getUser')

    navigateToMeSpa()

    cy.wait('@getUser', { timeout: 10000 }).then(
      () => {},
      () => {}
    )

    // nom/prénom et email
    cy.contains(/firstName\s+LASTNAME/i, { timeout: 10000 }).should('be.visible')
    cy.contains(userPayload.email).should('be.visible')

    // pas de bouton Delete si user.id === 1 (admin)
    cy.get('body').then(($b) => {
      const $byText = Cypress.$('button:contains("Delete"), button:contains("delete")', $b)
      if ($byText.length) return
      const $byIcon = Cypress.$('button mat-icon:contains("delete")', $b).closest('button')
      if ($byIcon.length) return
      throw new Error('Bouton Delete introuvable (texte ou icône)')
    })
  })

  it('should delete the user', () => {
    cy.intercept('GET', USER_DETAIL, { statusCode: 200, body: userPayload }).as('getUser')
    cy.intercept('DELETE', USER_DETAIL, { statusCode: 200, body: userPayload }).as('deleteUser')

    navigateToMeSpa()
    cy.wait('@getUser', { timeout: 10000 }).then(() => {}, () => {})


    cy.get('body').then(($b) => {
      const $byText = Cypress.$('button:contains("Delete"), button:contains("delete")', $b)
      if ($byText.length) {
        cy.wrap($byText.first()).click()
        return
      }
      const $byIcon = Cypress.$('button mat-icon:contains("delete")', $b).closest('button')
      if ($byIcon.length) {
        cy.wrap($byIcon.first()).click()
        return
      }
      throw new Error('Bouton Delete introuvable (texte ou icône)')
    })

    cy.get('.mat-simple-snack-bar-content', { timeout: 10000 }).should('be.visible')
    cy.wait('@deleteUser', { timeout: 10000 })
    cy.url().should('include', '/')
  })

  it('should allow user to go back', () => {
    cy.intercept('GET', USER_DETAIL, { statusCode: 200, body: userPayload }).as('getUser')

    navigateToMeSpa()
    cy.wait('@getUser', { timeout: 10000 }).then(() => {}, () => {})

    cy.get('button mat-icon')
      .contains(/arrow_back|arrow_back_ios|chevron_left/i)
      .closest('button')
      .click()

    cy.url().should('include', '/sessions')
    cy.url().should('not.include', '/me')
  })
})
