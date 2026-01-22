package com.physiotrack.test.service.impl;

import com.physiotrack.test.model.Question;
import com.physiotrack.test.model.Test;
import com.physiotrack.test.model.TestType;
import com.physiotrack.test.repository.TestRepository;
import com.physiotrack.test.repository.QuestionRepository;
import com.physiotrack.test.service.TestManageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestManageServiceImpl implements TestManageService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;

    public TestManageServiceImpl(TestRepository testRepository, QuestionRepository questionRepository) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * <<boundary>> Test Manage Page
     * displayQuestionList()
     */
    @Override
    @Transactional(readOnly = true) // Ensure session is open for lazy loading
    public List<Question> getQuestionList() {
        Test test = getInitialScreeningTest();
        // Access questionList inside transaction
        test.getQuestionList().size(); // force initialization (optional, can be removed)
        return test.getQuestionList();
    }

    /**
     * <<control>> addQuestion()
     */
    @Override
    @Transactional // Writing operation
    public void addQuestion(String questionDesc, String questionCat, String questionAns) {
        Test test = getInitialScreeningTest();
        Question question = new Question(questionDesc, questionCat, questionAns);
        test.addQuestion(question);
        questionRepository.save(question);
        testRepository.save(test);
    }

    /**
     * <<control>> editQuestion()
     */
    @Override
    @Transactional // Writing operation
    public void editQuestion(Question question) {
        Test test = getInitialScreeningTest();
        test.updateQuestion(question);
        testRepository.save(test);
    }

    /**
     * <<control>> removeQuestion()
     */
    @Override
    @Transactional // Writing operation
    public void removeQuestion(Question question) {
        Test test = getInitialScreeningTest();
        test.deleteQuestion(question);
        questionRepository.delete(question);
        testRepository.save(test);
    }

    /**
     * Internal helper
     */
    @Transactional(readOnly = true)
    private Test getInitialScreeningTest() {
        return testRepository.findByType(TestType.INITIAL_SCREENING)
                .orElseThrow(() ->
                        new RuntimeException("Initial screening test not found")
                );
    }
}
