package com.giyeon.cbnuchatbot.repository;

import com.giyeon.cbnuchatbot.domain.QNA;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
@RequiredArgsConstructor
public class QNARepository {

    private final EntityManager em;

    public void save(QNA qna){
        em.persist(qna);
    }

}
