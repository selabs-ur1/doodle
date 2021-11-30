# Tutoriel Monitoring de l'application doodle avec Jaeger & OpenTracing
 
## Schéma du fonctionnement de Jaeger

![image](https://user-images.githubusercontent.com/57901216/143829912-ed348025-33a3-4936-9dd6-44eb8e1956da.png)

Une explication des différents composants de ce qu'on ajoutera pour faire du monitoring
* Agent – Un daemon réseau qui écoute les intervalles envoyés via le DAO de l'application.
* Client – Le composant qui implémente l'API OpenTracing pour le traçage distribué.
* Collecteur – Le composant qui reçoit les étendues et les ajoute dans une file d'attente à traiter.
* Console – Une interface utilisateur qui permet aux utilisateurs de visualiser leurs données de traçage distribuées.
* Requête – Un service qui récupère les traces du stockage.
* Span – L'unité logique de travail dans Jaeger, qui comprend le nom, l'heure de début et la durée de l'opération.
* Trace – La façon dont Jaeger présente les demandes d'exécution. Une trace est composée d'au moins une plage.

<br/>

## Installation de Jaeger & OpenTracing :
### Ajout d'une dépendance dans le pom.xml de doodle/api :
```xml
<dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-smallrye-opentracing</artifactId>
</dependency>
```
<br/>

### Configuration de Jaeger dans notre back
Dans le : **src/main/resources/application.yml**, nous ajoutons ceci :

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



* Pour le premier paramètre, si le paramètre `service-name` n'est pas fournie, un traceur « no-op » sera configuré, ce qui entraînera l'absence de données de traçage signalées au backend.
* Deuxièmenment, un échantillonneur constant est utilisé.
* Troisièmement, `sampler-param` sert à régler l'échantillonage des requètes. Ici il y a un échantillonnage de toutes les requètes car il est à 1. Ce paramètre peut aller de 0 à 1. 
* Et finalement, nous ajoutons des ID de trace dans le message de log.

<br/><br/>

## Lancement de Jaeger

Pour lancer Jaeger (en docker) exécuter simplement :
```sh
$ docker run -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 jaegertracing/all-in-one:latest
```

Finalement aller sur : http://localhost:16686/ pour ouvrir l’UI de Jaeger

<br/>

## Lancement de l'application pour faire des tests
Une fois que nous avons bien configuré et démarré Jaeger il nous faut lancer l'application:


* **Dans le dossier doodle/api lancer** :
```sh
$ docker-compose up -d
``` 
**puis,**
```sh
$ ./mvnw compile quarkus:dev
``` 
<br/>

* **Dans le dossier doodle/front lancer** :
```sh
$ npm install
``` 
**puis,**
```sh
$ npm start
``` 
## Monitoring de l'application
A l'adresse :  http://localhost:16686/, nous avons donc l'interface graphique de Jaeger :
![image](https://user-images.githubusercontent.com/65306153/144050178-4005ca68-4d8e-4037-963a-6012bb742c08.png)

### Pour commencer, nous pouvons créer un sondage sur l'application et voir le résultat sur Jaeger :
Création de notre poll :
![image](https://user-images.githubusercontent.com/65306153/144050515-70dabaf7-032e-4956-8163-fa124bb97a29.png)

<br/>

Résultat sur Jaeger :
![image](https://user-images.githubusercontent.com/65306153/144050920-501d1183-7475-4708-8849-112799a59980.png)
Le point en haut nous indique l'heure à laquelle la requète a été faite ainsi que sa durée (temps jusqu'à réponse).
Nous pouvons cliquer sur sa trace pour en apprendre davantage.

<br/>

![image](https://user-images.githubusercontent.com/65306153/144051879-6e6a21ca-2673-4e16-971c-0846d969d4f6.png)
Ici nous pouvons voir la méthode HTTP (POST), la réponse obtenue (201), l'URL...

<br/><br/>

Maintenant nous pouvons essayer de créer un participant à la réunion :
![image](https://user-images.githubusercontent.com/65306153/144052754-ddd26b4f-bec5-4c68-94ac-92f69fbc1f86.png)

<br/>

Nous cliquons pour participer et nous remplissons les champs, puis nous soumettons :

<br/>

![image](https://user-images.githubusercontent.com/65306153/144053102-4dcc1bc8-0e55-4c2f-86a2-c7ab09cb27f9.png)

<br/><br/><br/>
Maintenant nous avons de nouvelles traces dans Jaeger (2 GET et 1 POST) qui vont servir à créer le participant :
![image](https://user-images.githubusercontent.com/65306153/144053270-9489cfb5-aceb-4e08-85c9-71aa9a812c0d.png)

Comme pour l'ancienne trace nous pouvons cliquer pour avoir toutes les informations sur ces requètes.



