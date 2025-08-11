package com.wht.ai2025.advisor;


import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import com.wht.ai2025.constant.CommonConstant;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliCloudRAGAdvisorConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApi;

    @Bean
    public Advisor aliCloudRAGAdvisor(){
        DashScopeApi build = DashScopeApi.builder().apiKey(dashScopeApi).build();
        DashScopeDocumentRetriever dashScopeDocumentRetriever = new DashScopeDocumentRetriever(build, DashScopeDocumentRetrieverOptions.builder()
                .withIndexName(CommonConstant.TRAVELER_KNOWLEDGE).build());
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(dashScopeDocumentRetriever)
                .build();
    }

}
