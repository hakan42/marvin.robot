package com.assetvisor.marvin.brain.springai.adapters;

import com.assetvisor.marvin.robot.domain.brain.AsleepException;
import com.assetvisor.marvin.robot.domain.brain.BrainResponder;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.brain.ForRemembering;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.jobdescription.RobotDescription;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.vectorstore.CassandraVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Component;

@Component
public class BrainSpringAiAdapter implements ForInvokingBrain, ForRemembering {

    private final Log LOG = LogFactory.getLog(BrainSpringAiAdapter.class);
    private final int VECTORSTORE_TOPK = 20;
    private final int CHAT_MEMORY_RETRIEVE_SIZE = 10;

    @Resource
    private CassandraVectorStore vectorStore;
    @Resource
    private ChatMemory chatMemory;
    @Resource
    private ChatClient.Builder chatClientBuilder;

    private ChatClient chatClient;

    @Override
    public void teach(
        List<EnvironmentDescription> environmentDescriptions
    ) {
        this.vectorStore.add(
            environmentDescriptions.stream()
                .map(this::map)
                .collect(Collectors.toList())
        );
    }

    @Override
    public void wakeUp(
        RobotDescription robotDescription,
        List<EnvironmentFunction<?, ?>> environmentFunctions
    ) {
        LOG.info("Waking up Brain...");

        this.chatClient = chatClientBuilder
            .defaultSystem(robotDescription.text())
            .defaultAdvisors(
                new MessageChatMemoryAdvisor(chatMemory),
                new QuestionAnswerAdvisor(
                    vectorStore,
                    SearchRequest
                        .defaults()
                        .withTopK(VECTORSTORE_TOPK)
                ),
                new SimpleLoggerAdvisor()
            )
            .defaultFunctions(environmentFunctions
                .stream()
                .map(this::map)
                .toArray(FunctionCallback[]::new)
            )
            .build();
        LOG.info("Brain woken up.");
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

    @Override
    public void invoke(String message, boolean reply, BrainResponder responder) {
        if (chatClient == null) {
            throw new AsleepException("Brain is asleep, please wake it up first.");
        }

        ChatResponse chatResponse = chatClient.prompt()
            .user(message)
            .advisors(a -> a
                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, "1")
                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, CHAT_MEMORY_RETRIEVE_SIZE)
            )
            .call().chatResponse();

        assert chatResponse != null;
        String responseString = chatResponse.getResult().getOutput().getContent();
        if (reply) {
            responder.respond(responseString);
        }
    }

    @Override
    public void remember(String thought) {
        vectorStore.add(List.of(new Document(thought)));
    }
}
