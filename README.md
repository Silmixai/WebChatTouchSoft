cd WebChatTouchSoft-master
mvn package
deploy Server/target/server.war at localhost:8080 ( not at localhost:8080/WebChatTouchSoft )

To start console Client use:
java -jar ConsoleClient/target/ConsoleClient-1.0-jar-with-dependencies.jar

To start console Agent use:
java -jar ConsoleAgent/target/ConsoleAgent-1.0-jar-with-dependencies.jar


Rest
Get methods:

1) Get all free agents
http://localhost:8080/rest/agents/ready
2) Get the number of free agents
http://localhost:8080/rest/agents/ready/count
3) Get all clients in the queue
http://localhost:8080/rest/clients
4) Get detailed information about one specified agent PathParam("id")
http://localhost:8080/rest/agents/{id}
5) Get detailed information about one specified client
http://localhost:8080/rest/clients/{id}
6) Get current open chats
http://localhost:8080/rest/chats

7) Get new client messages
http://localhost:8080/rest/msg/client/receive
8) Get new agent messages
http://localhost:8080/rest/msg/agent/receive

Rest Post methods:

1) Register an client PathParam("name"), Consumes("application/x-www-form-urlencoded")
http://localhost:8080/rest/reg/client
2) Send a message to the agent from the client FormParam("message") ,Consumes("application/x-www-form-urlencoded")
http://localhost:8080/rest/msg/client/send
3) Register an agent FormParam("name"),FormParam("count"),  Consumes("application/x-www-form-urlencoded")
http://localhost:8080/rest/reg/agent
4) Send a message to the client from the agent 
FormParam("message"), FormParam("id"),")  Consumes("application/x-www-form-urlencoded")
http://localhost:8080/rest/msg/agent/send
