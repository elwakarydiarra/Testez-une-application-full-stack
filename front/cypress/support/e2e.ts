/// <reference types="cypress" />

if (typeof (window as any).process === 'undefined') {
  (window as any).process = { env: {} }
}

import '@cypress/code-coverage/support'
import './commands'
