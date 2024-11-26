package com.assetvisor.marvin.brain.springai.adapters;

import com.assetvisor.marvin.robot.domain.brain.AsleepException;
import com.assetvisor.marvin.robot.domain.brain.BrainResponder;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import com.assetvisor.marvin.robot.domain.jobdescription.RobotDescription;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Component;

@Component
public class BrainSpringAiAdapter implements ForInvokingBrain {

    private final Log LOG = LogFactory.getLog(BrainSpringAiAdapter.class);

    @Resource
    private PgVectorStore vectorStore;
    @Resource
    private ChatMemory chatMemory;
    @Resource
    private ChatClient.Builder chatClientBuilder;

    private ChatClient chatClient;
    private RobotDescription robotDescription;

    @Override
    public void born(
        RobotDescription robotDescription,
        List<EnvironmentDescription> environmentDescriptions
    ) {
        LOG.info("Initialising Brain with instincts...");

        this.robotDescription = robotDescription;
        this.vectorStore.add(
            environmentDescriptions.stream()
                .map(this::map)
                .collect(Collectors.toList())
        );
        LOG.info("Brain initialised.");
    }

    @Override
    public void wokenUp(
        RobotDescription robotDescription,
        List<EnvironmentFunction<?,?>> environmentFunctions
    ) {
        LOG.info("Waking up Brain with new functions...");

        this.robotDescription = robotDescription;

        var chatMemoryAdvisor = VectorStoreChatMemoryAdvisor.builder(vectorStore)
            .withConversationId("1")
            .withChatMemoryRetrieveSize(10)
            .build();

        this.chatClient = chatClientBuilder
            .defaultAdvisors(
                new MessageChatMemoryAdvisor(chatMemory)
            )
            .defaultFunctions(environmentFunctions
                .stream()
                .map(this::map)
                .toArray(FunctionCallback[]::new)
            )
            .build();
        LOG.info("Brain woken up.");
    }

    @Override
    public void invoke(Observation observation, boolean reply, BrainResponder responder) {
        invoke(map(observation), reply, responder);
    }

    private String map(Observation observation) {
        return observation.toString(); //TODO proper mapping
    }

    private FunctionCallback map(EnvironmentFunction<?, ?> environmentFunction) {
        return FunctionCallback.builder()
            .description(environmentFunction.description())
            .function(environmentFunction.name(), environmentFunction)
            .inputType(environmentFunction.inputType())
            .build();
    }

    private Document map(EnvironmentDescription environmentDescription) {
        return new Document(environmentDescription.text());
    }

    private String getEnrichedRobotDescription() {
        String documentEnrichment = """
	        \r\nUse the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
	        If unsure, simply state that you don't know.
            Take a deep breath and relax. Solve your tasks step by step");
	        DOCUMENTS:
	        {documents}
         """;
        return robotDescription.text() + documentEnrichment;
    }

    @Override
    public void invoke(String message, boolean reply, BrainResponder responder) {
        if(chatClient == null) {
            throw new AsleepException("Brain is asleep, please wake it up first.");
        }
        vectorStore.add(List.of(new Document(message)));
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(20));
        String collect = documents.stream().map(Document::getContent)
            .collect(Collectors.joining(System.lineSeparator()));
        Message createdMessage = new SystemPromptTemplate(getEnrichedRobotDescription()).createMessage(
            Map.of("documents", collect));
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(createdMessage, userMessage));

        ChatResponse chatResponse = chatClient.prompt(prompt)
            .advisors(a -> a
                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, "1")
                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, "10")
            )
            .call().chatResponse();

        String responseString = chatResponse.getResult().getOutput().getContent();
        vectorStore.add(List.of(new Document(responseString)));
        if (reply) {
            responder.respond(responseString);
        }
    }
}
