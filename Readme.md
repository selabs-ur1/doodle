# TUTORIAL : Continuous integration, static analysis and test automation

## Requirments

Create an Ubuntu VM.

In order to setup your VM you have to install these followings.

### Install Java

Connect to your VM. Install Java by running :
`sudo apt install default-jdk-headless`

Verify that the install went well :
`java --version`

You are supposed to have at least open-jdk 11.

### Install Git

Still on your VM, install git by running :
`sudo apt install git`

### Install Docker & docker-compose

You will need to install docker and docker-compose in your VM. To do so, run the following commands :
`sudo apt install docker`
`sudo apt install docker-compose`

### Install Maven

Then, install maven in your VM by executing :
`sudo apt install maven`


## Gitlab CI

Gitlab CI is an integreted Gitlab tools. It allows you to easily run automated tasks as testing or bulding. It works by executing the steps described in the .gitlab-ci.yml file. 

### Gitlab Runner 

First of all, you need to set up a gitlab runner on a VM. To do so, you have to download and install gitlab runner by running 

`curl -LJO "https://gitlab-runner-downloads.s3.amazonaws.com/latest/deb/gitlab-runner_${arch}.deb"`

Replace `${arch}` by your VM architecture (amd64, arm, arm64)
You can know your architecture by running : `dpkg --print-architecture`

Then, install the packages needed for the gitlab runner to work. 
`dpkg -i gitlab-runner_<arch>.deb`

Finally, you have to register a runner by running :
`sudo gitlab-runner register`

You will have to enter the URL of your GitLab instance (for example for university projects : https://gitlab.istic.univ-rennes1.fr )

Then you will have to enter a token. To obtain it, go to your gitlab project (settings --> CI/CD --> runners (expand)). Then you will have to provide a description and tags for the runner. 
After that, you will need to provide the runner executor (shell in that case).

### .gitlab-ci.yml

Create a file named ".gitlab-ci.yml" at the root of your git project. Then, describe the job you want to execute. 

For example, if you want to automatically run build and test when a push is made to the repository you can add this job :

```
build-job:
  stage: build
  script:
    - cd doodle/api
    - docker-compose up -d
    - sleep 20
    - mvn clean install
    - docker-compose down 

test-job:
  stage: test
  script:
    - cd doodle/api
    - docker-compose up -d
    - sleep 20
    - mvn test
    - docker-compose down
```

## Sonarqube

If you want to have metrics about the quality of your code, you can integrate SonarQube to your project. To do so, follow these instructions and run the commands on your VM.

### PostgreSQL 

To install SonarQube, you need to have a database. First, you have to add the PostgreSQL repository by running :
` sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ \`lsb_release -cs\`-pgdg main" >> /etc/apt/sources.list.d/pgdg.list' `

Then, you will need to add the PostgreSQL signing key :
`wget -q https://www.postgresql.org/media/keys/ACCC4CF8.asc -O - | sudo apt-key add -`

After that, you have to install PostgreSQL by running :
`sudo apt install postgresql postgresql-contrib -y`

In order to automatically start the database server on reboot, you will have to play the following :
`sudo systemctl enable postgresql`

Start your database server for the first time using :
`sudo systemctl start postgresql`


### Sonar Installation

Then, you will proceed to the installation of SonarQube. To do so, you first need to Install the zip utility, which is needed to unzip the SonarQube files.
`sudo apt-get install zip -y`

Get the files needed to install the latest version of Sonar (here 9.2):
`sudo wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-9.2.1.49989.zip`

Unzip the files you just download :
`sudo unzip sonarqube-9.2.1.49989.zip`

Then, you have to move the unzipped files to /opt/sonarqube directory :
`sudo mv sonarqube-9.2.1.49989 /opt/sonarqube`

#### Add SonarQube Group and User

You need to create a group and user sonar to grant the permissions needed.
First, create a sonar group :
`sudo groupadd sonar`

Then, create a sonar user and set /opt/sonarqube as the home directory.
`sudo useradd -d /opt/sonarqube -g sonar sonar`

Finally, grant the sonar user access to the /opt/sonarqube directory.
`sudo chown sonar:sonar /opt/sonarqube -R`

#### Connect the Database to SonarQube

Now that you have your database and your sonar server running, you will need to link them together. This link is made thanks to the jdbc plugin.

To do so, you have to open the sonar configuration file by running :
`sudo vim /opt/sonarqube/conf/sonar.properties`

Find the line where there is 

``` 
    #sonar.jdbc.username=
    #sonar.jdbc.password=
```

Uncomment those lines and connect yourself with your database log. By default, you can use the standard configuration by connecting as admin. Or, you can create your own Postgre user and add it to your configuration. This second method is recommended to have a more reliable application.
For this tutorial, we chose the first option and uncommented those lines by addind the admin configuration :

``` 
    sonar.jdbc.username=admin
    sonar.jdbc.password=admin
```

Then, you will need to add the sonar.jdbc.url :
`sonar.jdbc.url=jdbc:postgresql://localhost:5432/sonarqube`

Save your updates and exit (echap + :wq if you are using vim).

After that, you will need to start the sonar application with the sonar user created before :
To do so, edit the sonar script :
`sudo vim /opt/sonarqube/bin/linux-x86-64/sonar.sh`

Find the line :
`#RUN_AS_USER=`

Uncomment it and add "sonar" after the "=".
Save your updates and exit (echap + :wq if you are using vim).

#### Systemd service

This step will allow you to start your sonar server once your system is booted.
To do so, edit your systemd service file to start SonarQube at system boot by running :
`sudo vim /etc/systemd/system/sonar.service`

Add the following lines :

```` 
[Unit]
Description=SonarQube service
After=syslog.target network.target

[Service]
Type=forking

ExecStart=/opt/sonarqube/bin/linux-x86-64/sonar.sh start
ExecStop=/opt/sonarqube/bin/linux-x86-64/sonar.sh stop

User=sonar
Group=sonar
Restart=always

LimitNOFILE=65536
LimitNPROC=4096

[Install]
WantedBy=multi-user.target

```` 

Save your updates and exit (echap + :wq if you are using vim).

Then, you need to enable the running of your SonarQube server when your system starts.
`sudo systemctl enable sonar`

Start your SonarQube server once and for all by running :
`sudo systemctl start sonar`

Now, you can access your SonarQube server interface by going to the following URL:
http://{your VM IP address}:9000

You can log to the server using username and password admin 

*We followed this tutorial to install SonarQube and PostgreSQL : 
https://www.vultr.com/docs/install-sonarqube-on-ubuntu-20-04-lts*


### Sonar analysis automation

If you want to automatize the sonar analysis task, you can add a job to your .gitlab-ci.yml file. 

```
sonarqube-check:
  stage: deploy
  variables:
    SONAR_USER_HOME: "${CI_PROJECT_DIR}/.sonar"  # Defines the location of the analysis task cache
    GIT_DEPTH: "0"  # Tells git to fetch all the branches of the project, required by the analysis task
  cache:
    key: "${CI_JOB_NAME}"
    paths:
      - .sonar/cache
  script:
    - cd {Your Pom Location}
    - docker-compose up -d
    - sleep 20
    # Change the name of the variable according to what you need
    - mvn verify sonar:sonar -Dsonar.projectName={PROJECT_NAME} -Dsonar.host.url={SONAR_SERVER_URL} -Dsonar.login={SONAR_TOKEN} -Dsonar.links.scm={GITLAB_PROJECT_LINK}
    - docker-compose down
```

In order to create a sonar token, you need to go to your instance. Then go to your account, in the "Security" tab. There, you can generate tokens. You have to copy the generated token immediatly after it's created because you won't have access to it after that.

## PIT Mutation Testing 

PITest helps you verify the quality of your tests by integrating some mutation on your code. It will see if your tests still pass or not. 

To do so, you just have to paste the following code in your pom.xml :
```
<plugin>
    <groupId>org.pitest</groupId>
    <artifactId>pitest-maven</artifactId>
    <version>LATEST</version>
 </plugin>
 ```

 Then, to run it, execute this command : 
 `mvn org.pitest:pitest-maven:mutationCoverage`

And see the results in the target/pit-reports directory of your maven project. 

You can also add this command in your .gitlab-ci.yml file to automate the generation of the report. 



This tutorial was made by Marie JULLIOT, Jérémy GEORGES, Samuel LE DEAN & Marie ROME on a gitlab repository : https://gitlab.istic.univ-rennes1.fr/18005675/projet_dlc 