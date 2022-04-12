# agenda
Basic Agenda API with Spring Boot 


# requirements
- IntelliJ IDEA Community Edition
- MySQL
- openjdk-18

# install

1. Create an empty MySQL database named "agenda"
2. Import resources/agenda.sql into the DB you just created
3. Create an MySQL user and grant it all permissions over the agenda DB
4. Open the agenda project with the IntelliJ IDE
5. Open the "application.properties" file under src/main/resources
6. Edit the "spring.datasource" properties with the appropiate information to connect to the "agenda" database


# test

1. Open Insomnia API Client (https://insomnia.rest/)
2. Open the import collection tool
3. Import the requests collection resources/insomnia_agenda-test.json


