resource "kubernetes_namespace" "persistence" {
  metadata {
    name = local.namespace-persistence
  }
}

resource "random_password" "neo4j-password" {
  length = 16
  special = false
}

resource "kubernetes_secret" "neo4j-access" {
  metadata {
    name = "neo4j-access"
    namespace = kubernetes_namespace.persistence.metadata[0].name
  }
  data = {
    username = local.neo4j-username
    password = random_password.neo4j-password.result
  }
}

resource "kubernetes_config_map" "neo4j-csv-data" {
  metadata {
    name = "neo4j-csv-data"
    namespace = kubernetes_namespace.persistence.metadata[0].name
  }

  data = {
    "events.csv" = file("data/events.csv")
    "locations.csv" = file("data/locations.csv")
    "organizations.csv" = file("data/organizations.csv")
    "persons.csv" = file("data/persons.csv")
    "rooms.csv" = file("data/rooms.csv")
    "timeSlots.csv" = file("data/timeSlots.csv")
    "timeSlotToRoom.csv" = file("data/timeSlotToRoom.csv")
    "topics.csv" = file("data/topics.csv")
    "topicToParentTopic.csv" = file("data/topicToParentTopic.csv")
    "talks.csv" = file("data/talks.csv")
    "talkToEvent.csv" = file("data/talkToEvent.csv")
    "talkToPerson.csv" = file("data/talkToPerson.csv")
    "talkToRoom.csv" = file("data/talkToRoom.csv")
    "talkToTimeSlot.csv" = file("data/talkToTimeSlot.csv")
    "talkToTopic.csv" = file("data/talkToTopic.csv")
  }
}

resource "kubernetes_config_map" "neo4j-cypher-file" {
  metadata {
    name = "neo4j-cypher-file"
    namespace = kubernetes_namespace.persistence.metadata[0].name
  }

  data = {
    "neo4j-import-queries.cypher" = file("resources/neo4j-import-queries.cypher")
  }
}

resource "helm_release" "neo4j" {
  chart = "https://github.com/neo4j-contrib/neo4j-helm/releases/download/4.1.1-2/neo4j-4.1.1-2.tgz"
  name = "neo4j"
  namespace = kubernetes_namespace.persistence.metadata[0].name

  values = [
    file("helm/neo4j.yaml")
  ]

  set {
    name = "neo4jPassword"
    value = kubernetes_secret.neo4j-access.data.password
  }
}

resource "helm_release" "neo4j-metrics-servicemonitor" {
  depends_on = [
    helm_release.prometheus,
    helm_release.neo4j
  ]

  chart = "charts/persistence"
  name = "neo4j-metrics-servicemonitor"
  namespace = kubernetes_namespace.persistence.metadata[0].name

  set {
    name = "metadata.namespace"
    value = kubernetes_namespace.persistence.metadata[0].name
  }
  set {
    name = "metadata.labels.release"
    value = helm_release.prometheus.name
  }
}
