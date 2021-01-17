#! /bin/bash
set -e

function logMessage ()
{
    echo ""
    printf "$1${NC}"
    echo ""
}

function applyPatch ()
{
    logMessage "Applying patch";
    $JBOSS_HOME/bin/jboss-cli.sh "patch apply /tmp/${PATCH_ARTIFACT}";
    logMessage "Patch applied successfully";
}

function cleanup ()
{
    rm -rf /tmp/patch.sh /tmp/${PATCH_ARTIFACT}
    logMessage "Temp files deleted..";   
    rm -rf $JBOSS_HOME/.installation
    logMessage "temporary installation directory deleted..";   
}

# Steps

applyPatch
cleanup