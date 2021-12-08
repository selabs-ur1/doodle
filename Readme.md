# Cloud-native applications and microservice architecture

Docker for the back
Create a microservice
Connect the microservice
Docker for the new microservice

## Run the back applicaiton using docker (for windows)

Update the application.yml file :
In order to do api requests from one docker container to another, you need to replace the host of the other containers
from ```localhost``` to ```host.docker.internal```.
Thus, you need to clean your tests file because they will not work anymore because we changed the host.

Then, you can package and build the application using a dockerfile.

In api folder :
```shell script
./mvnw package

docker build -f src/main/docker/Dockerfile.jvm -t quarkus/code-with-quarkus-jvm .
```

Now you can update the docker-compose file to run the application with a single command.
Add a new service to the docker-compose.
```yaml
  doodle_api:
    image: quarkus/code-with-quarkus-jvm
    restart: always
    ports:
      - "8080:8080"
```

We set the restart parameter to always because the application need the others microservices to run in order to compile.
So the doodle_api container may restart a couple of times before running correctly.

Use the following command to run the whole application. The application should be running at localhost:8080.
```shell script
docker-compose up --detach
```

## Publish the image to docker

To publish the image of api_doodle on docker to use it on remote machines, you first need to create a repository on docker hub.
Then you need to build the project with the name of the repository.

```shell script
docker build -f api/src/main/docker/Dockerfile.jvm -t [name_of_the_repository] .
```

Do not forget to update the name of the image in the docker-compose file.

Then you can push your image to your docker hub repository.

```shell script
docker push [name_of_the_repository]
```

And that's it, now you can run your application from other devices using the docker-compose file. You only need to check that the device has the rights to access your repository.

## Create a new microservice and connect it to the former api

We created a new microservice that will give the forecast using another framework called [Moleculer](https://moleculer.services/), but you can use any microservice that you want.
Like the doodle api, we want to use docker to run our application.

```shell script
docker-compose -f api/docker-compose.yaml -f forecastapi/docker-compose.yaml up -d
docker-compose -f api/docker-compose.yaml -f forecastapi/docker-compose.yaml down
```