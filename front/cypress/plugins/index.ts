/**
 * @type {Cypress.PluginConfig}
 */
const registerCodeCoverageTasks = require('@cypress/code-coverage/task');

export default (on: any, config: any) => {
  return registerCodeCoverageTasks(on, config);
};
