package com.physiotrack.test.seed;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.physiotrack.test.model.Question;
import com.physiotrack.test.model.Test;
import com.physiotrack.test.model.TestType;
import com.physiotrack.test.repository.TestRepository;

@Order(2)
@Component
public class TestSeedRunner implements CommandLineRunner {

    private final TestRepository testRepository;

    public TestSeedRunner(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedInitialScreeningTestIfMissing();
    }

    @Transactional
    private void seedInitialScreeningTestIfMissing() {
        // Check if the initial screening test already exists
        if (testRepository.findByType(TestType.INITIAL_SCREENING).isPresent()) {
            System.out.println("[SEED] Initial screening test already exists, skipping.");
            return;
        }

        // Create Test
        Test test = new Test();
        test.setType(TestType.INITIAL_SCREENING);
        test.setTestName("Initial Screening Test");
        test.setScore(0);
        test.setResponseList(new ArrayList<>());

        // Create Questions with correct answers
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(
                "Do you feel the pain or discomfort on your knee? (Y/N)",
                "Knee Pain",
                "Y"
        ));
        questions.add(new Question(
                "Do you feel the pain or discomfort on your back? (Y/N)",
                "Back Pain",
                "Y"
        ));
        questions.add(new Question(
                "Do you feel the pain or discomfort on your shoulder? (Y/N)",
                "Shoulder Pain",
                "Y"
        ));
        questions.add(new Question(
                "Do you have difficulty moving or using the affected body part? (Y/N)",
                "MOBILITY",
                "Y"
        ));
        questions.add(new Question(
                "Does the pain or discomfort affect your daily activities (walking, working, sleeping)? (Y/N)",
                "DAILY_ACTIVITY",
                "Y"
        ));
        questions.add(new Question(
                "Do you have swelling, numbness, tingling, or recent injury? (Y/N)",
                "RED_FLAG",
                "Y"
        ));


        // Assign questions to the test
        test.setQuestionList(questions);

        // Save Test along with questions (cascade will persist questions)
        testRepository.save(test);

        System.out.println("[SEED] Initial screening test seeded with " + questions.size() + " questions.");
    }
}
