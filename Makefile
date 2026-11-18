MVN ?= mvn

.PHONY: run build build-java build-frontend build-docker up down logs

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

# Uruchamia kontenery w tle.
up:
	docker compose up -d

down:
	docker compose down

logs:
	docker compose logs -f
