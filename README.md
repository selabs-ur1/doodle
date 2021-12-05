# Fuzzing tutorial

[Fuzzing or fuzz testing](https://en.wikipedia.org/wiki/Fuzzing) is a automated testing technique which consists in code blocks (*Fuzzers*) injecting false or random data as input to a program and monitoring such program for errors (crashes, failing built-in code assertions, or potential memory leaks). Put simply, Fuzzing is an automatic bug finding technique which purpose is to find software implementation faults, and identify them by using invalid data as input.

There are 2 type of fuzzing tools such as binary oriented tools and Structure-aware fuzzing tool.
> [the main difference with binary fuzzing tool such as libFuzz is that structure-aware fuzzing tools leverage domain-specific knowledge of the input format to produce inputs that are syntactically valid by construction meanwhile binary fuzzing tools like AFL and libFuzzer treat the input as a sequence of bytes. If the test program expects highly structured inputs, such as XML documents or JavaScript programs, then mutating byte-arrays often results in syntactically invalid inputs; the core of the test program remains untested.](https://github.com/rohanpadhye/jqf#what-is-structure-aware-fuzzing) 

## Implementing fuzzing with Javafuzz
[**JavaFuzz**](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz) is a binary oriented, coverage-guided fuzzer for testing Java packages. It uses [**Jacoco**](https://www.jacoco.org/jacoco/) as coverage tool.
This tutorial consists in installing Javafuzz, setting it up and use it to test the **doodle api** by using corrupt HTTP requests as input data.

### Usage of Javafuzz
Setting up Javafuzz is blatantly simple.
The first step is to install Javafuzz by adding its [dependency](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz#installing) to the *pom.xml*.

The following step is to implement a function called [Fuzz Target](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz#fuzz-target) which will be called by Javafuzz in an infinite loop with random input data. In this tutorial, the fuzz target looks like :
```Java
public class JavafuzzTestExample extends AbstractFuzzTarget{

	/**
	* Fuzz Target
	* Its signature remains the same for all Javafuzz test classes
	*/
    public void fuzz(byte[] data) {

        String[] queries = parseData(data);

        if (queries.length > 0 && queries[0].equals("GET"))
            testGet(queries);
        else if (queries.length > 0 && queries[0].equals("POST"))
            testPost(queries);
        else if (queries.length > 0 && queries[0].equals("PUT"))
            testPut(queries);
        else if (queries.length > 0 && queries[0].equals("DELETE"))
            testDelete(queries);
    }
```

The final step is to execute the following [commands](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz#running) to start the test :
```
mvn install
wget -O jacocoagent.jar https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz/-/raw/master/javafuzz-maven-plugin/src/main/resources/jacocoagent-exp.jar
MAVEN_OPTS="-javaagent:jacocoagent.jar" mvn javafuzz:fuzz -DclassName=fr.istic.tlc.JavafuzzExample
```
The parameter *DclassName* requires the name of the Javafuzz test class. In this tutorial it is [*fr.istic.tlc.JavafuzzExample*](https://github.com/KomInc/doodle/blob/oss-fuzz-tuto/api/src/test/java/fr/istic/tlc/JavafuzzTestExample.java).
Javafuzzer also have a optional parameter known as [Ddir](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz#corpus); it is a corpus which refers to a specified directory which purpose is to contains test cases on which will be based the random generated data. Depending on the project, users might need specific input data and this parameter can be really useful. In this tuto for instance, the tests are implemented so the input data should be string according to the following pattern: *HTTP_REQUEST_METHOD LINK POSSIBLE_DATA*. For instance, ```POST /api/polls/ {"title":"fuzz tuto","location":"online","description":"DLC"}```

This tutorial being in a Devops course, we decided to run the javafuzz test locally and in a pipeline.

#### Javafuzz in local environment
We run the test with the following command:
```
MAVEN_OPTS="-javaagent:jacocoagent.jar" mvn javafuzz:fuzz -DclassName=fr.istic.tlc.JavafuzzExample -Ddirs=CORPUS_DIR
```
We obtain the following ouput:
![Alt Image text](api/src/main/resources/images/javafuzz_local_result.png?raw=true "Javafuzz in local ouput")

We can see that the test detect bad input data such as corrupt URIs.

#### Javafuzz in pipeline
Fuzzing works out best if done continuously. As projects evolve,fuzz testing should be apply to the most recent versions of them in order to identify revisions when a regression was introduced as well as detect bug fixes and the corresponding revision.
In order to run the fuzzing in a pipeline, we set up a pipeline on github using [Github Actions](https://github.com/features/actions). We inserted the command to run javafuzz into the [pipeline](https://github.com/KomInc/doodle/blob/oss-fuzz-tuto/.github/workflows/ci.yml) file as :
```bash
- name: Fuzzing with Javafuzz
      working-directory: ./api
      run: |
        mvn install
        wget -O jacocoagent.jar https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz/-/raw/master/javafuzz-maven-plugin/src/main/resources/jacocoagent-exp.jar
        MAVEN_OPTS="-javaagent:jacocoagent.jar" mvn javafuzz:fuzz -DclassName=fr.istic.tlc.JavafuzzTestExample -Ddirs=CORPUS_DIR
```

Every push or pull request triggers our pipeline and the Javafuzz test is executed and the results can be seen in the [Actions](https://github.com/KomInc/doodle/actions) tab. The fuzzing with javafuzz in a pipeline ouputs look like :
![Alt Image text](api/src/main/resources/images/javafuzz_pipeline.PNG?raw=true "Javafuzz in pipeline")

## Implementing fuzzing with JQF and Zest
[**JQF**](https://github.com/rohanpadhye/jqf#what-is-structure-aware-fuzzing) is a structure-aware  fuzzing tool.
> JQF is a feedback-directed fuzz testing platform for Java (think: AFL/LibFuzzer but for JVM bytecode). JQF uses the abstraction of property-based testing, which makes it nice to write fuzz drivers as parameteric JUnit test methods. JQF is built on top of [junit-quickcheck](https://github.com/pholser/junit-quickcheck). JQF enables running junit-quickcheck style parameterized unit tests with the power of **coverage-guided** fuzzing algorithms such as **Zest**.

[**Zest**](https://rohan.padhye.org/files/zest-issta19.pdf)
> [Zest](https://rohan.padhye.org/files/zest-issta19.pdf) is an algorithm that biases coverage-guided fuzzing towards producing semantically valid inputs; that is, inputs that satisfy structural and semantic properties while maximizing code coverage. Zest's goal is to find deep semantic bugs that cannot be found by conventional fuzzing tools, which mostly stress error-handling logic only. By default, JQF runs Zest via the simple command: ```mvn jqf:fuzz```. 

### Usage of JQF


### JQF in local environment


### JQF in pipeline


### JFQ credits
#### Zest Research Paper

[ISSTA'19 paper]:

> Rohan Padhye, Caroline Lemieux, Koushik Sen, Mike Papadakis, and Yves Le Traon. 2019. **Semantic Fuzzing with Zest**. In Proceedings of the 28th ACM SIGSOFT International Symposium on Software Testing and Analysis (ISSTA’19), July 15–19, 2019, Beijing, China. ACM, New York, NY, USA, 12 pages. https://doi.org/10.1145/3293882.3330576


#### JQF Tool Paper

[ISSTA'19 tool paper]:

> Rohan Padhye, Caroline Lemieux, and Koushik Sen. 2019. **JQF: Coverage-Guided Property-Based Testing in Java**. In Proceedings of the 28th ACM SIGSOFT International Symposium on Software Testing and Analysis (ISSTA ’19), July 15–19, 2019, Beijing, China. ACM, New York, NY, USA, 4 pages. https://doi.org/10.1145/3293882.3339002


## Implementing fuzzing with RAFT

## Some fuzzing tools

. [*libFuzz*]() : Binary fuzzing tool\
. [*AFL*]() : Binary fuzzing tool\
. [*Zest*]() : Structure-aware fuzzing tool\
. [*Oss-Fuzz*](https://github.com/google/oss-fuzz) : it is a Google project for fuzzing of open-souce software; for a project to use Oss-Fuzz, it has to be accepted by Google's team and be a large-scale open-source project and widely used within the open-source community. For each project integrated with Oss-Fuzz, Google give a amount of money ($500 - $1000) to the developers.
. [**fuzzing Book**](https://www.fuzzingbook.org/)