# A/B Testing

## Introduction

The A/B testing goal is to determine which versions from two versions of a same application are the most appealing to the users.
For that, we are going to show to each user one selected version.
We send to the users one of the two versions in a completely transparent way.
We will then collect the data corresponding to the criterion (s) that we are trying to improve, in order to be able to determine which version is the most interesting for us.
.

## Presentation of a road map to achieve it

### Step 0 : get Doodle from the current git provided
git clone https://github.com/selabs-ur1/doodle.git
And follow the instructions in the readme file.

### Step 1 : Download GrowthBook and start it
For this tutorial we will use GrowthBook, in a terminal run the following instruction :

```
git clone https://github.com/growthbook/growthbook.git
cd growthbook
docker-compose up -d
```

Then go to [localhost:3000](http://localhost:3000).

You should arrive to this page : ![Alt Image text](front/src/assets/etape1.PNG "Start menu")

Click on Register and fill the box as the photo (no data will be save as long you stay on localhost)

![Alt Image text](front/src/assets/etape-register.PNG "Etape register")

Connect with the information you have provided

Go to the section Metrics click to "Add your first metric"

A metric will design on which way you will compare your version A and B

![Alt Image text](front/src/assets/new_metric.PNG "new metric")

Set up your metric as you want. In this tutorial we will use a Binomial metric and let the default value.
<img src="front/src/assets/new-metric2.PNG" width="50%" height="50%">



<img src="front/src/assets/new-metric3.PNG" width="50%" height="50%">

<img src="front/src/assets/new-metric4.PNG" width="50%" height="50%">



### Step 2 : Adding grothwbook on JS

For adding the library in js, type the following command

```
cd front
npm install --save @growthbook/growthbook
```

If you need more information, refer to the [documentation](https://docs.growthbook.io/lib/js)


### Step 3 : Generate modification


For this example will add a reset button which will clear all the selected choice on the calendar.

Add the following code in the file front/src/app/create-poll-component/create-poll-compenent.component.html :
```HTML
<div>
    <p-button label="Tout Supprimer"></p-button>
</div>
```
where you want to add this button. (We will not implemant all the interaction with this button as long we just need an easy example)

And implement your growthbook in Js like in the [documentation](https://docs.growthbook.io/lib/js)

### Step 4 : Create an experimentation

Return to GrowthBook and go to the Experimentation section and create a new one.

Fill the box (like the photo for example) :
<img src="front/src/assets/new-experiment.PNG" width="50%" height="50%">
<img src="front/src/assets/new-experiment1.PNG" width="50%" height="50%">
<img src="front/src/assets/new-experiment2.PNG" width="50%" height="50%">
<img src="front/src/assets/new-experiment3.PNG" width="50%" height="50%">

Click on Start for running the test and wait to harvest your statistics

### Step 5 : The results
If you want to look what you can have, check this [video](https://youtu.be/OY8DvD7eIiE?t=78)


### Going deeper

GrowthBooks allows you to use your different tools such as Google Analytics, other databases such as Postgresql, MariaDB.


