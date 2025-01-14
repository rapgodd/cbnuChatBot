package com.giyeon.cbnuchatbot.service;

import com.giyeon.cbnuchatbot.model.Answer;
import com.giyeon.cbnuchatbot.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OpenAiService implements OpenAiServiceInterface{

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    @Value("classpath:/templates/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    @Override
    public Answer getAnswer(Question question){

        //유저의 질문과 가장 유사한 상위 5개의 Chunk를 뽑아와서
        //리스트로 만든다.
        List<Document> documents = vectorStore.similaritySearch(SearchRequest
                                                    .query(question.question())
                                                    .withTopK(2));
        List<String> list = documents.stream().map(Document::getContent).toList();
        System.out.println(String.join("\n",list));


        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of("input", question.question(), "documents", String.join("\n", list)));

        ChatResponse call = chatModel.call(prompt);
        return new Answer(call.getResult().getOutput().getContent());
    }

}
