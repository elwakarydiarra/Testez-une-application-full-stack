// cypress.config.ts
import { defineConfig } from 'cypress'

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',

    setupNodeEvents: async (on, config) => {

      const mod = await import('@cypress/code-coverage/task')
      const codeCoverageTask = (mod as any).default ?? (mod as any)
      codeCoverageTask(on, config)

      return config
    },
  },
})
