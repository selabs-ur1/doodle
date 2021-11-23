# Fuzzing tutorial

[Fuzzing or fuzz testing](https://en.wikipedia.org/wiki/Fuzzing) is a automated testing technique which consists in code blocks (*Fuzzers*) injecting false or random data as input to a program and monitoring such program for errors (crashes, failing built-in code assertions, or potential memory leaks). Put simply, Fuzzing is an automatic bug finding technique which purpose is to find software implementation faults, and identify them by using invalid data as input.

## Implementing fuzzing with libFuzz

## Some fuzzing tools
. [*Oss-Fuzz*](https://github.com/google/oss-fuzz) : it is a Google project for fuzzing of open-souce software; for a project to use Oss-Fuzz, it has to be accepted by Google's team and be a large-scale open-source project and widely used within the open-source community. For each project integrated with Oss-Fuzz, Google give a amount of money ($500 - $1000) to the developers.