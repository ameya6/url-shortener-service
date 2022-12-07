## URL Shortner

### Description

*   Service to generate short URLs for long URLs
*   Uses distributed UID to generate 7 character long unique code, appended at the end domain

### Implementation

*   The distributed UID service generates a 64 bit unique integer.
*   The shortener service calls the UID service and generates a 7 character unique code.
    *   eg. https://domain/52jhOS3
*   The code is alpha-numeric containing small and capital letters and digits.
*   The base 62 logic is used to create the short code

### Tech Stack

*   **Language**
    *   Java 11
*   **API**
    *   Spring Boot
        *   Spring Boot Web
        *   Spring Boot Webflux
    *   Reactor Core
        *   Events are stored reactively
    *   Lombok
    *   Log4j2
    *   Hibernate
*   **Database**
    *   TimescaleDB
        *   Stores events
    *   Elasticsearch
        *   To store spark test data
    *   CockroachDB
        *   Stores entity related data
    *   Redis
        *   Stores recently created and accessed data
*   **Deployment**
    *   Docker
    *   Kubernetes
*   **Load Testing tools**
    *   JMeter
    *   Apache Spark

### Architecture Diagram

![](https://user-images.githubusercontent.com/117295903/206191060-851d2321-0b88-4df6-8954-776411c4ad27.jpg)