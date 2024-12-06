package com.giyeon.cbnuchatbot.bootstrap;

import com.giyeon.cbnuchatbot.config.VectorStoreProperties;
import com.giyeon.cbnuchatbot.service.LoadVectorCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@Component
public class LoadVectorStore implements CommandLineRunner {

    @Autowired
    VectorStore vectorStore;

    @Autowired
    VectorStoreProperties vectorStoreProperties;

    @Autowired
    LoadVectorCollectionService loadVectorCollectionService; // 컬렉션 생성 서비스 추가

    @Override
    public void run(String... args) throws Exception {

        loadVectorCollectionService.recreateCollection();

        if (vectorStore.similaritySearch("장학금").isEmpty()){
            log.debug("벡터에 저장중");

            vectorStoreProperties.getDocumentsToLoad().forEach(document -> {
                System.out.println("저장중: " + document.getFilename());

                TikaDocumentReader documentReader = new TikaDocumentReader(document);
                List<Document> documents = documentReader.get();

                TextSplitter textSplitter = new TokenTextSplitter();

                List<Document> splitDocuments = textSplitter.apply(documents);

                vectorStore.add(splitDocuments);
            });
        }

        log.debug("Vector 디비 저장 완료");
    }
}


















