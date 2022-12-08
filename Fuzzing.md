# Fuzzing
## Introduction
Fuzzing is a way of testing applications by providing random data as input trying to trigger errors such as exceptions, memory leak, stack smashing, double free or even higer level flaws such as sql injection.

Fuzzing can be applied to various type of systems : API, binaries, web apps..

Fuzzing is primarily used in two fields : V&V and Security  
Depending on the objectives the data generation techniques vary :  
- Grammar
- Dictionnary
- random mutators

## Fuzzing Doodle

Doodle is an application with a not so narrow "attack surface".  
We could choose to fuzz :
- The web front-end
- The back-end API
- The back-end binaries

