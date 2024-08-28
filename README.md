# ROTATIO REST Api

## Description

This is the backend part of the application responsible for providing data through a REST API.

## System Requirements

- Java 17
- Gradle or Maven
- MySQL or another JDBC-compatible database

## Installation & configuration

1. Clone the repository:
   ```bash
   git clone https://github.com/likeahim/rotatio-app-b.git
   cd backend-rest-api

2. Database
- Create a database and add its details in `application.properties` (located in `src/main/resources`)
- Create a user and add its details in `application.properties` (located in `src/main/resources`)
- You will need to add this connections in your IDE before running application

3. To configure your SMTP server, open the `application.properties` file and fill in the `#EMAIL CONFIGURATION`section
   with the appropriate information. You will need it to use Scheduler.
    - example configuration for gmail server:
   ```java
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your@email.com
   spring.mail.password=yourpassword
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   spring.mail.properties.mail.smtp.starttls.required=true
   spring.mail.default-encoding=UTF-8
   ```
> please notice, that there are gmail smtp settings by default
> if using default settings don't forget change `your@email.com` and `yourpassword` with your own details

4. To generate your **pdf.co** *API KEY* you need to visit this [website](https://app.pdf.co/), 
choose `Sign up`, register user, copy your *API KEY*, open the `application.properties` 
and add your `API KEY` in section `#PDFCO CONFIGURATION` under `pdf.co.api-key=`.

5. Install dependencies and run the application:
   `./gradlew bootRun`

## Testing
To run the test, use the command:
`./gradlew test`

## Related Projects
To access the frontend of this application built with Vaadin, also run the repository with 
[FRONTEND](https://github.com/likeahim/rotatio-app-f)

## License
This project is licensed under the MIT License. Details can be found in the LICENSE file.

