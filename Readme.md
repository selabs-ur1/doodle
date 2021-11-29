# Tutoriel Monitoring de notre application doodle

## Pré-requis
Pour pouvoir commencer , on démarre l'application doodle en exécutant :
-dans le dossier api : docker-compose up -d
	                     ./mvnw compile quarkus:dev
-dans le dossier front : npm install
	                       npm start 
 le lien : http://localhost:4200/ nous donne accès à l'interface de doodle.
 
## Open-Tracing et Jaeger

![image](https://user-images.githubusercontent.com/57901216/143829912-ed348025-33a3-4936-9dd6-44eb8e1956da.png)



Installation de OpenTracing & Jaeger :
Ajout des dépendances dans le fichier pom.xml dans api java

![image](https://user-images.githubusercontent.com/57901216/143828633-6e273eda-ba44-4f6a-91f8-2852a0aac436.png)

et

![image](https://user-images.githubusercontent.com/57901216/143828697-c0875a87-95e5-4f88-9ece-9a0fd2c5b3b5.png)


De ce fait , exécuter dans un powershell
docker run -d -p 5775:5775/udp -p 16686:16686 jaegertracing/all-in-one:latest 

Finalement aller sur : http://localhost:16686/ 
pour ouvrir l’UI de Jaeger

## Visualisation du monitoring et du logging

