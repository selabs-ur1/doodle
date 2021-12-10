terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
}

provider "aws" {
  region                  = var.region
  shared_credentials_file = var.creds
  profile                 = "default"
}

# Create a VPC
resource "aws_vpc" "app_vpc" {
  cidr_block = var.vpc_cidr

  tags = {
    Name = "app-vpc"
  }
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.app_vpc.id

  tags = {
    Name = "vpc_igw"
  }
}

resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.app_vpc.id
  cidr_block              = var.public_subnet_cidr
  map_public_ip_on_launch = true
  availability_zone       = "eu-west-3a"

  tags = {
    Name = "public-subnet"
  }
}

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.app_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "public_rt"
  }
}

resource "aws_route_table_association" "public_rt_asso" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_rt.id
}


resource "aws_instance" "web" {
  ami             = "ami-06d79c60d7454e2af"
  instance_type   = var.instance_type
  key_name        = var.instance_key
  subnet_id       = aws_subnet.public_subnet.id
  private_ip      = "178.0.10.230"
  security_groups = [aws_security_group.sg.id]

  user_data = <<-EOF
  #!/bin/bash
  echo "*** Installing apache2"
  sudo apt update -y
  sudo apt install apache2 -y
  sudo curl -L -O https://github.com/sazagui/doodle/archive/refs/heads/master.zip
  sudo apt install nodejs -y
  sudo apt install npm -y
  sudo apt install unzip -y
  sudo unzip master.zip
  cd ./doodle-master/front/
  sudo sed -i 's/localhost/35.181.7.116/g' proxy.conf.json
  sudo sed -i 's/localhost/35.181.7.116/g' src/environments/environment.prod.ts
  sudo sed -i 's/localhost/35.181.7.116/g' src/environments/environment.ts
  sudo npm install -y
  sudo npm run build --prod
  sudo cp -r /doodle-master/front/dist/tlcfront/* /var/www/html/
  echo "*** Completed Installing apache2"
  EOF

  tags = {
    Name = "web_instance"
  }

  volume_tags = {
    Name = "web_instance"
  }
}

resource "aws_instance" "docker" {
  ami             = "ami-06d79c60d7454e2af"
  instance_type   = var.instance_type
  key_name        = var.instance_key
  subnet_id       = aws_subnet.public_subnet.id
  security_groups = [aws_security_group.sg_docker.id]
  private_ip      = "178.0.10.240"
  user_data       = <<-EOF
  #!/bin/bash
  sudo su -
  whoami > utilisateur.txt
  apt update
  apt install unzip -y
  curl -L -O https://github.com/sazagui/doodle/archive/refs/heads/master.zip
  unzip master.zip
  rm -rf master.zip
  rm -rf doodle-master/front/
  cd ./doodle-master/api/
  sed -i 's/3.8/2.1/g' docker-compose.yaml
  apt install docker.io -y
  service docker start
  apt install docker-compose -y
  usermod -a -G docker ec2-user
  systemctl enable docker
  docker-compose up --detach > compose.txt
  EOF

  tags = {
    Name = "docker_instance"
  }

  volume_tags = {
    Name = "docker_instance"
  }
}


resource "aws_instance" "backend" {
  ami             = "ami-06d79c60d7454e2af"
  instance_type   = var.instance_type
  key_name        = var.instance_key
  subnet_id       = aws_subnet.public_subnet.id
  security_groups = [aws_security_group.sg_backend.id]
  private_ip      = "178.0.10.250"
  user_data       = <<-EOF
  #!/bin/bash
  sudo su -
  whoami > utilisateur.txt
  apt update
  apt install unzip -y
  apt install maven -y
  apt install openjdk-11-jdk -y
  export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
  export PATH=$PATH:$JAVA_HOME/bin
  curl -L -O https://github.com/sazagui/doodle/archive/refs/heads/master.zip
  unzip master.zip
  rm -rf master.zip
  rm -rf doodle-master/front/
  cd ./doodle-master/api/
  sed -i 's/localhost/178.0.10.240/g' src/main/resources/application.yml
  ./mvnw install -DskipTests  > package1.txt
  java -jar target/tlcdemoApp-1.0.0-SNAPSHOT-runner.jar
  EOF

  tags = {
    Name = "backend_instance"
  }

  volume_tags = {
    Name = "backend_instance"
  }
}
