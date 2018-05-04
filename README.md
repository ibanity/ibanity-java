# ibanity-java
#Java wrapper for the iBanity API
This Java Client library offers various Services you can use in order to submit requests towards the iBanity Platform.
## Configuration
* Adapt the ibanity.properties data based on your iBanity environment. 
(See comments inside the properties file)
## Crnk: JSON API library
For the implementation of this iBanity APIs Client library, we rely heavily on the CRNK library that follows the JSON API specification and recommendations. 

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

#Build Status
https://travis-ci.org/ibanity/ibanity-java
[![build_status](https://travis-ci.org/ibanity/ibanity-java.svg?branch=master)](https://travis-ci.org/ibanity/ibanity-java)

