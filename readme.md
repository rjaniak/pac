# Conference App

## General

- Versions used

	- Java: 11.0.5  
	- Minikube: v1.10.1  
	- Kubernetes Client Version: v1.15.3  
	- Kubernetes Server Version: v1.15.12  
	- Helm: v3.2.1  
	- Terraform: v0.12.26  

- While testing, please use Chrome with "ignore-certificate-errors" coniguration, because the self-signed TLS certificate isn't trusted by a CA:  

    open -g -a Google\ Chrome --args --ignore-certificate-errors --ignore-urlfetcher-cert-requests

- Github repository: https://github.com/rjaniak/pac

- Dockerhub repository: https://hub.docker.com/repository/docker/rjaniak/pac2020

- Postman collection: https://www.getpostman.com/collections/e5e5971b7a3eab60801f

## Credentials

1. Keycloak user for frontend login  

	pac + pac

2. Keycloak admin  

	kubectl -n keycloak get secrets keycloak-access -o yaml

3. Neo4j  

	kubectl -n persistence get secrets neo4j-access -o yaml

4. Grafana  

	kubectl -n monitoring get secrets grafana-access -o yaml

## Setup

1. Configure minikube  

    minikube config set driver hyperkit  
    minikube config set kubernetes-version v1.15.12  
    minikube config set memory 8000  
    minikube config set cpus 4  
    minikube config set disk-size 30g

2. Start minikube  

	minikube start

3. Enable minikube addons  

	minikube addons enable ingress

4. Get minikube ip  

	minikube ip

5. Update /etc/hosts and add these lines with minikube ip  

	192.168.64.7 keycloak.minikube  
	192.168.64.7 backend.minikube  
	192.168.64.7 frontend.minikube  
	192.168.64.7 grafana.minikube

6. Checkout repository  

	git clone https://github.com/rjaniak/pac.git

7. Install conference app in minikube cluster (if there is an error "timed out waiting for the condition", just repeat ./install)  

	cd pac/terraform  
	./install

8. After Neo4j is ready, import demo data  

	./import-demo-data.sh

9. Access app  

	Frontend: https://frontend.minikube  
	Backend Swagger: https://backend.minikube/swagger-ui.html  
	Backend API: https://backend.minikube/api/events  
	Keycloak: https://keycloak.minikube/auth/admin  
	Grafana Dashboard: https://grafana.minikube/d/HIfXF0HGz/conference-app?orgId=1  

