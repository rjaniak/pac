resource "kubernetes_namespace" "backend" {
  metadata {
    name = local.namespace-backend
  }
}

/*resource "kubernetes_secret" "dockerhub-pullsecret" {
  metadata {
    name = "dockerhub-pullsecret"
    namespace = kubernetes_namespace.backend.metadata[0].name
  }

  data = {
    ".dockerconfigjson" = file("docker/config.json")
  }

  type = "kubernetes.io/dockerconfigjson"
}*/

data "template_file" "backend-properties" {
  template = file("resources/backend.properties")

  vars = {
    namespace = kubernetes_namespace.persistence.metadata[0].name
    username = kubernetes_secret.neo4j-access.data.username
    password = kubernetes_secret.neo4j-access.data.password
  }
}

resource "kubernetes_config_map" "backend-config-map" {
  metadata {
    name = "conference-backend"
    namespace  = kubernetes_namespace.backend.metadata[0].name
  }

  data = {
    "application.properties" = data.template_file.backend-properties.rendered
  }
}

resource "kubernetes_cluster_role" "backend-role-config-map" {
  metadata {
    name = "backend-role-config-map"
  }

  rule {
    api_groups = [""]
    resources  = ["configmaps"]
    verbs      = ["get", "list", "watch"]
  }
}

resource "kubernetes_cluster_role_binding" "backend-role-binding-config-map" {
  metadata {
    name = "backend-role-binding-config-map"
  }
  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "ClusterRole"
    name      = "backend-role-config-map"
  }
  subject {
    kind      = "ServiceAccount"
    name      = "backend"
    namespace = kubernetes_namespace.backend.metadata[0].name
  }
}

resource "kubernetes_secret" "backend-tls-secret" {
  metadata {
    name = "tls-secret"
    namespace = kubernetes_namespace.backend.metadata[0].name
  }
  data = kubernetes_secret.tls-secret.data
  type = "kubernetes.io/tls"
}

resource "helm_release" "backend" {
  chart = "charts/backend"
  name = "backend"
  namespace = kubernetes_namespace.backend.metadata[0].name

  values = [
    file("helm/backend.yaml")
  ]

  set {
    name = "servicemonitor.metadata.namespace"
    value = kubernetes_namespace.backend.metadata[0].name
  }
  set {
    name = "servicemonitor.metadata.labels.release"
    value = helm_release.prometheus.name
  }
}
