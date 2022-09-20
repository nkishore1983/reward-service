Technologies and Tools used:<br/>
Java 13, Spring Boot<br/>
Database: H2<br/>
IDE: Intellij

1. Run the command mvn spring-boot:run
2. after the server starts, from your postman or any rest client or browser, 
hit the end point localhost:8080/customers/1/rewards
In the above end point, /customers/1/rewards fetches the rewards details for customer with id 1
3. Transaction data is loaded on start up from the script data.sql.
Valid customer ids: 1, 2, 3, 4, 5
4. When we pass any invalid customer id, service will throw a 404 not found error
http://localhost:8080/customers/12345/rewards

Notes:
Integration and unit tests are run when we issue the command
mvn clean install


