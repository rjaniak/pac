provider "kubernetes" {
  version = "~> 1.11"
  config_context   = "minikube"
}

provider "random" {
  version = "~> 2.2"
}

provider "tls" {
  version = "~> 2.1"
}

provider "helm" {
  version = "~> 1.2"
  kubernetes {
    config_context = "minikube"
  }
}

// TODO --> besser stable prometheus-operator inkl. grafana
/*provider "grafana" {
  version = "~> 1.5"
  url  = "https://conference-app.com/grafana/"
  auth = random_password.grafana_password.result
}*/
