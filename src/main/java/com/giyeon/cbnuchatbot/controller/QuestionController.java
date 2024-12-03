package com.giyeon.cbnuchatbot.controller;

import com.giyeon.cbnuchatbot.model.Answer;
import com.giyeon.cbnuchatbot.model.Question;
import com.giyeon.cbnuchatbot.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final OpenAiService openAiService;

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAiService.getAnswer(question);
    }

}
