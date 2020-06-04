# Catalog Editor

Quasar/Typescript/Graphql

## Editor 프로젝트 폴더에서 아래 명령어를 실행해서 dependencies 설치
```bash
yarn
```

### 개발 모드 실행 (hot-code reloading, error reporting, etc.)
```bash
quasar dev
```

### Lint 코드 검사 실행
```bash
yarn run lint
```

### 앱 빌
```bash
quasar build
```

### 설정 참고 자료
See [Support TypeScript](https://quasar.dev/quasar-cli/cli-documentation/supporting-ts)
See [Configure VS Code](https://quasar.dev/start/vs-code-configuration)
See [Configuring quasar.conf.js](https://quasar.dev/quasar-cli/quasar-conf-js).

### [vue-i18n-extact](https://pixari.github.io/vue-i18n-extract/how-to-use.html#getting-started)
This library analyses your Vue.js source code looking for any vue-i18n key usage, in order to:
- extract and report all the missing and unused keys in the language files (`yarn i18n`);
- optionnaly write every missing keys into every language files (`yarn i18n:extract`);

### [eslint-plugin-vue-i18n](https://eslint-plugin-vue-i18n.intlify.dev/started.html)
eslint-plugin-vue-i18n is ESLint plugin of Vue I18n. It easily integrates some localization features to your Vue.js Application. (`yarn lint`)
**Note:** The "'vue-i18n/no-raw-text': 'off'," line in .eslintrc.js should be removed in your repo!


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
