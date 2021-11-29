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

Une explication des différents composants de ce qu'on ajoutera pour faire du monitoring
* Agent – Un daemon réseau qui écoute les intervalles envoyés via le DAO de l'application.
* Client – Le composant qui implémente l'API OpenTracing pour le traçage distribué.
* Collecteur – Le composant qui reçoit les étendues et les ajoute dans une file d'attente à traiter.
* Console – Une interface utilisateur qui permet aux utilisateurs de visualiser leurs données de traçage distribuées.
* Requête – Un service qui récupère les traces du stockage.
* Span – L'unité logique de travail dans Jaeger, qui comprend le nom, l'heure de début et la durée de l'opération.
* Trace – La façon dont Jaeger présente les demandes d'exécution. Une trace est composée d'au moins une plage.


Installation de OpenTracing & Jaeger :
Ajout des dépendances dans le fichier pom.xml dans api java avec :
![image](https://user-images.githubusercontent.com/57901216/143852893-f7547914-a084-4f38-942c-5aa9a45daf97.png) 

pour avoir

![image](https://user-images.githubusercontent.com/57901216/143853629-1008448d-fff4-4b3d-ac25-d4bdac79a390.png)

### Créer la configuration
![image](https://user-images.githubusercontent.com/57901216/143854807-3c2c5973-85cd-4299-96b5-2e424c77b2dc.png)
* Pour le premier paramètre, si la propriété quarkus.jaeger.service-name n'est pas fournie, un traceur « no-op » sera configuré, ce qui entraînera l'absence de données de traçage signalées au backend.
* De suite, une configuration d'un échantillonneur constant est utilisé.
* En troisième lieu, un échantillonnage de toutes les demandes. On peut régler le sampler-param quelque part entre 0 et 1, par ex. 0,50
* Et finalement, nous ajoutons des ID de trace dans le message de journal.



De ce fait , exécuter dans un powershell
docker run -d -p 5775:5775/udp -p 16686:16686 jaegertracing/all-in-one:latest 

Finalement aller sur : http://localhost:16686/ 
pour ouvrir l’UI de Jaeger

## Visualisation du monitoring et du logging

