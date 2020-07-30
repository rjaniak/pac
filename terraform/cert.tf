###
### TLS certificate
###
resource "tls_private_key" "tls_private_key" {
  algorithm = "RSA"
}

resource "tls_self_signed_cert" "tls_cert" {
  key_algorithm   = tls_private_key.tls_private_key.algorithm
  private_key_pem = tls_private_key.tls_private_key.private_key_pem

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

  # TODO: wildcard for backend etc.?
  dns_names = ["frontend.minikube",
               "backend.minikube",
               "keycloak.minikube"]

  # TODO: set common name
  subject {
    common_name  = "frontend.minikube"
    organization = "PRODYNA SE"
  }
}

resource "kubernetes_secret" "tls_secret" {
  metadata {
    name = "tls-secret"
  }
  data = {
    "tls.crt" = tls_self_signed_cert.tls_cert.cert_pem
    "tls.key" = tls_private_key.tls_private_key.private_key_pem
  }
  type = "kubernetes.io/tls"
}