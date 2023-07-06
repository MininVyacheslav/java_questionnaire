package com.opencode.practice.service.impl;

import com.opencode.practice.exceptions.AccessExeption;
import com.opencode.practice.projections.Statistics;
import com.opencode.practice.models.Questionnaire;
import com.opencode.practice.models.User;
import com.opencode.practice.repos.ReposAnswer;
import com.opencode.practice.repos.ReposQuestionnaire;
import com.opencode.practice.repos.ReposUser;
import com.opencode.practice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    ReposUser reposUser;
    @Autowired
    ReposQuestionnaire reposQuestionnaire;
    @Autowired
    ReposAnswer reposAnswer;



    @Override
    public List<User> findAllUsers() {
        return reposUser.findAll();
    }

    @Override
    public void deleteQuestionnaireById(long id) {
        reposQuestionnaire.deleteById(id);
    }

    @Override
    public void addQuestionnaire(Questionnaire questionnaire) {
        reposQuestionnaire.save(questionnaire);
    }


    @Override
    public void editQuestionnaire(long id, Questionnaire newQcuestionnaire) {

        reposQuestionnaire.findById(id).map(questionnaire -> {
            questionnaire.setTitle(newQcuestionnaire.getTitle());
            for(int i = 0; i < questionnaire.getQuestions().size(); i ++) {
                questionnaire.getQuestions().get(i).setText(newQcuestionnaire.getQuestions().get(i).getText());
                questionnaire.getQuestions().get(i).setRightAnswerIdx(newQcuestionnaire.getQuestions().get(i).getRightAnswerIdx());

                for (int j = 0; j < questionnaire.getQuestions().get(i).getAnswers().size(); j++) {
                    questionnaire.getQuestions().get(i).getAnswers().get(j).setText(newQcuestionnaire.getQuestions().get(i).getAnswers().get(j).getText());
                }
            }
            return reposQuestionnaire.save(questionnaire);
        }).orElseThrow(() -> new AccessExeption("No id"));
    }

    public List<Statistics> getUsersStatistics(long questionFirstId, long questionSecondId) {
        return reposAnswer.findUsersStatistics(questionFirstId, questionSecondId);
    }
}
