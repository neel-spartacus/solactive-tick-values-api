Introduction:

System requirements:

1)OpenJDK for Java 1.8
2)Git
3)Maven 3.6.1 or higher
4)OpenCsv
5)Jackson
6)Junit 4.13
7)Swagger 2.9.2
8)Project Lombok: https://projectlombok.org


Building the project:

To build the JAR and run some tests:
  mvn clean install

To run the application:
  java -jar target/solactive-tick-values-0.0.1-SNAPSHOT.jar

Swagger UI:
  http://localhost:8080/swagger-ui/

Assumptions:
1)Since an in memory solution had to be implemented,so used map to store the tick values and the the exported csv.
  Initially,thought of implementing an in memory database but since its mentioned no database,hence dropped it.

2)Added few unit and integration test cases.

3)Added multithreading using executors for the post save call to process data asynchronously.

Improvements:
1)Caching could be implemented.

2)Since it seems to be a ready heavy service,NoSql databases/Amazon S3 can be used while scaling.

3)