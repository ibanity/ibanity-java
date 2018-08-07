# Java wrapper for the Ibanity API

[![Maven Central](https://img.shields.io/maven-central/v/com.ibanity.apis/ibanity-java.svg)](http://mvnrepository.com/artifact/com.ibanity.apis/ibanity-java)
[![Build Status](https://travis-ci.org/ibanity/ibanity-java.svg?branch=master)](https://travis-ci.org/ibanity/ibanity-java)
[![License](https://img.shields.io/cocoapods/l/AFNetworking.svg)](https://github.com/ibanity/ibanity-java/blob/master/LICENSE)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=com.ibanity.apis:ibanity-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.ibanity.apis:ibanity-java)
[![Maintainability](https://sonarcloud.io/api/project_badges/measure?project=com.ibanity.apis:ibanity-java&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=com.ibanity.apis:ibanity-java&metric=sqale_rating)
[![Security](https://sonarcloud.io/api/project_badges/measure?project=com.ibanity.apis:ibanity-java&metric=security_rating)](https://sonarcloud.io/component_measures?id=com.ibanity.apis:ibanity-java&metric=security_rating)
[![Reliability](https://sonarcloud.io/api/project_badges/measure?project=com.ibanity.apis:ibanity-java&metric=reliability_rating)](https://sonarcloud.io/component_measures?id=com.ibanity.apis:ibanity-java&metric=vulnerabilities)


This Java Client library offers various Services you can use in order to submit requests towards the Ibanity Platform.
## Configuration
* Adapt the ibanity.properties data based on your Ibanity environment.
(See comments inside the properties file)
## Crnk: JSON API library
For the implementation of this Ibanity APIs Client library, we rely heavily on the CRNK library that follows the JSON API specification and recommendations.

More details about CRNK can be found here: [http://www.crnk.io](http://www.crnk.io)
## Requirements
* Java 8 (or above)
* Maven (for compilation)

### JCE Unlimited Strength Jurisdiction Policy Files

https://golb.hplar.ch/2017/10/JCE-policy-changes-in-Java-SE-8u151-and-8u152.html

## Non Production Environment Tests
In order to run the tests within a local or non production environment, you need to add the following properties in the ibanity.properties file:

  * **ibanity.client.ssl.ca.certificates.folder=**(Full path of the folder containing the root_ca.crt and XXX_domain_ca.crt CA Certificates (XXX being the environment name) of the self signed certificates)
  * **ibanity.client.docker.extrahost.callback.name=**(callback hostname)
  * **ibanity.client.docker.extrahost.callback.ip=**(Docker environment IP address)
  * **ibanity.client.docker.extrahost.sandbox.authorization.name=**(sandbox-authorization hostname)
  * **ibanity.client.docker.extrahost.sandbox.authorization.ip=**(Docker environment IP address)
