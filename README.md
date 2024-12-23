## Welcome to Marvin, the smart house robot

Marvin is a RAG AI agent that can connect to a house control server, read sensor states and send commands via 
the REST API. At the moment, OpenHAB is supported, but Marvin can in the future be extended to other systems.

It's brain is implemented with an interface to AI and conneccts to AI using the Spring AI framework.

You can talk to Marvin in natural language and ask him questions about your house. He will talk back to you.

## Initial learning
* Put a text file called marvin-robot.txt in your home directory.
* Put a text file called marvin-environment in your home directory.
* There are example files in the descriptions folder

* Marvin will read descriptions from the connected control server and add them to a vector store for use in prompts.
* For OpenHAB items, add the tag Marvin to the item in OpenHAB.
* Marvin will receive events from the control server and use them for prompts. To enable events, add the tag Events to the item.
* Marvin can read control server rules. To enable specific rules, add the tag Marvin to the rule.
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

When the application has started, open http://localhost:9090 in your browser. You will be redirected to the OAuth2 login page, currently at github.

A person record is then added in the personentry table with relation STRANGER. Change this to FRIEND and refresh the page.

There is a swagger ui included on http://localhost:9090/swagger-ui/index.html To initialise Marvin, use the POST /initialise endpoint. That will clear it's memory and populate the vector store.
 

## Cool things to try
* Change the rule for the lights in the living room to turn on at sunset
* Notify me about dinner in 5 minutes
* Turn on the lights in the living room
* What is the temperature in the living room?
* Increase the lights in the corridor to 100 in steps of 10 with 2 seconds interval and down again to 0
* I want to watch TV with the lights at 50
