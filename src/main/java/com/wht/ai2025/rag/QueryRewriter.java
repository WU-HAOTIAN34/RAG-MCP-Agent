package com.wht.ai2025.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.stereotype.Component;


@Component
public class QueryRewriter {


    @Resource
    private ChatModel dashScopeChatModel;

    private RewriteQueryTransformer rewriteQueryTransformer;

    public QueryRewriter() {
        RewriteQueryTransformer re = RewriteQueryTransformer.builder()
                .chatClientBuilder(ChatClient.builder(dashScopeChatModel))
                .build();
        rewriteQueryTransformer = re;
    }

    public String transform(String query) {
        Query query1 = new Query(query);
        String text = rewriteQueryTransformer.transform(query1).text();
        return text;
    }

}
