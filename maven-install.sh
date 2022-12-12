#!/bin/bash

TMP_MAVEN_VERSION=${1:-"3.8.6"}

# Download Maven
wget https://apache.org/dist/maven/maven-3/$TMP_MAVEN_VERSION/binaries/apache-maven-$TMP_MAVEN_VERSION-bin.tar.gz -P /tmp

# Unzip
sudo tar xf /tmp/apache-maven-*.tar.gz -C /opt
sudo rm /tmp/apache-maven-*-bin.tar.gz
sudo ln -s /opt/apache-maven-$TMP_MAVEN_VERSION /opt/maven

# Setup environment variables
sudo touch /etc/profile.d/maven.sh
sudo chmod +x /etc/profile.d/maven.sh
>> /etc/profile.d/maven.sh echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
>> /etc/profile.d/maven.sh echo "export M2_HOME=/opt/maven"
>> /etc/profile.d/maven.sh echo "export MAVEN_HOME=/opt/maven"
>> /etc/profile.d/maven.sh echo "export PATH=\$M2_HOME/bin:\$PATH"
