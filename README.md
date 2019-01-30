cd WebChatTouchSoft-master
mvn package
deploy Server/target/server.war at localhost:8080 ( not at localhost:8080/WebChatTouchSoft )

To start console Client use:
java -jar ConsoleClient/target/ConsoleClient-1.0-jar-with-dependencies.jar

To start console Agent use:
java -jar ConsoleAgent/target/ConsoleAgent-1.0-jar-with-dependencies.jar

Rest 
Swagger at http://localhost:8080/swagger-ui