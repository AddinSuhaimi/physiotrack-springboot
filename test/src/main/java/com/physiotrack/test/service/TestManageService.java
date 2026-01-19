package com.physiotrack.test.service;

import com.physiotrack.test.model.Question;
import java.util.List;

public interface TestManageService {

    List<Question> displayQuestionList();

    void addQuestion(Question question);

    void editQuestion(Question question);

    void removeQuestion(Question question);
}
