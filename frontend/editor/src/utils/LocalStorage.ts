import { LocalStorage } from 'quasar';

// App
const sidebarStatusKey = 'sidebar_status';
export const getSidebarStatus = () => LocalStorage.getItem(sidebarStatusKey)?.toString();
export const setSidebarStatus = (sidebarStatus: string) => LocalStorage.set(sidebarStatusKey, sidebarStatus);

const languageKey = 'language';
export const getLanguage = () => LocalStorage.getItem(languageKey)?.toString();
export const setLanguage = (language: string) => LocalStorage.set(languageKey, language);

const sizeKey = 'size';
export const getSize = () => LocalStorage.getItem(sizeKey)?.toString();
export const setSize = (size: string) => LocalStorage.set(sizeKey, size);

// User
const tokenKey = 'access_token';
export const getToken = () => LocalStorage.getItem(tokenKey)?.toString();
export const setToken = (token: string) => LocalStorage.set(tokenKey, token);
export const removeToken = () => LocalStorage.remove(tokenKey);
