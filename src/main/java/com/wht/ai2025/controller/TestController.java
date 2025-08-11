package com.wht.ai2025.controller;


import com.wht.ai2025.app.TestChatClient;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${spring.ai.dashscope.api-key}")
    String apiKey;

    @Resource
    private TestChatClient testChatClient;

    @Resource
    private ChatModel dashScopeChatModel;


    @GetMapping("/test")
    public String test1() {
        return apiKey;
    }


    @PostMapping("")
    public String test(@RequestBody Map<String, String> map) {
        String msg = map.get("msg");
        String id = map.get("id");
        String s = testChatClient.sendMessage(msg, id);
        return s;
    }
}
