package com.physiotrack.test.service;

import com.physiotrack.test.model.Question;
import java.util.List;

public interface TestManageService {

    List<Question> getQuestionList();

    void addQuestion(String questionDesc, String questionCat, String questionAns);

    void editQuestion(Question question);

    void removeQuestion(Question question);
}
