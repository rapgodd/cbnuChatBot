package com.giyeon.cbnuchatbot.service;

import com.giyeon.cbnuchatbot.model.Answer;
import com.giyeon.cbnuchatbot.model.Question;

public interface OpenAiServiceInterface {

    Answer getAnswer(Question question);

}
