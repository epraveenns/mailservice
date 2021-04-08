#Mail Service

This project is a micro service for asynchronous sending of emails

###Technologies Used
* Java 11
* Spring Boot 2
* Spring Data JPA
* MySQL - For logging email history
* Swagger - For API Docs
* Junit 5
* Mockito
* H2 Database for test cases
* Kafka
* Zookeeper
* Spring Retry for automatic retry of failed email delivery
* Embedded kafka for kafka integration testing
* Spring mail for sending email

###Features at a Glance
* Asynchronous mail sender
* Kafka and Zookeeper based queueing of emails
* REST API based service

###Configurations
* Kafka server can be configured in [properties](src/main/resources/application.properties)
* MySQL credentials can also be configured in [properties](src/main/resources/application.properties)

###Assumptions
* Application has been designed with an assumption that SMTP server will be provided. The configuration of this server shall be done in [properties](src/main/resources/application.properties). For demo, the code for invoking the mail server has been commented out in MailSenderService.java
* The attachment given by the user will be first downloaded. If the file is not available, the user will be notified immediately. Also, the file will be saved in the application storage layer so that while asynchronously sending the mail, we need not care if the actual file is deleted by the user
* Attachment storage layer is code to an interface. As of now, the file will be stored in the application server itself. It can be easily stored in S3 for production use by implementing the interface for production profile.
* Attachment file size check is not being done as of now.  

### Future Improvements
* Right now, only the Zookeeper and Kafka are run from docker. The entire application can be dockerized.
* Incase of email failure due to any error, we could send back an email to sender stating the cause for failure.
* Multiple consumers can be run in parallel for reduced queue waiting time
* CI/CD using Travis can be done
* Scanning the attachments for viruses and malicious content, limiting the size of uploads can be done


## To Build

### Dependencies
* OpenJDK 11
* MySQL 8. Username and password are configurable in [properties](src/main/resources/application.properties)
* MySQL CLI client
* Docker

## To Run
* Run the SQL Command `create database mailservice;`
* `docker run --name zookeeper  -p 2181:2181 -d zookeeper`
* (Replace `Praveen-MacBook-Pro.local` with your machine's hostname) `docker run -p 9092:9092 --name kafka  -e KAFKA_ZOOKEEPER_CONNECT=Praveen-MacBook-Pro.local:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://Praveen-MacBook-Pro.local:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 -d confluentinc/cp-kafka`
* `./mvnw clean install`
* `cd target/`
* `java -jar mailservice-0.0.1-SNAPSHOT.jar`

## API Docs
* Swagger API Docs can be accessed by opening http://localhost:8080/swagger-ui/# . This requires the application to be in running state.

## Architecture
