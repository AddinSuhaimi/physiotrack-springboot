package com.physiotrack.test.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.physiotrack.test.model.Question;
import com.physiotrack.test.model.Test;
import com.physiotrack.test.model.TestType;
import com.physiotrack.test.repository.TestRepository;
import com.physiotrack.test.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    @Transactional // Ensure Hibernate session is open for lazy loading
    public int evaluate() {
        Test test = testRepository.findByType(TestType.INITIAL_SCREENING)
            .orElseThrow(() ->
                new RuntimeException("Initial screening test not found"));

        Scanner scanner = new Scanner(System.in);

        // Access question list inside transaction
        List<Question> questions = test.getQuestionList();

        if (questions.isEmpty()) {
            System.out.println("[INFO] No questions found for this test.");
            return 0;
        }

        List<String> responses = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            System.out.println("\nQuestion " + (i + 1) + ":");
            System.out.println(question.getQuestionDesc());

            String answer = scanner.nextLine();
            responses.add(answer);
        }

        // Save user responses and calculate score
        test.setResponseList(responses);

        int score = test.getScore(); // Assuming getScore() calculates based on responses
        test.setScore(score);

        testRepository.save(test);

        System.out.println("\n[RESULT] Your score: " + score);
        return score;
    }
}
