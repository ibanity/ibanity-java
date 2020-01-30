# Java wrapper for the Ibanity API

[![Maven Central](https://img.shields.io/maven-central/v/com.ibanity.apis/ibanity-java.svg)](https://search.maven.org/artifact/com.ibanity.apis/ibanity-java)
[![Build Status](https://travis-ci.org/ibanity/ibanity-java.svg?branch=master)](https://travis-ci.org/ibanity/ibanity-java)
[![License](https://img.shields.io/cocoapods/l/AFNetworking.svg)](https://github.com/ibanity/ibanity-java/blob/master/LICENSE)
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
    IbanityService ibanityServiceBuilder = IbanityServiceBuilder.builder()
            .ibanityApiEndpoint("https://api.ibanity.com")
            .tlsPrivateKey(myPrivateKey)
            .passphrase("aPassphrase")
            .tlsCertificate(myCertificate)
            .build();
```

You can then make use of Xs2a services through your IbanityService instance.

```java
CustomerAccessTokenService customerAccessTokensService = ibanityService.xs2aService().customerAccessTokensService();
```
All services are thread safe and can be configured as singleton if you want to leverage frameworks like Spring.

See ClientSample class for extended examples.

### Perform custom request to Ibanity
You can perform custom http calls to Ibanity using the IbanityHttpClient.
It can be accessed by calling :

```java
IbanityHttpClient ibanityHttpClient = ibanityService.ibanityHttpClient();
```


### Use HttpSignatureService
If you want to sign http request, you can use the HttpSignatureService from the library.

Instantiate the implementation class by calling:

```java
IbanityHttpSignatureService = new IbanityHttpSignatureServiceImpl(
                                              privateKey,
                                              certificate,
                                              certificateId);
```

```java
    public interface IbanityHttpSignatureService {
    
        /**
         * Alias to be used when the request has no payload.
         * @see IbanityHttpSignatureService#getHttpSignatureHeaders(String, URL, Map, String)
         * Allows you to create the needed headers to sign an http request following draft http signature
         * @see <a href="https://tools.ietf.org/html/draft-cavage-http-signatures-09">https://tools.ietf.org/html/draft-cavage-http-signatures-09</a>
         * @param httpMethod the http method of the current request.
         * @param url the url containing host, path and query parameters.
         * @param requestHeaders the headers of the current request. All ibanity-* headers will included in the signature.
         * @return the map with signature related headers: date, digest and signature headers.
         */
        Map<String, String> getHttpSignatureHeaders(String httpMethod, URL url, Map<String, String> requestHeaders);
    
        /**
         * Allows you to create the needed headers to sign an http request following draft http signature
         * @see <a href="https://tools.ietf.org/html/draft-cavage-http-signatures-09">https://tools.ietf.org/html/draft-cavage-http-signatures-09</a>
         * @param httpMethod the http method of the current request.
         * @param url the url containing host, path and query parameters.
         * @param requestHeaders the headers of the current request. All ibanity-* headers will included in the signature.
         * @param payload the payload of the actual request.
         * @return the map with signature related headers: date, digest and signature headers.
         */
        Map<String, String> getHttpSignatureHeaders(String httpMethod, URL url, Map<String, String> requestHeaders, String payload);
    
    }

```

## Requirements
* Java 8 (or above)
* Maven (for compilation)

### JCE Unlimited Strength Jurisdiction Policy Files

https://golb.hplar.ch/2017/10/JCE-policy-changes-in-Java-SE-8u151-and-8u152.html
