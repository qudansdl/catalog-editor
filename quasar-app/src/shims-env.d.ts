declare namespace NodeJS {
  interface ProcessEnv {
    // Values supplied by Quasar CLI (https://quasar.dev/quasar-cli/cli-documentation/handling-process-env#Introduction)
    DEV: 'true' | 'false' | undefined; // Code runs in development mode
    PROD: 'true' | 'false' | undefined; // Code runs in production mode
    CLIENT: 'true' | 'false' | undefined; // Code runs on client (not on server)
    SERVER: 'true' | 'false' | undefined; // Code runs on server (not on client)
    MODE: 'pwa' | 'ssr' | 'cordova' | 'capacitor' | 'electron' | undefined; // Quasar CLI mode (spa, pwa, …)
    NODE_ENV: 'production' | 'development' | undefined; // Has two possible values: production or development

    // Vue router (https://router.vuejs.org/guide/essentials/history-mode.html)
    VUE_ROUTER_MODE: 'hash' | 'history' | 'abstract' | undefined;
    VUE_ROUTER_BASE: string | undefined;
  }
}
