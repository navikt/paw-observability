# paw-observability
Komponenter som brukes i forbindelse med tracing og metrics.

# Viktig!
Avhengig av loggnivå kan Opentelemetry logge data slik den ser ut *før* maskeringssteget. Det er derfor viktig å bruke en løsning for maskering av loggene også!

## OpenTelemetry Anonymisering
Denne komponenten fungerer som en wrapper til 'OtlpSpanExporterProvider' og kan brukes sammen med openTelemetry-java-agent for å anonymisere data som samles inn før den sendes videre. Nåværende versjon er relativ enkel og maskerer all tall på 11 siffer i "attributes" listen som eksporteres.
Komponenten kan brukes sammen med opentelemetry-javaagent. Den må da lastes sammen med agenten under oppstart, se eksempel under:
```
##Eksempel på Dockerfile med OpenTelemetry agent og annonymisering
FROM ghcr.io/navikt/baseimages/temurin:17

#service navn, bruk samme som 'metadata.name' (nais.yaml)
ENV SERVICE_NAVN=service-navn-brukt-av-opentelemetry
ENV AGENT=./opentelemetry-javaagent.jar
ENV ANONYMISERING=./opentelemetry-anonymisering.jar
ENV APPLIKASJON_JAR=App.jar

COPY app/build/libs/fat.jar app.jar
COPY $AGENT opentelemetry-javaagent.jar
COPY $ANONYMISERING opentelemetry-anonymisering.jar
ENV JAVA_OPTS -javaagent:opentelemetry-javaagent.jar -Dotel.javaagent.extensions=opentelemetry-anonymisering.jar -Dotel.resource.attributes=service.name=$SERVICE_NAVN
CMD ["java", "$JAVA_OPTS", "-jar", "$APPLIKASJON_JAR"]
```

I tillegg til å laste komponenten må miljøvariabel 'OTEL_TRACES_EXPORTER' settes til 'maskert_oltp'.
