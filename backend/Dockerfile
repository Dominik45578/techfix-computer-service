FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src/main ./src/main
RUN mvn -B -q package -Dmaven.test.skip=true

FROM tomcat:10.1-jre17-temurin

ARG APP_UID=10001
ARG APP_GID=10001

RUN groupadd --system --gid ${APP_GID} tomcat \
 && useradd --system --uid ${APP_UID} --gid tomcat --home-dir /usr/local/tomcat \
        --no-create-home --shell /usr/sbin/nologin tomcat \
 && rm -rf /usr/local/tomcat/webapps/* \
 && mkdir -p /usr/local/tomcat/data/biblioteka /usr/local/tomcat/logs \
        /usr/local/tomcat/temp /usr/local/tomcat/work \
 && chown -R tomcat:tomcat /usr/local/tomcat/data /usr/local/tomcat/logs \
        /usr/local/tomcat/temp /usr/local/tomcat/work /usr/local/tomcat/webapps

COPY --from=build --chown=tomcat:tomcat /app/target/biblioteka.war /usr/local/tomcat/webapps/ROOT.war

USER tomcat
VOLUME /usr/local/tomcat/data
EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=3s --start-period=40s --retries=3 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8080 && printf "GET /logowanie HTTP/1.0\r\nHost: localhost\r\n\r\n" >&3 && head -n 1 <&3 | grep -q " 200 "'

CMD ["catalina.sh", "run"]
