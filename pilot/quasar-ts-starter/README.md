# Quasar Framework starter with TypeScript

A Quasar Framework using TypeScript.

## Install the dependencies
```bash
yarn
```

### Start the app in development mode (hot-code reloading, error reporting, etc.)
```bash
quasar dev
```

### Lint the files
```bash
yarn run lint
```

### Build the app for production
```bash
quasar build
```

### Customize the configuration
See [Support TypeScript](https://quasar.dev/quasar-cli/cli-documentation/supporting-ts)
See [Configure VS Code](https://quasar.dev/start/vs-code-configuration)
See [Configuring quasar.conf.js](https://quasar.dev/quasar-cli/quasar-conf-js).

### Used generation params:
? Project name (internal usage for dev) quasar-ts-starter
? Project product name (must start with letter if building mobile apps) Quasar App
? Project description A Quasar Framework app
? Author Egyel
? Pick your favorite CSS preprocessor: (can be changed later) Stylus
? Pick a Quasar components & directives import strategy: (can be changed later) Auto import
? Check the features needed for your project: ESLint, Vuex, Axios, Vue-i18n, IE11 support
? Pick an ESLint preset: Airbnb
? Cordova/Capacitor id (disregard if not building mobile apps) org.cordova.quasar.app
? Should we run `npm install` for you after the project has been created? (recommended) yarn


## App internationalization (i18n)
**Note:** The used tools below are required/prefer to use json files.

### [vue-i18n-extact](https://pixari.github.io/vue-i18n-extract/how-to-use.html#getting-started)
This library analyses your Vue.js source code looking for any vue-i18n key usage, in order to:
- extract and report all the missing and unused keys in the language files (`yarn i18n`);
- optionnaly write every missing keys into every language files (`yarn i18n:extract`);

### [eslint-plugin-vue-i18n](https://eslint-plugin-vue-i18n.intlify.dev/started.html)
eslint-plugin-vue-i18n is ESLint plugin of Vue I18n. It easily integrates some localization features to your Vue.js Application. (`yarn lint`)
**Note:** The "'vue-i18n/no-raw-text': 'off'," line in .eslintrc.js should be removed in your repo!


## TODO
- preventing ESLint and formatting errors from being committed: [lint-staged](https://github.com/okonet/lint-staged) + [husky](https://github.com/typicode/husky)


## Misc
- Quasar docs: [Support TypeScript](https://quasar.dev/quasar-cli/cli-documentation/supporting-ts#Installation-of-TypeScript-Support), [Configure VS Code](https://quasar.dev/start/vs-code-configuration#Recommended-additional-VS-Code-extensions-and-settings-updates)
- decorators:
  - [component](https://class-component.vuejs.org/)
  - [property](https://github.com/kaorun343/vue-property-decorator)
  - [summery doc](https://blog.logrocket.com/how-to-write-a-vue-js-app-completely-in-typescript/)
- TypeScript [linting](https://github.com/typescript-eslint/typescript-eslint/blob/master/docs/getting-started/linting/README.md)
- [Using ESLint and Prettier in a TypeScript Project](https://www.robertcooper.me/using-eslint-and-prettier-in-a-typescript-project)
- useing Prettier:
  - [vuejs](https://github.com/vuejs/eslint-config-typescript), [repo](https://github.com/vuejs/eslint-config-prettier)
  - [config](https://prettier.io/docs/en/configuration.html)
- similar projecs:
  - [article](https://dev.to/xkonti/creating-quasar-framework-project-with-typescript-support-51ge) + [github](https://github.com/Xkonti/quasar-clean-typescript)
  - [quasar extention](https://github.com/quasarframework/app-extension-typescript)
