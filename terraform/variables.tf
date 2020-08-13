variable "minikube_ip" {
  type = string
  description = "Minikube ip used for host alias 'keycloak.minikube' in backend pods. This avoids issuer validation error."
}