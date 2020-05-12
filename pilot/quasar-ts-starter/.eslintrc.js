const { resolve } = require('path');
module.exports = {
  root: true,

  env: {
    browser: true,
    es6: true
  },



  extends: [
    'eslint:recommended',
    'plugin:vue/essential', // Priority A: Essential (Error Prevention)
    'plugin:vue-i18n/recommended',
    'airbnb-typescript/base', // parserOptions.project is requered
    '@vue/typescript/recommended' // Opinionated ruleset
  ],

  settings: {
    'import/resolver':{
      typescript: {}
    },
    'vue-i18n': {
      localeDir: './src/i18n/*.json'
    }
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
    "max-len": 0,
    // allow raw text
    'vue-i18n/no-raw-text': 'off',

    // allow console.log during development only
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    // allow debugger during development only
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off'
  }
}
