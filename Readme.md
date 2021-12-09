# Cloud-native applications and microservice architecture

## Run the back applicaiton using docker (for windows)

Update the /api/src/main/resources/application.yml file :
In order to do api requests from one docker container to another, you need to replace the host of the other containers
from ```localhost``` to ```host.docker.internal```.
Thus, you need to clean all your test files in the /api/src/test folder because they will not work anymore because we changed the host.

Then, you can package and build the application using a dockerfile.

Open a terminal and navigate to the api folder, then use the following command lines :
```sh
./mvnw package

docker build -f src/main/docker/Dockerfile.jvm -t quarkus/code-with-quarkus-jvm .
```

Now you can update the docker-compose.yaml file to run the application with a single command.
Add a new service to the docker-compose.
```yaml
  doodle_api:
    image: quarkus/code-with-quarkus-jvm
    restart: always
    ports:
      - "8080:8080"
```

We set the restart parameter to always because the application needs the other microservices to be already running in order to compile.
So the doodle_api container may restart a couple of times before running correctly.

Use the following command to run the whole application. The application should be running at localhost:8080.
```sh
docker-compose up --detach
```
If the command can't run because some ports are already used, please kill those processes.

## Publish the image to docker

If you don't want to publish an image on docker hub, you can skip this part.
To publish the image of api_doodle on docker to use it on remote machines, you first need to create a repository on docker hub.
The name of the repository should be ```[your_username]/[name_of_the_service]```.
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

## Create a new microservice and connect it to the api

Now that we saw how to run the existing app as a cloud-native application, you can create your own microservices to improve the doodle app.
As an example, we created a new microservice that will give the forecast using an external api.
We did this microservice as a simple node.js api but you may use the framework that you want.
Here are some examples on this [page](https://medium.com/microservices-architecture/top-10-microservices-framework-for-2020-eefb5e66d1a2).

### Node.js forecast microservice

First, create a new folder at the root of the project. This is where you can put your microservices.
We used the express framework to create our api and also the request module to make our requests to the external api.

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
    const uri = 'http://api.weatherapi.com/v1/forecast.json?key=1baa4a47b6694dd89d274000212911&q=London&days=1&aqi=no&alerts=no' // We set here the uri of the external api that we call

    const forecast = await request(uri)
    res.json(forecast)
});

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);
```

We also need to add a package.json file to describe and configure our node application.

```json
{
    "name": "forecastapp",
    "version": "1.0.0",
    "description": "Forecast app",
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

A "dependencies" key with all modules installed should have appeared in the package.json file.

Now you can run the microservice using ```npm run start``` and see the result at http://localhost:8081/forecast

### Link the new microservice to the doodle api

We now want to call our forecast microservice from the front-end application through the doodle api. So, we create a new endpoint that will redirect the forecast from the new microservice to the front-end.
Following the former doodle example, we need first to tell to the doodle api which url the new microservice use.
In application.yml file (api\src\main\resources folder) we add the following line under doodle:

```yml
  weatherServiceUrl: "http://host.docker.internal:8081/"
```

Like that, if the url of our forecast microservice change, we will only need to change this line for the all api. 

Now we can create a new endpoint, so that our front-end application can get the forecast through the doodle api.
Like the other endpoints in this application, we create a new file dedicated to the forecast under api\src\main\java\fr\istic\tlc\resources folder, we called it WeatherResourcesEx.java


```java
package fr.istic.tlc.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@RestController
@RequestMapping("/api")
public class WeatherResourceEx {

    @ConfigProperty(name = "doodle.weatherServiceUrl", defaultValue = "http://localhost:8081/") // Here we collect the url of our forecast microservice from the application.yml file
    String weatherServiceUrl = "";

    @GetMapping("/weather") // The endpoint
    public ResponseEntity<String> retrieveWeather() throws InterruptedException, ExecutionException, IOException {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        HttpGet request = new HttpGet(weatherServiceUrl + "forecast");

        Future<HttpResponse> future = client.execute(request, null);
        HttpResponse response = future.get();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.getEntity().writeTo(out);
        String responseString = out.toString();
        out.close();
        client.close();

        System.out.println(responseString);

        return new ResponseEntity<>(responseString, HttpStatus.OK);
    }

}
```
If you built the application earlier, use the following command line :
```
  docker-compose down
```

Build the application (in a terminal, in the /api/ forlder):
```
  ./mvnw package
  docker build -f src/main/docker/Dockerfile.jvm -t quarkus/code-with-quarkus-jvm .
  docker-compose up --detach
```
Wait for the api_doodle_api_1 container to start running properly

If all went well, now, you may access the forecast from http://localhost:8080/api/weather, while running the forecast microservice and the doodle api.

### Run the forecast microservice using Docker

In order to run the whole application as a cloud-native application as we did in the first part, we need to create a docker image for our new forecast microservice.

First, we need a Dockerfile to create our image. So, add a new file in the microservice folder called Dockerfile :

```DockerFile
FROM node:10

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
COPY package*.json ./

RUN npm install

# Bundle app source
COPY . .

EXPOSE 8081
CMD [ "node", "server.js" ]
```

To run the DockerFile and build the image, run ```docker build . -t [name_of_the_service]```
Then you can run the microservice with ```docker run -p 8081:8081 -d [name_of_the_service]```

You should still have access to the forecast from the doodle api http://localhost:8080/api/weather.

### Run the whole application with a single command

Finally, we want to run all of our microservices using Docker and with a single command.

To do this, we first need to create a new docker-compose file to run the forecast microservice.

create a new docker-compose.yaml file

```docker-compose.yaml
version: "3.8"
services:
  forecast:
    image: [name_of_the_service]
    ports:
      - "8081:8081"
``` 

Then, stop and delete the container previously created :
'''
  docker container rm [container_name]
  
'''
container_name is not the name of the service but the name of the container.

Then, return to the root folder. Launch the two docker-compose.yaml file. You can add as much docker-compose files as you want :

```shell script
docker-compose -f api/docker-compose.yaml -f [PATH_TO_THE_FILE]/docker-compose.yaml up -d
``` 

To stop everything, run the following command
```shell script 
  docker-compose -f api/docker-compose.yaml -f forecastapi/docker-compose.yaml down
``` 