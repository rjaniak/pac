resource "kubernetes_namespace" "frontend" {
  metadata {
    name = local.namespace-frontend
  }
}

resource "kubernetes_secret" "frontend-tls-secret" {
  metadata {
    name = "tls-secret"
    namespace = kubernetes_namespace.frontend.metadata[0].name
  }
  data = kubernetes_secret.tls-secret.data
  type = "kubernetes.io/tls"
}

resource "helm_release" "frontend" {
  chart = "charts/frontend"
  name  = "frontend"
  namespace = kubernetes_namespace.frontend.metadata[0].name

  values = [
    file("helm/frontend.yaml")
  ]
}
