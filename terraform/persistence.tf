resource "kubernetes_namespace" "persistence" {
  metadata {
    name = var.persistence_namespace
  }
}

resource "random_password" "neo4j-password" {
  length = 16
  special = false
}

resource "kubernetes_secret" "neo4j-access" {
  metadata {
    name = "neo4j-access"
    namespace = kubernetes_namespace.persistence.metadata[0].name
  }
  data = {
    username = "neo4j"
    password = random_password.neo4j-password.result
  }
}

resource "helm_release" "neo4j" {
  chart = "https://github.com/neo4j-contrib/neo4j-helm/releases/download/4.1.0-1/neo4j-4.1.0-1.tgz"
  namespace = kubernetes_namespace.persistence.metadata[0].name
  name = "neo4j"

  values = [
    file("helm/neo4j.yaml")
  ]

  set {
    name = "neo4jPassword"
    value = kubernetes_secret.neo4j-access.data.password
  }
}