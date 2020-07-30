resource "kubernetes_namespace" "persistence" {
  metadata {
    name = local.namespace-persistence
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
    username = local.neo4j-username
    password = random_password.neo4j-password.result
  }
}

resource "helm_release" "neo4j" {
  chart = "https://github.com/neo4j-contrib/neo4j-helm/releases/download/4.1.0-1/neo4j-4.1.0-1.tgz"
  name = "neo4j"
  namespace = kubernetes_namespace.persistence.metadata[0].name

  values = [
    file("helm/neo4j.yaml")
  ]

  set {
    name = "neo4jPassword"
    value = kubernetes_secret.neo4j-access.data.password
  }
}

resource "helm_release" "neo4j-metrics-servicemonitor" {
  depends_on = [
    helm_release.prometheus,
    helm_release.neo4j
  ]

  chart = "charts/persistence"
  name = "neo4j-metrics-servicemonitor"
  namespace = kubernetes_namespace.persistence.metadata[0].name

  set {
    name = "metadata.namespace"
    value = kubernetes_namespace.persistence.metadata[0].name
  }
  set {
    name = "metadata.labels.release"
    value = local.helm-release-name-prometheus
  }
}
