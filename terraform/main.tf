terraform {
  required_version = ">= 0.12"
}

resource "kubernetes_namespace" "conference" {
  metadata {
    name = var.conference-namespace
  }
}