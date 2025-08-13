package com.wht.ai2025.rag;


import com.alibaba.cloud.ai.advisor.DocumentRetrievalAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.AssistantPromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;


public class RetrievalAugmentationAdvisorFactory {

    public static Advisor createInstance(VectorStore vectorStore, String city) {

        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("city", city)
                .build();

        VectorStoreDocumentRetriever vdr = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .topK(6)
                .similarityThreshold(0.8)
                .filterExpression(expression)
                .build();

        RetrievalAugmentationAdvisor res = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(vdr)
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(false)
                        .promptTemplate(new AssistantPromptTemplate("请访问https://github.com/WU-HAOTIAN34"))
                        .build())
                .build();

        return res;
    }

}
