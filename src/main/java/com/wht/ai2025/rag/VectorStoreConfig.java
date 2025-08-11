package com.wht.ai2025.rag;


import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class VectorStoreConfig {

    @Resource
    private MarkDownDocLoader markDownDocLoader;

    @Bean
    public VectorStore localVectorStore(EmbeddingModel dashScopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel).build();
        List<Document> documents = markDownDocLoader.loadMarkDown();
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }

}
