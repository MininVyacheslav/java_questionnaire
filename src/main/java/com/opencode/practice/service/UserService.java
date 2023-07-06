package com.opencode.practice.service;

import com.opencode.practice.ScoreClass.ScoreQuestionnaire;
import com.opencode.practice.ScoreClass.ScoreUser;
import com.opencode.practice.models.Answer;
import com.opencode.practice.models.Questionnaire;
import com.opencode.practice.projections.AnswerId;
import com.opencode.practice.projections.QuestionnaireView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<QuestionnaireView> findAllQuestionnaire();
    List<QuestionnaireView> findQuestionnairesNoCompletedByUser(long userId);
    void saveAnswers(List<Integer> answers, long questionnaireId, long userId);
    void updateAnswers(List<Integer> answers, long questionnaireId, long userId);
    Questionnaire getQuestionnaireById(long questionnaireId);
    List<ScoreUser> getLeaderBordInOneQuestionnaire(long questionnaireId);
    List<ScoreQuestionnaire> getUserScoreInAllQuestionnaires(long userId);

    //Helping methods
    List<AnswerId> getUserAnswersInOneQuestionnaire(long userId, long questionnaireId);
    int getUserScoreInOneQuestionnaire(List<AnswerId> answers, Questionnaire questionnaire);
    List<Answer> undestandingUserAnswers(List<Integer> answers, long questionnaireId);
}
