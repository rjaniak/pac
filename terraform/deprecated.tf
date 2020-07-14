###
### Ingress
###
/*resource "kubernetes_ingress" "conference_ingress" {
  metadata {
    name = "nginx"
    namespace = var.namespace
  }
  spec {
    backend {
      service_name = "frontend"
      service_port = 8080
    }
    rule {
      host = "conference-app.com"
      http {
        path {
          backend {
            service_name = "example-service"
            service_port = 8080
          }
          path = "/example"
        }
        path {
          backend {
            service_name = "backend"
            service_port = 8080
          }
          path = "/
          api/"
        }
        path {
          backend {
            service_name = "frontend"
            service_port = 8080
          }
          path = "/"
        }
      }
    }
    tls {
      secret_name = "tls-secret"
      hosts = ["conference-app.com"]
    }
  }
}*/

###
### Prometheus
###
//resource "helm_release" "prometheus" {
//  name       = "prometheus"
//  namespace  = var.namespace
//  chart      = "stable/prometheus"
//  version    = "11.4.0"
//}

###
### Grafana
###
### TODO: Configura Grafana dashboard and export as json
/*resource "random_password" "grafana_password" {
  length = 16
}*/

//resource "grafana_dashboard" "metrics" {
//  config_json = file("resources/grafana-dashboard.json")
//  depends_on = [grafana_data_source.prometheus]
//}

### TODO: Set prometheus url
//resource "grafana_data_source" "prometheus" {
//  type          = "prometheus"
//  name          = "Prometheus"
//  url           = "http://localhost:9090/"
//}
//
//resource "grafana_organization" "org" {
//  name         = "Conference app"
//  admin_user   = "admin"
//  create_users = false
//}


