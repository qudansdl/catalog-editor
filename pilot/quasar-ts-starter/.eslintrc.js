const { resolve } = require('path');
module.exports = {
  root: true,

  env: {
    browser: true,
    es6: true
  },



  extends: [
    // Base ESLint recommended rules
    'eslint:recommended',
    //'plugin:@typescript-eslint/eslint-recommended', // note: it is included in "@vue/typescript[/recommended]"
    //'plugin:@typescript-eslint/recommended', // note: it is included in "@vue/typescript/recommended"

    // Uncomment any of the lines below to choose desired strictness,
    // but leave only one uncommented!
    // See https://eslint.vuejs.org/rules/#available-rules
    'plugin:vue/essential', // Priority A: Essential (Error Prevention)
    // 'plugin:vue/strongly-recommended', // Priority B: Strongly Recommended (Improving Readability)
    // 'plugin:vue/recommended', // Priority C: Recommended (Minimizing Arbitrary Choices and Cognitive Overhead)

    // App internationalization
    // only checks the .vue files and does not handle js/ts modules
    // good for discovering raw text
    // See https://eslint-plugin-vue-i18n.intlify.dev/started.html#installation
    'plugin:vue-i18n/recommended',

    // TypeScript parser options, uncomment one out of two
    // See https://github.com/vuejs/eslint-config-typescript
    // 'eslint:recommended', '@vue/typescript'  // Base configuration for Vue-TypeScript projects
    'airbnb-typescript/base', // parserOptions.project is requered
    '@vue/typescript/recommended' // Opinionated ruleset

    // Additional type checking
    // See https://github.com/typescript-eslint/typescript-eslint/tree/master/packages/eslint-plugin
    //'plugin:@typescript-eslint/recommended-requiring-type-checking'
  ],

  settings: {
    'import/resolver':{
      typescript: {}
    },
    'vue-i18n': {
      localeDir: './src/i18n/*.json'
    }
    //'import/extensions':['.vue']
  },

  parserOptions: {
    // https://github.com/typescript-eslint/typescript-eslint/tree/master/packages/parser#configuration
    // https://github.com/TypeStrong/fork-ts-checker-webpack-plugin#eslint
    // Needed to make the parser take into account 'vue' files
    extraFileExtensions: ['.vue'],
    // parser: '@typescript-eslint/parser', // note: it is included in "@vue/typescript[/recommended]"
    project: resolve(__dirname, './tsconfig.json'),
    tsconfigRootDir: __dirname,
    ecmaVersion: 2018, // Allows for the parsing of modern ECMAScript features
    sourceType: 'module' // Allows for the use of imports
  },

  plugins: [
    // https://eslint.vuejs.org/user-guide/#vhy-doesn-t-it-work-on-vue-file
    // required to lint *.vue files
    'vue',
    // required to apply rules which need type information
    '@typescript-eslint'
  ],

  globals: {
    'ga': true, // Google Analytics
    'cordova': true,
    '__statics': true,
    'process': true,
    'Capacitor': true,
    'chrome': true
  },

  // add your custom rules here
  rules: {
    // allow raw text
    'vue-i18n/no-raw-text': 'off',
    // Some more strict i18n rules
    // 'vue-i18n/no-unused-keys': ['error', {extensions: ['.js','.ts','.vue']}],
    // 'vue-i18n/no-missing-keys': 'error',

    // allow console.log during development only
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    // allow debugger during development only
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off'
  }
}
