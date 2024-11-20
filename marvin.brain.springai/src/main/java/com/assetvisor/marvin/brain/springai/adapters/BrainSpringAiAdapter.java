package com.assetvisor.marvin.brain.springai.adapters;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.jobdescription.RobotDescription;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.communication.ForTellingHumans;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Component;

@Component
public class BrainSpringAiAdapter implements ForInvokingBrain {

    private final Log LOG = LogFactory.getLog(BrainSpringAiAdapter.class);

    @Resource
    private PgVectorStore vectorStore;
    @Resource
    private ForTellingHumans speech;
    @Resource
    private ForTellingHumans web;
    @Resource
    private ChatClient.Builder chatClientBuilder;
    @Resource
    private ChatMemory chatMemory;

    private ChatClient chatClient;
    private RobotDescription robotDescription;

    @Override
    public void initialise(
        RobotDescription robotDescription,
        List<EnvironmentDescription> environmentDescriptions,
        List<EnvironmentFunction<?,?>> environmentFunctions
    ) {
        LOG.info("Initialising BrainSpringAiAdapter...");
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

        this.robotDescription = robotDescription;
        this.vectorStore.add(
            environmentDescriptions.stream()
                .map(this::map)
                .collect(Collectors.toList())
        );
        LOG.info("BrainSpringAiAdapter initialised.");
    }

    private FunctionCallback map(EnvironmentFunction<?, ?> environmentFunction) {
        return new FunctionCallbackWrapper.Builder<>(environmentFunction)
            .withName(environmentFunction.name())
            .withDescription(environmentFunction.description())
            .build();
    }
    private Document map(EnvironmentDescription environmentDescription) {
        return new Document(environmentDescription.text());
    }

    private String getEnrichedRobotDescription() {
        String documentEnrichment = """
	        \r\nUse the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
	        If unsure, simply state that you don't know.
	        DOCUMENTS:
	        {documents}
         """;
        return robotDescription.text() + documentEnrichment;
    }

    @Override
    public void invoke(String message, boolean reply) {
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
        if (reply) {
            speech.tell(chatResponse.getResult().getOutput().getContent());
            web.tell(chatResponse.getResult().getOutput().getContent());
        }
    }
}
