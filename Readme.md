# Tutorial Monitoring de notre application doodle

## Pré-requis
Pour pouvoir commencer , on démarre l'application doodle en exécutant :
-dans le dossier api : docker-compose up -d
	                     ./mvnw compile quarkus:dev
-dans le dossier front : npm install
	                       npm start 
 le lien : http://localhost:4200/ nous donne accès à l'interface de doodle.
 
 ## Open-Tracing et Jaeger
 
 





Verify that these are installed on your computer :

- Java (JDK) 11+, e.g. [Oracle JSE](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (with the JAVA_HOME environment variable correctly set)
- [Maven](http://maven.apache.org/install.html)
- [Git](https://git-scm.com/download/)
- [Docker](https://docs.docker.com/engine/install/) (at least version 19.03.0, 20.10 preferred)
- Docker compose ([Compose V2](https://docs.docker.com/compose/cli-command/#installing-compose-v2) preferred, should be able to run 3.8 compose files)
- [Node](https://nodejs.org/en/) at least version 16
- npm at least version 8 (installed with Node)
- A Java IDE (Eclipse, IntelliJ IDEA, NetBeans, VS Code, Xcode, etc.)

If you are on Windows, Docker can not mount files outside your user folder (Unless an absolute path is provided).
Please, clone the doodle in the user folder or change the compose file to correctly mount the etherpad APIKEY.txt
