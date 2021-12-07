# Fuzzing tutorial

[Fuzzing or fuzz testing](https://en.wikipedia.org/wiki/Fuzzing) is a automated testing technique which consists in code blocks (*Fuzzers*) injecting false or random data as input to programs and monitoring such programs for errors (crashes, failing built-in code assertions, or potential memory leaks). Put simply, Fuzzing is an automatic bug finding technique which purpose is to find software implementation faults, and identify them by using invalid data as input.

There are 2 type of fuzzing tools such as binary oriented tools and Structure-aware fuzzing tools.
> [the main difference with binary fuzzing tool such as libFuzz is that structure-aware fuzzing tools leverage domain-specific knowledge of the input format to produce inputs that are syntactically valid by construction meanwhile binary fuzzing tools like AFL and libFuzzer treat the input as a sequence of bytes. If the test program expects highly structured inputs, such as XML documents or JavaScript programs, then mutating byte-arrays often results in syntactically invalid inputs; the core of the test program remains untested.](https://github.com/rohanpadhye/jqf#what-is-structure-aware-fuzzing)

## Implementing fuzzing with Javafuzz

[**JavaFuzz**](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz) is a binary oriented, coverage-guided fuzzer for testing Java packages. It uses [**Jacoco**](https://www.jacoco.org/jacoco/) as a coverage tool.
This tutorial consists in installing Javafuzz, setting it up and use it to test the **doodle api** by using corrupt HTTP requests as input data.

### Usage of Javafuzz

#### Step 1
Setting up Javafuzz is blatantly simple.
We install Javafuzz by adding its [dependency](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz#installing) to the *pom.xml*.

#### Step 2
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

#### Step 3
The final step is to execute the following [commands](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz#running) to start the test :
```bash
mvn install
wget -O jacocoagent.jar https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz/-/raw/master/javafuzz-maven-plugin/src/main/resources/jacocoagent-exp.jar
MAVEN_OPTS="-javaagent:jacocoagent.jar" mvn javafuzz:fuzz -DclassName=fr.istic.tlc.JavafuzzExample
```
The parameter *DclassName* requires the name of the Javafuzz test class. In this tutorial it is [*fr.istic.tlc.JavafuzzExample*](https://github.com/KomInc/doodle/blob/oss-fuzz-tuto/api/src/test/java/fr/istic/tlc/JavafuzzTestExample.java).
Javafuzzer also have a optional parameter known as [Ddir](https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz#corpus); it is a corpus which refers to a specified directory whose purpose is to contain test cases on which the random generated data will be based. Depending on the project, users might need specific input data and this parameter can be really useful. In this tuto for instance, the tests are implemented so the input data should be string according to the following pattern: *HTTP_REQUEST_METHOD LINK POSSIBLE_DATA*. For instance, ```POST /api/polls/ {"title":"fuzz tuto","location":"online","description":"DLC"}```

This tutorial being in a Devops course, we decided to run the javafuzz test locally and in a pipeline.

#### Javafuzz in local environment
We run the test with the following command:
```bash
MAVEN_OPTS="-javaagent:jacocoagent.jar" mvn javafuzz:fuzz -DclassName=fr.istic.tlc.JavafuzzExample -Ddirs=CORPUS_DIR
```
We obtain the following ouput:
![Alt Image text](api/src/main/resources/images/javafuzz_local_result.png?raw=true "Javafuzz in local ouput")

We can see that the test detects bad input data such as corrupt URIs.

#### Javafuzz in pipeline
Fuzzing works out best if done continuously. As projects evolve,fuzz testing should be applied to the most recent versions of them in order to identify revisions when a regression was introduced as well as detect bug fixes and the corresponding revision.
In order to run the fuzzing in a pipeline, we set up a pipeline on github using [Github Actions](https://github.com/features/actions). We inserted the command to run javafuzz into the [pipeline](https://github.com/KomInc/doodle/blob/oss-fuzz-tuto/.github/workflows/ci.yml) file as :
```bash
- name: Fuzzing with Javafuzz
      working-directory: ./api
      run: |
        mvn install
        wget -O jacocoagent.jar https://gitlab.com/gitlab-org/security-products/analyzers/fuzzers/javafuzz/-/raw/master/javafuzz-maven-plugin/src/main/resources/jacocoagent-exp.jar
        MAVEN_OPTS="-javaagent:jacocoagent.jar" mvn javafuzz:fuzz -DclassName=fr.istic.tlc.JavafuzzTestExample -Ddirs=CORPUS_DIR
```

Every push or pull request triggers our pipeline and the Javafuzz test is executed and the results can be seen in the [Actions](https://github.com/KomInc/doodle/actions) tab. The fuzzing with javafuzz in a pipeline outputs look like :
![Alt Image text](api/src/main/resources/images/javafuzz_pipeline.PNG?raw=true "Javafuzz in pipeline")

## Implementing fuzzing with JQF and Zest

[**JQF**](https://github.com/rohanpadhye/jqf#what-is-structure-aware-fuzzing) is a structure-aware  fuzzing tool.
> JQF is a feedback-directed fuzz testing platform for Java (think: AFL/LibFuzzer but for JVM bytecode). JQF uses the abstraction of property-based testing, which makes it nice to write fuzz drivers as parameterized JUnit test methods. JQF is built on top of [junit-quickcheck](https://github.com/pholser/junit-quickcheck). JQF enables running junit-quickcheck style parameterized unit tests with the power of **coverage-guided** fuzzing algorithms such as **Zest**.

[**Zest**](https://rohan.padhye.org/files/zest-issta19.pdf)
> [Zest](https://rohan.padhye.org/files/zest-issta19.pdf) is an algorithm that biases coverage-guided fuzzing towards producing semantically valid inputs; that is, inputs that satisfy structural and semantic properties while maximizing code coverage. Zest's goal is to find deep semantic bugs that cannot be found by conventional fuzzing tools, which mostly stress error-handling logic only. By default, JQF runs Zest via the simple command: ```mvn jqf:fuzz```.

### Usage of JQF
This tutorial consists in installing JQF, setting it up and use it to test the **doodle api** by using corrupt HTTP requests as input data.

#### Step 1
The first step consists in adding the necessary dependancies and plugins to the *pom.xml* file.
We install JQF by adding its dependency to the *pom.xml*.
```
<!-- JQF: test dependency for @Fuzz annotation -->
<dependency>
    <groupId>edu.berkeley.cs.jqf</groupId>
    <artifactId>jqf-fuzz</artifactId>
    <!-- confirm the latest version at: https://mvnrepository.com/artifact/edu.berkeley.cs.jqf -->
    <version>1.7</version>
    <scope>test</scope>
</dependency>
```
Then, we add the *JQF plugin* to the *pom.xml* in order to use the command ```mvn jqf:fuzz```.
```
<!-- The JQF plugin, for invoking the command `mvn jqf:fuzz` -->
<plugin>
    <groupId>edu.berkeley.cs.jqf</groupId>
    <artifactId>jqf-maven-plugin</artifactId>
    <!-- confirm the latest version at: https://mvnrepository.com/artifact/edu.berkeley.cs.jqf -->
    <version>1.3</version>
</plugin>
```
We also need to add to the *pom.xml* the *junit-quickchec-generator* which purpose is to generate random inputs.
```
<!-- JUnit-QuickCheck: API to write generators -->
<dependency>
    <groupId>com.pholser</groupId>
    <artifactId>junit-quickcheck-generators</artifactId>
    <version>1.0</version>
    <scope>test</scope>
</dependency>
```
In this tuto, we use as input string queries following the pattern : *HTTP_REQUEST_METHOD LINK POSSIBLE_DATA*; Hence , we use the [Generex](https://github.com/mifmif/Generex) library which goal is to generate multiple string matching a regular expression. In order to use this library, we add the following library to the *pom.xml*.
```
<!-- https://mvnrepository.com/artifact/com.github.mifmif/generex -->
<dependency>
    <groupId>com.github.mifmif</groupId>
    <artifactId>generex</artifactId>
    <version>1.0.1</version>
</dependency>
```
#### Step 2
The following step is to implement a [generator class](https://github.com/rohanpadhye/JQF/wiki/Fuzzing-with-Zest#step-2-write-an-input-generator):
> JQF leverages the junit-quickcheck framework to produce structured inputs. In order to generate inputs for type T, we need a class that extends Generator<T>. Such a subclass need only provide a method that can produce random instances of T using a provided source of randomness.
The following is our generator for String objects matching our query pattern, in the file [JQFTestQueryGenerator.java](api/src/test/java/fr/istic/tlc/JQFTest/JQFTestQueryGenerator.java):
```Java
public class JQFTestQueryGenerator extends Generator<String> {

    public JQFTestQueryGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness r, GenerationStatus s) {

        Generex regex = new Generex("(GET|POST|PUT|DELETE) https?://localhost://[0-9]{4}/([a-zA-Z0-9]{1,10}/){1,5}[1-9]*");
            
        return regex.random(0, s.size());
    }

}
```
You may check out the [junit-quickcheck](https://pholser.github.io/junit-quickcheck/site/0.8.2/usage/other-types.html) documentation for advanced ways of composing generators.

#### Step 3
Here, we implement a class of a JUnit-style test driver that will have as input the values generated by our generator. Such class should be annotated with ```@RunWith(JQF.class)``` to specify to JUnit that we are using the JQF engine to invoke test methods. We should also annotate test functions with ```@Fuzz``` to tell JQF which functions to pass the generated inputs to. There is also the annotation ```@From``` which purpose is to tell JQF which specific generator class we want to use.
The following is our test class which execute many http requests, in the file [JQFTestExample.java](api/src/test/java/fr/istic/tlc/JQFTest/JQFTestExample.java):
```Java
@RunWith(JQF.class)
public class JQFTestExample {
   

    @Fuzz
    public void fuzzTest(@From(JQFTestQueryGenerator.class) String input) {

        System.out.println("\n-- Query : "+input );
        String[] queries = input.split("\\s+");;

        if (queries.length > 0 && queries[0].equals("GET"))
            testGet(queries);
        else if (queries.length > 0 && queries[0].equals("POST"))
            testPost(queries);
        else if (queries.length > 0 && queries[0].equals("PUT"))
            testPut(queries);
        else if (queries.length > 0 && queries[0].equals("DELETE"))
            testDelete(queries);

    }

}
```

#### Step 4
The final step is to execute the test. The basic command to start a JQF test is :```mvn jqf:fuzz```. There are additional parameter such as ```-Dclass``` which purpose is to specify the JQF test driver, ```-Dmethod``` which purpose is to tell JQF particular methods to test and ```-Dtime``` to specify the duration of the test.


#### JQF in local environment
We run the test with the following command:
```bash
mvn jqf:fuzz -Dclass=fr.istic.tlc.JQFTest.JQFTestExample -Dmethod=fuzzTest -Dtime=5s
```
We obtain the following ouput:
![Alt Image text](api/src/main/resources/images/JQF_local.png?raw=true "JQF in local ouput")

We can see that the test has been executed 3 times with 2 failures.

#### JQF in pipeline
We run the test in our pipeline by adding the following lines :
```bash
- name: Fuzzing with JQF and Zest
       working-directory: ./api
       run: mvn jqf:fuzz -Dclass=fr.istic.tlc.JQFTest.JQFTestExample -Dmethod=fuzzTest -Dtime=5s
```

We obtain the following ouput:
![Alt Image text](api/src/main/resources/images/JQF_pipeline.png?raw=true "JQF in pipeline ouput")

### JFQ credits
**Zest Research Paper** [ISSTA'19 paper]:

> Rohan Padhye, Caroline Lemieux, Koushik Sen, Mike Papadakis, and Yves Le Traon. 2019. **Semantic Fuzzing with Zest**. In Proceedings of the 28th ACM SIGSOFT International Symposium on Software Testing and Analysis (ISSTA’19), July 15–19, 2019, Beijing, China. ACM, New York, NY, USA, 12 pages. https://doi.org/10.1145/3293882.3330576


**JQF Tool Paper** [ISSTA'19 tool paper]:

> Rohan Padhye, Caroline Lemieux, and Koushik Sen. 2019. **JQF: Coverage-Guided Property-Based Testing in Java**. In Proceedings of the 28th ACM SIGSOFT International Symposium on Software Testing and Analysis (ISSTA ’19), July 15–19, 2019, Beijing, China. ACM, New York, NY, USA, 4 pages. https://doi.org/10.1145/3293882.3339002


## Some fuzzing tools

. [**Oss-Fuzz**](https://github.com/google/oss-fuzz) : it is a Google project for fuzzing of open-souce software; for a project to use Oss-Fuzz, it has to be accepted by Google's team and be a large-scale open-source project and widely used within the open-source community. For each project integrated with Oss-Fuzz, Google gives an amount of money ($500 - $1000) to the developers.

. [**RAFT**](https://github.com/microsoft/rest-api-fuzz-testing) : it is a Microsoft project for fuzzing REST APIs.
