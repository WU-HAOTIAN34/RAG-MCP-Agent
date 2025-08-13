package com.wht.ai2025.rag;


import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyWordEnricher {

    @Resource
    private ChatModel dashScopeCharModel;

    public List<Document> enrich(List<Document> document) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashScopeCharModel, 4);
        return keywordMetadataEnricher.apply(document);
    }

}
