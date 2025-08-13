package com.wht.ai2025.app;


import com.wht.ai2025.advisor.LoggerAdvisor;
import com.wht.ai2025.advisor.Re2Advisor;
import com.wht.ai2025.constant.PromptConstant;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class TravelChatClient {

    private ChatClient chatClient;

    @Resource
    private VectorStore localVectorStore;

    @Resource
    private Advisor aliCloudRAGAdvisor;

    public TravelChatClient(ChatModel dashScopeChatModel) {

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();

        ChatClient.Builder builder = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(PromptConstant.TRAVELER_SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),
//                        new Re2Advisor(),
                        new LoggerAdvisor());

        this.chatClient = builder.build();
    }


    public String doResponse(String message, String userId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }

    public String doResponseWithLocalRAG(String message, String userId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .advisors(QuestionAnswerAdvisor.builder(localVectorStore).build())
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }

    public String doResponseWithAliCloudRAG(String message, String userId){
        ChatResponse chatResponse = chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .advisors(aliCloudRAGAdvisor)
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }

}
