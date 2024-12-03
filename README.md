## Welcome to Marvin, the smart house robot

Marvin is a robot that can connect to an OpenHAB server, read item states and send commands via 
the REST API.

It's brain is implemented in Spring AI.

## Initial learning
Put a text file called marvin-robot.txt in your home directory.
Put a text file called marvin-environment in your home directory.
There are example files in the descriptions folder

Marvin will also read item descriptions from OpenHAB and learn about them. To enable items, add the tag Marvin to the item in OpenHAB.
Marvin will in addition receive events from OpenHAB. To enable events, add the tag Events to the item in OpenHAB.

## How to run Marvin


To use Marvin, you need to have a running OpenHAB server.

Add the following environment variables

* OPENHAB_URL http://[openhab ip]:[openhab port]
* OPENHAB_USERNAME [myopenhabusername]
* OPENHAB_PASSWORD [myopenhabpassword]
* GOOGLE_APIKEY (get your own from google)
* GOOGLE_CX (get your own from google)
* OPENAI_APIKEY (get your own from openai)


The main module that brings Marvin to life is marvin.interaction.web. It is a Spring Boot application
that contains the application.yml configuration file.

The Spring AI implemented brain is in the marvin.brain.springai module. It needs a vector store to work.

Use a docker container to run the vector store. There is a docker compose file in the marvin.brain.springai docker folder.

If you use intellij, there are two run configs bundled with the project. One for runtime and one that includes populating the vector store.

If not, to trigger the vector store population, include the Spring profile 'teach' when starting the application.


When the application has started, open localhost:8080 in your browser. You can ask Marvin questions and give commands in natural language.
## How to build Marvin

Maven is used to build Marvin.

## Cool things to try
* Notify me about dinner in 5 minutes
* Turn on the lights in the living room
* What is the temperature in the living room?
