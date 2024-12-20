## Welcome to Marvin, the smart house robot

Marvin is a robot that can connect to an OpenHAB server, read item states and send commands via 
the REST API.

It's brain is implemented in Spring AI.

You can talk to Marvin in natural language and ask him questions about your house. He will talk back to you.

## Initial learning
* Put a text file called marvin-robot.txt in your home directory.
* Put a text file called marvin-environment in your home directory.
* There are example files in the descriptions folder

* Marvin will read item descriptions from OpenHAB and learn about them. To enable items, add the tag Marvin to the item in OpenHAB.
* Marvin will receive events from OpenHAB. To enable events, add the tag Events to the item in OpenHAB.
* Marvin can read OpenHAB rules. To enable rules, add the tag Marvin to the rule in OpenHAB.
* Marvin can also update rules.

## How to run Marvin


To use Marvin, you need to have a running OpenHAB server.

Add the following environment variables

* OPENHAB_URL http://[openhab ip]:[openhab port]
* OPENHAB_USERNAME [myopenhabusername]
* OPENHAB_PASSWORD [myopenhabpassword]
* VECTORDB_ADDRESS [vector store address] # ex: localhost:5432
* GOOGLE_APIKEY (get your own from google)
* GOOGLE_CX (get your own from google)
* OPENAI_APIKEY (get your own from openai)
* GITHUB_CLIENT_ID (for oauth, get your own from github)
* GITHUB_CLIENT_SECRET (for oauth, get your own from github)


The main module that brings Marvin to life is marvin.interaction.web. It is a Spring Boot application
that contains the application.yml configuration file.

The Spring AI implemented brain is in the marvin.brain.springai module. It needs a vector store to work.

Use a docker container to run the Cassandra vector store. There is a docker compose file at project root. 

When cassandra is up and running you have to create a keyspace called springframework. 
You can do this by running the following command:

```
CREATE KEYSPACE springframework WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
```

When the application has started, open localhost:9090 in your browser. You will be redirected to the OAuth2 login page, currently at github.

A person record is then added in the personentry table with relation STRANGER. Change this to FRIEND and refresh the page.

There is a swagger ui included on http://localhost:9090/swagger-ui/index.html To initialise Marvin, use the POST /initialise endpoint.
 

## Cool things to try
* Change the rule for the lights in the living room to turn on at sunset
* Notify me about dinner in 5 minutes
* Turn on the lights in the living room
* What is the temperature in the living room?
