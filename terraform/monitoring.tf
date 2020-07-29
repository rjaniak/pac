resource "kubernetes_namespace" "monitoring" {
  metadata {
    name = var.monitoring_namespace
  }
}

resource "random_password" "grafana-password" {
  length = 16
  special = false
}

resource "kubernetes_secret" "grafana-access" {
  metadata {
    name = "grafana-access"
    namespace = kubernetes_namespace.monitoring.metadata[0].name
  }
  data = {
    username = "admin"
    password = random_password.grafana-password.result
  }
}

resource "kubernetes_config_map" "grafana-dashboard" {
  metadata {
    name = "grafana-dashboard"
    namespace = kubernetes_namespace.monitoring.metadata[0].name
    labels = {
      grafana_dashboard = "dashboard"
    }
  }

  data = {
    "k8s-dashboard.json" = file("resources/grafana-dashboard.json")
  }
}

resource "helm_release" "prometheus" {
  chart = "stable/prometheus-operator"
  name = var.prometheus_helm_release
  namespace = kubernetes_namespace.monitoring.metadata[0].name

  values = [
    file("helm/prometheus.yaml")
  ]

  set {
    name = "grafana.adminPassword"
    value = kubernetes_secret.grafana-access.data.password
  }
}