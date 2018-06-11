# Java wrapper for the Ibanity API

[![Maven Central](https://img.shields.io/maven-central/v/com.ibanity.apis/ibanity-java.svg)](http://mvnrepository.com/artifact/com.ibanity.apis/ibanity-java)
[![Build Status](https://travis-ci.org/ibanity/ibanity-java.svg?branch=master)](https://travis-ci.org/ibanity/ibanity-java)
[![License](https://img.shields.io/cocoapods/l/AFNetworking.svg)](https://github.com/ibanity/ibanity-java/blob/master/LICENSE)

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
## Demo Client
* A sample Java Client Class (**ClientSample.java**) using the various services is available for you as demo.
    * Make sure the ibanity.properties file is configured correctly
    * run the demo client and follow the instruction
        * Note: In the execution of the Java Client, don't forget to use the generated URLs in your browser when requested.
    ```
    mvn clean compile exec:exec 
    ```
  
* A sample Java Client Class (**ClientSanboxSample.java**) to show how to create Sandbox Accounts/Transactions that can be queried by the (**ClientSample.java**)

