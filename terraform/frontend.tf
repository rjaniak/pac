resource "kubernetes_namespace" "frontend" {
  metadata {
    name = var.frontend_namespace
  }
}

resource "helm_release" "frontend" {
  chart = "charts/frontend"
  name  = "frontend"
  namespace = kubernetes_namespace.frontend.metadata[0].name

  values = [
    file("helm/frontend.yaml")
  ]
}
