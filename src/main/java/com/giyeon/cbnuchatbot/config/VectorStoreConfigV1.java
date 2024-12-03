//package com.giyeon.cbnuchatbot.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.embedding.EmbeddingModel;
//import org.springframework.ai.reader.tika.TikaDocumentReader;
//import org.springframework.ai.transformer.splitter.TokenTextSplitter;
//import org.springframework.ai.vectorstore.SimpleVectorStore;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.File;
//import java.util.List;
//
//
///**
// * 파일을 읽어서
// * 벡터 스토어에 저장한다.
// */
//@Slf4j
//@Configuration
//public class VectorStoreConfig {
//
//    @Bean
//    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel, VectorStoreProperties vectorStoreProperties) {
//        SimpleVectorStore store = new SimpleVectorStore(embeddingModel);
//        File vectorStoreFile = new File(vectorStoreProperties.getVectorStorePath());
//
//        if(vectorStoreFile.exists()) {
//            store.load(vectorStoreFile);
//        }else {
//            log.debug("로딩 중");
//            vectorStoreProperties.getDocumentsToLoad().forEach(document -> {
//                System.out.println("문서 로딩" + document.getFilename());
//                TikaDocumentReader documentReader = new TikaDocumentReader(document);
//                List<Document> documents = documentReader.get();//metadata(여러 부가 데이터)생성
//                TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();//여러 부가 데이터를 나눈다
//                List<Document> splitDocs = tokenTextSplitter.apply(documents);
//                store.add(splitDocs);//OPEN API 에 doc보내고 임베딩 값 받음
//            });
//
//            store.save(vectorStoreFile);
//        }
//        return store;
//    }
//
//}
