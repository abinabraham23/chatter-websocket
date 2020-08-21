FROM registry.access.redhat.com/ubi8/ubi:8.1

LABEL maintainer="Abin Abraham"

USER root

# Set JBoss ENVs
ENV JBOSS_HOME=/opt/jboss/jboss-eap-7.2
#	JAVA_HOME=/usr/lib/jvm/java-1.8.0

# Set JBoss ARGs
ARG INSTALL_ARTIFACT=jboss-eap-7.2.0.zip 
ARG APP_ARTIFACT=ROOT.war
	    
# Get utils & Java Open JDK 8
RUN yum install -y unzip \ 
		zip \
		curl \
		java-1.8.0-openjdk \
		git \
		&& rm -rf /tmp/yum*

# Set Timezone
ENV TZ "Europe/Berlin"
RUN echo "Europe/Berlin" | tee /etc/timezone

# Fetch the JBoss installation archive from context directory
# Extract the installation archive to JBOSS_HOME
COPY ${INSTALL_ARTIFACT} /tmp/${INSTALL_ARTIFACT}
RUN unzip /tmp/${INSTALL_ARTIFACT} -d /opt/jboss && \
    rm -rf /tmp/${INSTALL_ARTIFACT}

# Copy the chatter app war file as ROOT.war to server deployment directory
COPY ${APP_ARTIFACT} ${JBOSS_HOME}/standalone/deployments/${APP_ARTIFACT}

# Open Ports
EXPOSE 8080 9990

# CleanUp, changing access rights on $JBOSS_HOME and the startJBossCommand.sh
RUN chmod -R a+rwx ${JBOSS_HOME} && \
    rm -R /tmp/*

CMD ["/opt/jboss/jboss-eap-7.2/bin/standalone.sh", "-c", "standalone-full.xml", "-b", "0.0.0.0", "-Djboss.http.port=8080" ]