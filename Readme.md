# Monitoring Tutorial of doodle application with Jaeger & OpenTracing
 
## Diagram of how Jaeger works

![image](https://user-images.githubusercontent.com/57901216/143829912-ed348025-33a3-4936-9dd6-44eb8e1956da.png)

An explanation of the different components of what we will add to do monitoring
* Agent – A network daemon that listens for intervals sent via the application's DAO.
* Client – The component that implements the OpenTracing API for distributed tracing.
* Collector – The component that receives the extents and adds them to a queue for processing.
* Console – A user interface that allows users to view their distributed tracing data.
* Query – A service that retrieves traces from storage.
* Span – The logical unit of work in Jaeger, which includes the name, start time and duration of the operation.
* Trace – The way Jaeger presents execution requests. A trace consists of at least one range.

<br/>

## Installation of Jaeger & OpenTracing :
### Adding a dependance in the pom/xml of doodle/api :
```xml
<dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-smallrye-opentracing</artifactId>
</dependency>
```
<br/>

### Configuration of Jaeger in our back-end :
In the : **src/main/resources/application.yml**, we add this :

```yml
quarkus:
  jaeger:
    service-name: doodle
    sampler-type: const
    sampler-param: 1
  log:
    console:
      format: '%d{HH:mm:ss} %-5p traceId=%X{traceId}, parentId=%X{parentId}, spanId=%X{spanId}, sampled=%X{sampled} [%c{2.}] (%t) %s%e%n'
```



* For the first parameter, if the service-name parameter is not provided, a no-op tracer will be configured, which will result in no tracing data being reported to the backend.
* Second, a constant sampler is used.
* Third, sampler-param is used to set the sampling of the requests. Here there is a sampling of all the queries as it is set to 1. This parameter can go from 0 to 1.
* And finally, we add trace IDs in the log message.

<br/><br/>

## Launch of Jaeger

To launch Jaeger (in docker) simply run :
```sh
$ docker run -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 jaegertracing/all-in-one:latest
```

Finally go to: http://localhost:16686/ to open the Jaeger UI

<br/>

## Application's launch for doing tests
Once we have configured and started Jaeger, we need to launch the application:


* **In the doodle/api file, execute** :
```sh
$ docker-compose up -d
``` 
**then,**
```sh
$ ./mvnw compile quarkus:dev
``` 
<br/>

* **In the doodle/front file, execute** :
```sh
$ npm install
``` 
**then,**
```sh
$ npm start
``` 
## Application's monitoring
At the address: http://localhost:16686/, we have the graphical interface of Jaeger:
![image](https://user-images.githubusercontent.com/65306153/144050178-4005ca68-4d8e-4037-963a-6012bb742c08.png)

### By starting, we can create a survey in the application and see result on Jaeger :
Creation of our poll :
![image](https://user-images.githubusercontent.com/65306153/144050515-70dabaf7-032e-4956-8163-fa124bb97a29.png)

<br/>

Results on Jaeger :
![image](https://user-images.githubusercontent.com/65306153/144050920-501d1183-7475-4708-8849-112799a59980.png)
The dot at the top tells us the time the request was made and its duration (time to response). We can click on its trace to learn more.

<br/>

![image](https://user-images.githubusercontent.com/65306153/144051879-6e6a21ca-2673-4e16-971c-0846d969d4f6.png)
Here we can see the HTTP method (POST), the response obtained (201), the URL... In the name of the trace we can also see which method of which class was called.

<br/><br/>

Now we can try to create a meeting participant :
![image](https://user-images.githubusercontent.com/65306153/144052754-ddd26b4f-bec5-4c68-94ac-92f69fbc1f86.png)

<br/>

We click to participate and fill in the fields, then we submit :

<br/>

![image](https://user-images.githubusercontent.com/65306153/144053102-4dcc1bc8-0e55-4c2f-86a2-c7ab09cb27f9.png)

<br/><br/><br/>
Now we have new traces in Jaeger (2 GET and 1 POST) that will be used to create the participant :
![image](https://user-images.githubusercontent.com/65306153/144053270-9489cfb5-aceb-4e08-85c9-71aa9a812c0d.png)

As for the old trace we can click to have all the information on these requests.



