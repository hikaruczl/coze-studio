/*
 * Coze Logs Migration - I18n Compat Layer
 * 简化的 I18n.t 替代实现，可替换为你项目的国际化系统（如 react-i18next）。
 */
export const I18n = {
  t(key: string, options?: { defaultValue?: string }) {
    return options?.defaultValue ?? key;
  },
};

