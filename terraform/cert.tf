###
### TLS certificate
###
resource "tls_private_key" "tls-private-key" {
  algorithm = "RSA"
}

resource "tls_self_signed_cert" "tls-cert" {
  key_algorithm   = tls_private_key.tls-private-key.algorithm
  private_key_pem = tls_private_key.tls-private-key.private_key_pem

  # Certificate expires after 12 hours.
  validity_period_hours = 12

  # Generate a new certificate if Terraform is run within three
  # hours of the certificate's expiration time.
  early_renewal_hours = 3

  # Reasonable set of uses for a server SSL certificate.
  allowed_uses = [
    "key_encipherment",
    "digital_signature",
    "server_auth",
  ]

  dns_names = ["frontend.minikube",
               "backend.minikube",
               "keycloak.minikube"]

  subject {
    common_name  = "frontend.minikube"
    organization = "PRODYNA SE"
  }
}

### TLS Secret in default namespace
resource "kubernetes_secret" "tls-secret" {
  metadata {
    name = "tls-secret"
  }
  data = {
    "tls.crt" = tls_self_signed_cert.tls-cert.cert_pem
    "tls.key" = tls_private_key.tls-private-key.private_key_pem
  }
  type = "kubernetes.io/tls"
}
