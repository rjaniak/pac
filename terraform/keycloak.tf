resource "kubernetes_namespace" "keycloak" {
  metadata {
    name = local.namespace-keycloak
  }
}

resource "random_password" "keycloak-mariadb-password" {
  length = 16
  special = false
}

resource "kubernetes_secret" "keycloak-mariadb-access" {
  metadata {
    name = "keycloak-mariadb-access"
    namespace = kubernetes_namespace.keycloak.metadata[0].name
  }

  data = {
    database = local.keycloak-mariadb-database
    username = local.keycloak-mariadb-username
    password = random_password.keycloak-mariadb-password.result
  }
}

resource "helm_release" "mariadb" {
  chart = "mariadb"
  name = "keycloak-mariadb"
  namespace = kubernetes_namespace.keycloak.metadata[0].name
  repository = local.helm_repository_bitnami

  values = [
    file("helm/mariadb.yaml")
  ]

  set {
    name = "db.name"
    value = kubernetes_secret.keycloak-mariadb-access.data.database
  }
  set {
    name = "db.user"
    value = kubernetes_secret.keycloak-mariadb-access.data.username
  }
  set {
    name = "db.password"
    value = kubernetes_secret.keycloak-mariadb-access.data.password
  }
}

resource "random_password" "keycloak-password" {
  length = 16
  special = false
}

resource "kubernetes_secret" "keycloak-access" {
  metadata {
    name = "keycloak-access"
    namespace = kubernetes_namespace.keycloak.metadata[0].name
  }

  data = {
    username = local.keycloak-username
    password = random_password.keycloak-password.result
  }
}

resource "kubernetes_secret" "keycloak-realm-pac" {
  metadata {
    name = "keycloak-realm-pac"
    namespace = kubernetes_namespace.keycloak.metadata[0].name
  }

  data = {
    "realm-pac.json" = file("realm/realm-pac.json")
  }
}

resource "kubernetes_secret" "keycloak-tls-secret" {
  metadata {
    name = "tls-secret"
    namespace = kubernetes_namespace.keycloak.metadata[0].name
  }
  data = kubernetes_secret.tls-secret.data
  type = "kubernetes.io/tls"
}

resource "helm_release" "keycloak" {
  depends_on = [
    helm_release.mariadb
  ]

  chart = "keycloak"
  name = "keycloak"
  version = "8.2.2"
  namespace = kubernetes_namespace.keycloak.metadata[0].name
  repository = local.helm_repository_codecentric

  values = [
    file("helm/keycloak.yaml")
  ]

  set {
    name = "keycloak.username"
    value = local.keycloak-username
  }

  set {
    name = "keycloak.persistence.dbName"
    value = local.keycloak-mariadb-database
  }
}
