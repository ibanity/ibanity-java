# Java wrapper for the Ibanity API

[![Maven Central](https://img.shields.io/maven-central/v/com.ibanity.apis/ibanity-java.svg)](http://mvnrepository.com/artifact/com.ibanity.apis/ibanity-java)
[![Build Status](https://travis-ci.org/ibanity/ibanity-java.svg?branch=master)](https://travis-ci.org/ibanity/ibanity-java)
[![License](https://img.shields.io/cocoapods/l/AFNetworking.svg)](https://github.com/ibanity/ibanity-java/blob/master/LICENSE)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=com.ibanity.apis:ibanity-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.ibanity.apis:ibanity-java)
[![Maintainability](https://sonarcloud.io/api/project_badges/measure?project=com.ibanity.apis:ibanity-java&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=com.ibanity.apis:ibanity-java&metric=sqale_rating)
[![Security](https://sonarcloud.io/api/project_badges/measure?project=com.ibanity.apis:ibanity-java&metric=security_rating)](https://sonarcloud.io/component_measures?id=com.ibanity.apis:ibanity-java&metric=security_rating)
[![Reliability](https://sonarcloud.io/api/project_badges/measure?project=com.ibanity.apis:ibanity-java&metric=reliability_rating)](https://sonarcloud.io/component_measures?id=com.ibanity.apis:ibanity-java&metric=vulnerabilities)



This Java Client library offers various Services you can use in order to submit requests towards the Ibanity Platform.


## Quick start

Configure the library using IbanityServiceBuilder.builder().

Minimal configuration values are:

* The ibanity url
* Your application private key
* the passphrase for the private key
* the application public certificate

```java
IbanityService ibanityService = IbanityServiceBuilder.builder()
                .ibanityApiEndpoint("https://api.ibanity.com")
                .applicationPrivateKey(myPrivateKey)
                .passphrase("aPassphrase")
                .applicationCertificate(myCertificate)
                .build()
                
```

You can then make use of Xs2a services through your IbanityService instance.

```java
CustomerAccessTokenService customerAccessTokensService = ibanityService.xs2aService().customerAccessTokensService();
```
See ClientSample class for extended examples.

All services are thread safe and can be configured as singleton if you want to leverage frameworks like Spring.
## Requirements
* Java 8 (or above)
* Maven (for compilation)

### JCE Unlimited Strength Jurisdiction Policy Files

https://golb.hplar.ch/2017/10/JCE-policy-changes-in-Java-SE-8u151-and-8u152.html