package com.opencode.practice.service;

import com.opencode.practice.projections.Statistics;
import com.opencode.practice.models.Questionnaire;
import com.opencode.practice.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    List<User> findAllUsers();
    void deleteQuestionnaireById(long id);
    void addQuestionnaire(Questionnaire questionnaire);
    void editQuestionnaire(long id, Questionnaire newQcuestionnaire);
    List<Statistics> getUsersStatistics(long questionFirstId, long questionSecondId);

}
