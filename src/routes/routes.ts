/**
 * All the routes in our entire application, in one place for referencing.
 */
export const ROUTES = {
    AUTH: {
        REGISTER: '/auth/register',
        LOG_IN: '/auth/login',
        FORGOT_PASSWORD: '/auth/forgot-password',
        RESET_PASSWORD: '/auth/reset-password',
    },
    HOME: {
        INDEX: '/',
    },
    JOBSEARCH: {

    },
    ANALYSIS: {},
    CRM: {},
    // All our config and settings in one place
    SETTINGS: {
        INDEX: '/user',
        API: '/user/api',
        INVOICE_HISTORY: '/user/invoice-history',
    },

    NOT_FOUND: '*'
};
