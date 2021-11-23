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
npx ng build

set +e
set +x

