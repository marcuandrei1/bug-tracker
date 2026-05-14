import { defineConfig } from "cypress";

export default defineConfig({
  projectId: '6mrbbi',
  allowCypressEnv: false,

  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
