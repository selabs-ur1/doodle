# Cloud-native applications and microservice architecture

## Run the back applicaiton using docker (for windows)

Update the application.yml file :
In order to do api requests from one docker container to another, you need to replace the host of the other containers
from ```localhost``` to ```host.docker.internal```.
Thus, you need to clean your tests file because they will not work anymore because we changed the host.

Then, you can package and build the application using a dockerfile.

In api folder :
```sh
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
```sh
docker-compose up --detach
```

## Publish the image to docker

To publish the image of api_doodle on docker to use it on remote machines, you first need to create a repository on docker hub.
Then you need to build the project with the name of the repository.

```sh
docker build -f api/src/main/docker/Dockerfile.jvm -t [name_of_the_repository] .
```

Do not forget to update the name of the image in the docker-compose file.

Then you can push your image to your docker hub repository.

```sh
docker push [name_of_the_repository]
```

And that's it, now you can run your application from other devices using the docker-compose file. You only need to check that the device has the rights to access your repository.

## Create a new microservice and connect it to the former api

Now that we saw how to run the existing app as a cloud-native application, you can create your own microservices to improve the doodle app.
As an example, we created a new microservice that will give the forecast using an external api.
We did this microservice as a simple node.js api but you may use any framework that you want.
Here are some examples on this [page](https://medium.com/microservices-architecture/top-10-microservices-framework-for-2020-eefb5e66d1a2).

### Node.js forecast microservice

Firstly, create a new folder at the root of the project where you will put your microservice.
We use the express framework to create our api and also request module to make request to the external api.

Our microservice is just a single JavaScript file called server.js with a unique endpoint called forecast.

```js
// Dependencies
const express = require('express');
const request = require('request-promise-native')

// Constants
const PORT = 8081; // You may use another port
const HOST = '0.0.0.0'; // localhost

// App
const app = express();
app.get('/forecast', async (req, res) => {
    const uri = '' // We set here the uri of the external api that we call

    const forecast = await request(uri)
    res.json(forecast)
});

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);
```

We also need to add a package.json file to describe and configure our node application.

```json
{
    "name": "nodeapp",
    "version": "1.0.0",
    "description": "Node.js on Docker",
    "main": "server.js",
    "scripts": {
        "start": "node server.js"
    }
}
```

Then you need to install all modules needed.

```sh
npm install express
npm install request
npm install request-promise-native
```

A dependencies key with all modules installed should have appeared in the package.json file.










link to doodle
run with docker
run all microservices with a unique command


```shell script
docker-compose -f api/docker-compose.yaml -f forecastapi/docker-compose.yaml up -d
docker-compose -f api/docker-compose.yaml -f forecastapi/docker-compose.yaml down
```