# Oxalis installation

The purpose of this document is to document how to install Oxalis as simple as possible.

## Prerequisites

* Java JDK 1.8 (newer versions should also work)
* [Maven 3+](http://maven.apache.org/download.cgi) (if you plan to build Oxalis yourself)
* [Tomcat 9+](https://tomcat.apache.org/download-90.cgi) (if you have a different JEE container, you need to figure out the differences on your own, sorry :-)
* Create `OXALIS_HOME` directory to hold configuration files, certificates etc
* Add `OXALIS_HOME` environment variable to reference that directory

All of these must be installed properly, i.e. make sure that the binaries are available from your command line.


## Checklist
When running the following commands you should expect output similar to the one shown

| Verify | Command | Expected output |
| ------ | ------- | --------------- |
| JDK 1.8 | `javac -version` | javac 1.8.0_45 |
| Maven 3 | `mvn -version` | Apache Maven 3.2.1 |
| OXALIS_HOME | `echo $OXALIS_HOME` | /Users/thore/.oxalis |


## Installation steps

1. Install Tomcat and configure it for SSL on port 443 or make sure you terminate SSL in front of Tomcat on port 443 (using nginx or similar). Please, do not change this port. Most other access points need to communicate with you and their fascist department (operations) usually frowns upon opening non-standard ports. **Do not use your PEPPOL certificate as an SSL certificate!**

2. Make sure Tomcat starts and stops and manager is available with user manager/manager

3. Obtain the binary artifacts for Oxalis by either:
   1. Downloading the binary artifacts from [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Coxalis) and unpack the distribution.
   1. Building yourself from the source at [GitHub](https://github.com/difi/oxalis/)

4. Create an Oxalis home diretory in which you place files that do not change between new releases of Oxalis.
   We recommend that you name the Oxalis home directory `.oxalis` in what is considered the home directory of the user running Oxalis. If you
   are using Tomcat, it should be the home directory of the tomcat user.
   Remember to set the `$OXALIS_HOME` environment variable referencing you Oxalis home directory in your shell startup
   script.

   Example:
       ```
       export OXALIS_HOME=~/.oxalis
       ```

5. See the [Oxalis keystore guide](/doc/keystore.md) for details on how to crete your keystore. Your certificate is validated towards the proper certificate chain during startup. Please not Oxalis is expected to not start when non-PEPPOL or expired certificates are provided.

6. Create the file `oxalis.conf`. Here is an example of how it might look:

   ```
    oxalis.keystore {
        # Relative to OXALIS_HOME
        path=peppol-keystore.jks
        password = peppol
        key.alias = ap
        key.password = peppol
    }

    # The relative name of the directory holding plugin
    oxalis.path.plugin = oxalis-plugin

    # Signals to Oxalis that we should look for plugin
    oxalis.persister.receipt = plugin

    # Where to store inbound files
    oxalis.path.inbound = /var/peppol/IN
   ```

   More information may be find in the [configuration document](../configuration.adoc).

7. Copy the file `oxalis.war` into your Tomcat deployment directory, example :

   ```
   cp oxalis-distribution/target/oxalis-distribution-x.y.z/jee/oxalis.war /users/oxalis/apache-tomcat-7.0.56/webapps
   ```

8. Start Tomcat, check the logs for any errors and make sure the [oxalis status page](http://localhost/oxalis/status) seems right (the URL could be differet for your setup).
   Note! If you intend to terminate TLS in your Tomcat instance, the status pages resides at `https://localhost:443/oxalis/status`

9. Attempt to send a sample invoice using the file `example.sh` file located in `oxalis-standalone`.
   Do not forget to review the script first!


## Testing and verifying your installation  

Testing and verification of your installation presupposes that you have performed the actions
as listed above.

* You have obtained a PEPPOL test certificate.

(The mode you are running in is detected by Oxalis by validating your certificate - no need to maintain that setting any more.)


### Sending a sample invoice to Difi's test access point

This is how you send a sample invoice to Difi's test access point using the test SML (SMK):
```
java -jar target/oxalis-standalone.jar \
     -f src/test/resources/BII04_T10_PEPPOL-v2.0_invoice.xml \
     -r 9908:810418052 \
     -s 9909:810418052
```

Verify that your sample invoice was received at
[Difi's test access point](https://test-aksesspunkt.difi.no/transmission/participant/iso6523-actorid-upis::9908:810418052)


### Sending a sample invoice to your own local access point

You need to override the use of the SML/SMP in order to send directly to your own access point.
This is done by specifying a) the URL, b) the protocol and the c) AS2 system identifier.

Here is how to send a sample invoice in PEPPOOL Bis 4A profile to your own local access point:

````
java -jar target/oxalis-standalone.jar \
     -f src/test/resources/BII04_T10_PEPPOL-v2.0_invoice.xml \
     -u http://localhost:8080/oxalis/as2 \
     -cert /path/to/your/certificate.cer
````




Good luck!
