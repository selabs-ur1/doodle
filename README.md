# Fuzzing tutorial

[Fuzzing or fuzz testing](https://en.wikipedia.org/wiki/Fuzzing) is a automated testing technique which consists in code blocks (*Fuzzers*) injecting false or random data as input to a program and monitoring such program for errors (crashes, failing built-in code assertions, or potential memory leaks). Put simply, Fuzzing is an automatic bug finding technique which purpose is to find software implementation faults, and identify them by using invalid data as input.

## Implementing fuzzing with Javafuzz
[*JavaFuzz*](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz)

### Javafuzz in local

### Javafuzz in pipeline
Fuzzing works out best if done continuously. As projects evolve,fuzz testing should be apply to the most recent versions of them in order to identify revisions when a regression was introduced as well as detect bug fixes and the corresponding revision.

## Implementing fuzzing with JQF
[*JQF*](https://github.com/rohanpadhye/jqf#what-is-structure-aware-fuzzing)

## Implementing fuzzing with RAFT

## Some fuzzing tools

. [*libFuzz*]() : Binary fuzzing tool
. [*AFL*]() : Binary fuzzing tool
. [*Zest*]() : Structure-aware fuzzing tool; [the main difference with binary fuzzing tool such as libFuzz is that structure-aware fuzzing tools leverage domain-specific knowledge of the input format to produce inputs that are syntactically valid by construction meanwhile binary fuzzing tools like AFL and libFuzzer treat the input as a sequence of bytes. If the test program expects highly structured inputs, such as XML documents or JavaScript programs, then mutating byte-arrays often results in syntactically invalid inputs; the core of the test program remains untested.](https://github.com/rohanpadhye/jqf#what-is-structure-aware-fuzzing)
. [*Oss-Fuzz*](https://github.com/google/oss-fuzz) : it is a Google project for fuzzing of open-souce software; for a project to use Oss-Fuzz, it has to be accepted by Google's team and be a large-scale open-source project and widely used within the open-source community. For each project integrated with Oss-Fuzz, Google give a amount of money ($500 - $1000) to the developers.