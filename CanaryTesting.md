# Canary Testing

## Introduction

Canary testing is a method of software verification whose principle is to deploy a new version of the application to a subset of users. It allows to test the application in a real situation while not affecting all the population using it, and in case of failure, affects less users.

For example, Meta uses the Canary Testing method on its employess applications and then can gather information quickly by gathering data and feedback from the employees without affecting the external users.

Canary testing and A/B testing are similar in deployment, but their objective is different. Canary testing is meant to test resilience of new features, while A/B testing is meant to gather feedback on user experience between multiple already working features.

Like A/B testing, the Canary method fits well with continous software deployment.

## Implementation

Canary and A/B testing being similar, we can then use the same tutorial than for [A/B testing](./ABTesting.md).

However, due to the difference in the objectives of both methods, we have to modify certain parameters. The first parameter is the population. We do not want to have a large part of the user base (for example 50% like in the A/B tutorial), so we can have different methods of user separation : 
  - Employees separation
  - 90/10 ratio (or less depending on the magnitude of the changes or risks, going as low as 99/1), 10 being the percentage of the population getting the new features to test.
