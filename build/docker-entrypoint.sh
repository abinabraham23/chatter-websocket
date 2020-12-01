#!/bin/bash
set -e

# ***********************************
# Function definitions
# ***********************************

# function applyBaseConfiguration () {
#   	# Apply the base configuration
#   	logMessage "Applying Jboss Base Configuration..."
#   	$JBOSS_HOME/bin/jboss-cli.sh -c --file=${JBOSS_HOME}/bin/base_eap_configuration
# }

function logMessage () {
    echo ""
    printf "$1${NC}"
    echo ""
}

function startJBoss () {
  	logMessage "Starting JBoss EAP..."
  	# Starting JBoss and making it a background process
  	nohup $JBOSS_HOME/bin/standalone.sh -c standalone-full.xml 2>&1 &     
  	logMessage "JBoss EAP 7.2 server started sucessfully."
}


# ************************
# Container start-up steps
# ************************

startJBoss