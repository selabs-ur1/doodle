# Tutoriel Monitoring de l'application doodle avec Jaeger & OpenTracing

## Pré-requis

 
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
![image](https://user-images.githubusercontent.com/57901216/143893858-488e957b-af61-47e8-9c8e-4a2802322466.png)


### Monitoring et logging de l'application doodle 
Obervons maintenant les résultats proposés par l'outil :

