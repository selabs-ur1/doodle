# DLC : Fuzzing, A/B and Canary Testing

As part of the Devops course at [ESIR](https://esir.univ-rennes1.fr/), we have been asked to make a tutorial on different testing techniques such as *fuzzing*, *A/B testing* and *canary testing*. This tutorial is applied on the project [doodle](https://github.com/selabs-ur1/doodle). \
To install doodle, please follow [these instructions](https://github.com/selabs-ur1/doodle#readme).

## Fuzzing

**Fuzzing** is a automated testing technique consisting in injecting false or random data as input to programs and monitoring such programs for errors (crashes, failing built-in code assertions, or potential memory leaks).

[here](fuzz-tuto.md) is our documented fuzzing tutorial.

## A/B Testing

**A/B testing** is a testing technique which purpose is to determine which version from two versions of a same application are the most appealing to the users. 
[here](Readme_AB_Testing.md) is our documented fuzzing tutorial.

## Canary Testing

Canary release, deployment and testing are often used as synonyms. 

The name comes from the canaries that were sent into the mines to warn the workers of the presence of toxic gas. The death of the bird indicated that the levels were too high and that it was time to evacuate. Canary testing was inspired by them. The idea here is to give advance warning of problems that may arise and allow them to be dealt with without all users being affected.

In a classic development scheme, when a new feature is developed, it is deployed in a test environment and then in production. If a problem arises, it must be fixed quickly or we must go back to a previous version. 

Canary testing is a technique for testing new features while minimising their impact on users and possibly for collecting feedback.

To do this, a second application with the new functionality will be deployed on a production environment and a portion of the users (~5%) will be routed to the test version. This technique has the advantage of allowing testing with real users, in real conditions, and minimising the impact of bugs and reducing the pressure on the teams during the corrections.

## Authors

Komi Amoussou \
Mathilde Blandel \
Léa Mercier \
Haga Rakotomanana

## Credits

Benoît Combemale \
Gwendal Jouneaux 

2021 - 2022 \
ESIR3 - Spécialité "Systèmes d'Information"
