package com.wht.ai2025.app;


import com.wht.ai2025.advisor.LogAdvisor;
import com.wht.ai2025.advisor.Re2Advisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;


@Component
public class TestChatClient {

    private final ChatClient chatClient;

    public TestChatClient(ChatModel dashScopeChatModel) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(3)
                .build();
        ChatClient.Builder builder = ChatClient.builder(dashScopeChatModel)
                .defaultSystem("你是一个厨艺高超的厨师，需要给用户提供一些菜品制作方法和所需食材")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new LogAdvisor(),
                        new Re2Advisor());
        this.chatClient = builder.build();
    }

    public String sendMessage(String message, String userId) {
        ChatClient.CallResponseSpec call = chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .call();
        //String content = call.content();
        ChatResponse chatResponse = call.chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }

}
