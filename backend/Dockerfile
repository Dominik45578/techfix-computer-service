# ---------- Backend: serwlety + SQLite na Tomcat 10.1 ----------

# Etap 1: budowanie pliku WAR Mavenem
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Najpierw sam pom.xml, żeby zależności trafiły do cache warstw Dockera
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src/main ./src/main
RUN mvn -B -q package -Dmaven.test.skip=true

# Etap 2: obraz uruchomieniowy z Tomcatem
FROM tomcat:10.1-jdk17-temurin

# Aplikacja jako ROOT -> serwlety dostępne pod /logowanie, /ksiazki, /wyloguj
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/biblioteka.war /usr/local/tomcat/webapps/ROOT.war

# Database.java zapisuje bazę w ${catalina.base}/data/biblioteka/biblioteka.db
RUN mkdir -p /usr/local/tomcat/data/biblioteka
VOLUME /usr/local/tomcat/data

EXPOSE 8080
CMD ["catalina.sh", "run"]
