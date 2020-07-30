provider "kubernetes" {
  version = "~> 1.11"
  config_context   = "minikube"
}

provider "random" {
  version = "~> 2.2"
}
provider "template" {
  version = "~> 2.1"
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
