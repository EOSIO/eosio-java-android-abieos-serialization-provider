DEST_REPO=$1 && \
echo "publishing to $DEST_REPO on Artifactory" && \
cat gradle.properties && \
./gradlew properties && \
./gradlew clean artifactoryPublish -Partifactory_repo=$DEST_REPO