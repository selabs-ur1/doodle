# Tutorial Monitoring de notre application doodle

## Pré-requis
Pour pouvoir commencer , on démarre l'application doodle en exécutant :
-dans le dossier api : docker-compose up -d
	                     ./mvnw compile quarkus:dev
-dans le dossier front : npm install
	                       npm start 
 le lien : http://localhost:4200/ nous donne accès à l'interface de doodle.
 
## Open-Tracing et Jaeger
Installation de OpenTracing & Jaeger :
Ajout des dépendances dans le fichier pom.xml dans api java
<dependency>
<dependency>
  <groupId>io.jaegertracing</groupId>
  <artifactId>jaeger-client</artifactId>
  <version>0.32.0</version>
</dependency>

et

<dependency>
	<groupId>io.quarkus</groupId>
	<artifactId>quarkus-smallrye-opentracing</artifactId>
</dependency>


De ce fait , exécuter dans un powershell
docker run -d -p 5775:5775/udp -p 16686:16686 jaegertracing/all-in-one:latest 

Finalement aller sur : http://localhost:16686/ 
pour ouvrir l’UI de Jaeger


