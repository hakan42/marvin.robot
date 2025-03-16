## Welcome to Marvin, the smart house robot

Marvin is a RAG AI agent that can connect to a house control server, read sensor states and send commands via
the REST API. At the moment, OpenHAB is supported, but Marvin can in the future be extended to other systems.

It's brain is implemented with an interface to AI and conneccts to AI using the Spring AI framework.

You can talk to Marvin in natural language and ask him questions about your house. He will talk back to you.

## Initial learning

* Put a text file called marvin-robot.txt in your home directory.
* There are example files in the descriptions folder
* Marvin will read descriptions from the connected control server and add them to a vector store for use in prompts.
* For OpenHAB items, add the tag Marvin to the item in OpenHAB.
* Marvin will receive events from the control server and use them for prompts. To enable events, add the tag Events to the item.
* Marvin can read control server rules. To enable specific rules, add the tag Marvin to the rule.
* Marvin can also update rules.

## How to run Marvin

To use Marvin, you need to have a running OpenHAB server. To authenticate Marvin against OpenHAB you either
need to create an access token or explicitly enable Basic authentication in OpenHAB
(see [openHAB REST API / Authentication](https://www.openhab.org/docs/configuration/restdocs.html#authentication)).

The user interface and REST services are protected with OAuth2. To be able to log in to Marvin, you
need to create an OAuth application in either Github or Google Auth Platform (or both).

Set at least the following environment variables:

* `EXTERNAL_ADDRESS` URL Marvin will be reachable under, e.g. http://localhost:9090
* `OPENHAB_URL` URL of the OpenHAB instance, e.g. http://[openhab ip]:[openhab port]
* `OPENHAB_USERNAME` OpenHAB username if using Basic authentication or access token
* `OPENHAB_PASSWORD` Password of the OpenHAB user if using Basic authentication
* `VECTORDB_ADDRESS` Hostname and port of the Cassandra vector store, e.g. localhost:5432
* `OPENAI_APIKEY` Personal OpenAI API key (get from your OpenAI account)

To use Github OAuth2 to log into Marvin, set the following variables:

* `GITHUB_CLIENT_ID`
* `GITHUB_CLIENT_SECRET`

To use Google OAuth2, set the following variables:

* `GOOGLE_CLIENT_ID`
* `GOOGLE_CLIENT_SECRET`

To enable Marvin to perform Google searches on your behalf, set the following variables:

* `GOOGLE_APIKEY`
* `GOOGLE_CX`

The main module that brings Marvin to life is marvin.interaction.web. It is a Spring Boot application
that contains the application.yml configuration file.

The Spring AI implemented brain is in the marvin.brain.springai module. It needs a vector store to work.

Use a docker container to run the Cassandra vector store. There is a docker compose file at project root. Keyspaces and
tables will be created and updated when the application starts.

When the application has started, open http://localhost:9090 in your browser. You will be redirected to the OAuth2 login page.

A person record is then added in the personentry table with relation STRANGER. Change this to FRIEND and refresh the page.

There is a swagger ui included on http://localhost:9090/swagger-ui/index.html
To add environment descriptions, use the /environment endpoint.
To initialise Marvin, open http://localhost:9090/initialise in your browser after authenticating. That will clear it's
memory and populate the vector store.


## Cool things to try
* Change the rule for the lights in the living room to turn on at sunset
* Notify me about dinner in 5 minutes
* Turn on the lights in the living room
* What is the temperature in the living room?
* Increase the lights in the corridor to 100 in steps of 10 with 2 seconds interval and down again to 0
* I want to watch TV with the lights at 50
