# DLC : Fuzzing, A/B and Canary Testing

## Fuzzing
[here](fuzz-tuto.md) is our documented fuzzing tutorial.

## A/B Testing

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
