MVN ?= mvn

.PHONY: run build build-java build-frontend build-docker push up down logs

# Buduje wszystko (Java, React, obrazy Dockera) i uruchamia kontenery.
run: build up

# Buduje Javę, Reacta i obrazy Dockera (bez uruchamiania).
build: build-java build-frontend build-docker

build-java:
	$(MVN) -B -f backend/pom.xml -DskipTests package

build-frontend:
	cd frontend && npm ci && npm run build

build-docker:
	docker compose build

# Buduje i wypycha obrazy backendu i frontendu na Docker Hub.
# Wymaga: `docker login` oraz DOCKERHUB_USER w .env. Wersja: make push IMAGE_TAG=1.0.0
push:
	@if docker compose config --images | grep -q '^local/'; then \
		echo "Brak DOCKERHUB_USER w .env - ustaw swój login z Docker Huba"; exit 1; fi
	docker compose build backend frontend
	docker compose push backend frontend

# Uruchamia kontenery w tle.
up:
	docker compose up -d

down:
	docker compose down

logs:
	docker compose logs -f
