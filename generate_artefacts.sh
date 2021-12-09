#!/bin/bash

set -x
set -e

#Generate artefacts for quarkus
cd api
./mvnw package -DskipTests

cd ..

# Generate angular artefacts
cd front
npm install
npm run build

set +e
set +x

