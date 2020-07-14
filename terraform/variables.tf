variable "conference-namespace" {
  type = string
  default = "conference"
  description = "Namespace for common resources of conference app."
}

variable "keycloak_namespace" {
  type = string
  default = "keycloak"
}

variable "persistence_namespace" {
  type = string
  default = "persistence"
}

variable "backend_namespace" {
  type = string
  default = "backend"
}

variable "frontend_namespace" {
  type = string
  default = "frontend"
}

variable "prometheus_namespace" {
  type = string
  default = "prometheus"
}

variable "grafana_namespace" {
  type = string
  default = "grafana"
}