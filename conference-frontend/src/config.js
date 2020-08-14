module.exports = {
    BACKEND_BASE_URL: 'https://backend.minikube/api',
    KEYCLOAK_INIT_OPTIONS: {
        url: 'https://keycloak.minikube/auth',
        realm: 'pac',
        clientId: 'conference-frontend',
        onLoad: 'check-sso'
    }
}