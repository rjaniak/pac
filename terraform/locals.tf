locals {
  namespace-keycloak = "keycloak"
  namespace-persistence = "persistence"
  namespace-backend = "backend"
  namespace-frontend = "frontend"
  namespace-monitoring = "monitoring"

  // Prometheus release name is necessary as label for servicemonitors
  helm-release-name-prometheus = "prometheus"

  neo4j-username = "neo4j"
  grafana-username = "admin"
  keycloak-mariadb-username = "keycloak"
  keycloak-mariadb-database = "keycloak"
  keycloak-username = "admin"
}
