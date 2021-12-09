# A/B Testing 

## Introduction

The A/B testing goal is to determine which versions from two versions of a same aplication is the more appealing to the users.
For that we are going to show to each user one version selected 
L'A/B testing consistent à comparer deux versions d'une même application en faisant tester chaques versions à un grand nombre d'utiliseurs.
On le fait de manière totalement transparente, en envoyant les utilisateur vers l'une des deux versions de manière totalement arbitraire.
On va ensuite récolter les données correspondantes au(x) critère(s) que l'on cherche à améliorer, afin de pouvoir quelle version est la plus intéressante pour nous.

## Presentation of a road map to achieve it

### étape 0 : get Doodle from the current git provided
git clone https://github.com/selabs-ur1/doodle.git

### étape 1 : download GrowthBook

```
git clone https://github.com/growthbook/growthbook.git
cd growthbook
docker-compose up -d
```

### étape 2 : Ajouter la librairie GrowthBook dans le code de base

```
https://docs.growthbook.io/lib/js
```

### étape 3 : générer la modification souhaitée


For this example will add a reset button which will clear all the selected choice on the calendar. 

Add the following code at the file front/src/app/create-poll-component/create-poll-compenent.component.html : 
```HTML
<div>
	<p-button label="Tout Supprimer"></p-button>
</div>
```
where you want to add this butto. 

### étape 4 : faire tourner les différentes versions et sauvegardés les statistiques de chacunes

### étape 5 : Visualiser, et monitorer l'impact des variations à l'aide de GrowthBook

https://www.youtube.com/watch?v=NCIEe1me9oE


Métrics calculée : nombre de cliques sur le bouton + temps passée sur la page comportant le bouton