resource "kubernetes_namespace" "backend" {
  metadata {
    name = var.backend_namespace
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

resource "kubernetes_config_map" "backend-config-map" {
  metadata {
    name = "conference-backend"
    namespace  = kubernetes_namespace.backend.metadata[0].name
  }

  # TODO: Set data with |-  ?
  data = {
    "application.properties" = <<EOF
server.port=8080
spring.data.neo4j.uri=bolt://neo4j-neo4j
spring.data.neo4j.username=kubernetes_secret.neo4j-access.data.username
spring.data.neo4j.password=kubernetes_secret.neo4j-access.data.password
EOF
  }
}

resource "kubernetes_cluster_role" "role-config-maps" {
  metadata {
    name = "role-config-maps"
  }

  rule {
    api_groups = [""]
    resources  = ["configmaps"]
    verbs      = ["get", "list", "watch"]
  }
}

resource "kubernetes_cluster_role_binding" "role-config-maps-binding" {
  metadata {
    name = "role-config-maps-binding"
  }
  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "ClusterRole"
    name      = "role-config-maps"
  }
  subject {
    kind      = "ServiceAccount"
    name      = "backend"
    namespace = kubernetes_namespace.backend.metadata[0].name
  }
}

resource "helm_release" "backend" {
  chart = "charts/backend"
  name = "backend"
  namespace = kubernetes_namespace.backend.metadata[0].name

  values = [
    file("helm/backend.yaml")
  ]
}
