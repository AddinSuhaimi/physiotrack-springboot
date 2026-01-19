package com.physiotrack.test.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "screening_questions")
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(nullable = false)
    private String questionDesc;

    @Column(nullable = false)
    private String questionCat; // General, Upper, Lower, Daily

    @Column(nullable = true)
    private String questionAns;

    // =========================
    // Constructors
    // =========================
    public Question(String questionDesc, String questionCat, String questionAns) {
        this.questionDesc = questionDesc;
        this.questionCat = questionCat;
        this.questionAns = questionAns;
    }

    public Question() {
        this("", "", "");
    }

    // =========================
    // Getters and Setters
    // =========================
    public String getQuestionDesc() {
        return questionDesc;
    }

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    public String getQuestionAns() {
        return questionAns;
    }

    public void setQuestionAns(String questionAns) {
        this.questionAns = questionAns;
    }

    public String getQuestionCat() {
        return questionCat;
    }

    public void setQuestionCat(String questionCat) {
        this.questionCat = questionCat;
    }
}
