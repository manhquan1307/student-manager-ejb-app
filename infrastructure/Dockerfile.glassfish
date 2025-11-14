FROM eclipse-temurin:8-jdk

ENV GLASSFISH_VERSION=4.1.2 \
    GLASSFISH_HOME=/opt/glassfish4 \
    PATH=$PATH:/opt/glassfish4/bin

# Install GlassFish
RUN apt-get update && \
    apt-get install -y wget unzip curl ca-certificates && \
    (wget --no-check-certificate -q https://download.oracle.com/glassfish/4.1.2/release/glassfish-4.1.2.zip -O /tmp/glassfish.zip 2>/dev/null || \
     wget --no-check-certificate -q https://github.com/eclipse-ee4j/glassfish/releases/download/4.1.2/glassfish-4.1.2.zip -O /tmp/glassfish.zip 2>/dev/null || \
     curl -L -k -o /tmp/glassfish.zip https://download.oracle.com/glassfish/4.1.2/release/glassfish-4.1.2.zip 2>/dev/null || \
     curl -L -k -o /tmp/glassfish.zip https://github.com/eclipse-ee4j/glassfish/releases/download/4.1.2/glassfish-4.1.2.zip) && \
    unzip -q /tmp/glassfish.zip -d /opt && \
    rm -f /tmp/glassfish.zip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set permissions
RUN chmod -R 755 $GLASSFISH_HOME && \
    chmod -R 777 $GLASSFISH_HOME/glassfish/domains

# Expose ports
EXPOSE 8080 4848 3700

WORKDIR $GLASSFISH_HOME

CMD ["asadmin", "start-domain", "-v"]

