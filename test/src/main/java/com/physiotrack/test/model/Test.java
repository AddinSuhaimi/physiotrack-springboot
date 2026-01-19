package com.physiotrack.test.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "screening_tests")
@Data
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    @Column(nullable = false)
    private String testName;

    private String testDesc;

    private LocalDateTime testTakenDateTime;
    private LocalDateTime testFinishDateTime;

    @Enumerated(EnumType.STRING)
    private TestType type;

    // =========================
    // Questions in this test (Many-to-Many)
    // =========================
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "test_questions",
        joinColumns = @JoinColumn(name = "test_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questionList = new ArrayList<>();

    // =========================
    // Patient responses
    // =========================
    @ElementCollection
    @CollectionTable(
        name = "test_responses",
        joinColumns = @JoinColumn(name = "test_id")
    )
    @Column(name = "response")
    private List<String> responseList = new ArrayList<>();

    private Integer score;

    // =========================
    // Domain methods
    // =========================
    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setResponseList(List<String> responseList) {
        this.responseList = responseList;
    }

    public int getScore() {
        if (score == null) score = 0;
        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            String correctAns = question.getQuestionAns();
            String patientAns = responseList.size() > i ? responseList.get(i) : "";
            if (correctAns != null && correctAns.equalsIgnoreCase(patientAns)) {
                score += 1;
            }
        }
        return score;
    }

    public void setId(Long testId) {
        this.testId = testId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    // =========================
    // Helper methods for Questions
    // =========================
    public void addQuestion(Question question) {
        questionList.add(question);
    }

    public void updateQuestion(Question question) {
        if (question == null || question.getQuestionId() == null) {
            throw new IllegalArgumentException("Question or Question ID cannot be null");
        }

        // Find the existing question in this Test's questionList
        List<Question> questions = this.getQuestionList(); // assuming getQuestionList() is available

        boolean found = false;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            if (q.getQuestionId().equals(question.getQuestionId())) {
                // Update fields
                q.setQuestionDesc(question.getQuestionDesc());
                q.setQuestionCat(question.getQuestionCat());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Question with ID " + question.getQuestionId() + " not found in this test");
        }
    }

    public void deleteQuestion(Question question) {
        questionList.remove(question);
    }
}
