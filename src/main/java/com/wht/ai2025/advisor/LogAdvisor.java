package com.wht.ai2025.advisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;

import java.util.function.Function;


@Slf4j
public class LogAdvisor implements CallAdvisor, StreamAdvisor {
    public static final Function<ChatClientRequest, String> DEFAULT_REQUEST_TO_STRING = ChatClientRequest::toString;
    public static final Function<ChatResponse, String> DEFAULT_RESPONSE_TO_STRING = ModelOptionsUtils::toJsonStringPrettyPrinter;
    private static final Logger logger = LoggerFactory.getLogger(LogAdvisor.class);
    private final Function<ChatClientRequest, String> requestToString;
    private final Function<ChatResponse, String> responseToString;
    private final int order;
    private String loggerType = "info";

    public LogAdvisor() {
        this(DEFAULT_REQUEST_TO_STRING, DEFAULT_RESPONSE_TO_STRING, 10);
    }

    public LogAdvisor(String type, int order) {
        this(DEFAULT_REQUEST_TO_STRING, DEFAULT_RESPONSE_TO_STRING, order);
        this.loggerType = type;
    }

    public LogAdvisor(String type) {
        this(DEFAULT_REQUEST_TO_STRING, DEFAULT_RESPONSE_TO_STRING, 10);
        this.loggerType = type;
    }

    public LogAdvisor(int order) {
        this(DEFAULT_REQUEST_TO_STRING, DEFAULT_RESPONSE_TO_STRING, order);
    }

    public LogAdvisor(@Nullable Function<ChatClientRequest, String> requestToString, @Nullable Function<ChatResponse, String> responseToString, int order) {
        this.requestToString = requestToString != null ? requestToString : DEFAULT_REQUEST_TO_STRING;
        this.responseToString = responseToString != null ? responseToString : DEFAULT_RESPONSE_TO_STRING;
        this.order = order;
    }

    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        this.logRequest(chatClientRequest);
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        this.logResponse(chatClientResponse);
        return chatClientResponse;
    }

    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        this.logRequest(chatClientRequest);
        Flux<ChatClientResponse> chatClientResponses = streamAdvisorChain.nextStream(chatClientRequest);
        return (new ChatClientMessageAggregator()).aggregateChatClientResponse(chatClientResponses, this::logResponse);
    }

    private void logRequest(ChatClientRequest request) {
        log.info("request: {}", request.prompt());

    }

    private void logResponse(ChatClientResponse chatClientResponse) {
        log.info("response: {}", chatClientResponse.chatResponse().getResult().getOutput().getText());

    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public int getOrder() {
        return this.order;
    }

    public String toString() {
        return LogAdvisor.class.getSimpleName();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Function<ChatClientRequest, String> requestToString;
        private Function<ChatResponse, String> responseToString;
        private int order = 0;

        private Builder() {
        }

        public Builder requestToString(Function<ChatClientRequest, String> requestToString) {
            this.requestToString = requestToString;
            return this;
        }

        public Builder responseToString(Function<ChatResponse, String> responseToString) {
            this.responseToString = responseToString;
            return this;
        }

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public LogAdvisor build() {
            return new LogAdvisor(this.requestToString, this.responseToString, this.order);
        }
    }
}
