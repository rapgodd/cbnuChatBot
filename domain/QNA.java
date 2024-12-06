package com.giyeon.cbnuchatbot.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class QNA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    private String question;
    private String answer;

    public QNA(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

}
