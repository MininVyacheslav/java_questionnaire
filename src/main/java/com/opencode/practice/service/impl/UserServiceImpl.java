package com.opencode.practice.service.impl;

import com.opencode.practice.ScoreClass.ScoreQuestionnaire;
import com.opencode.practice.ScoreClass.ScoreUser;
import com.opencode.practice.models.Answer;
import com.opencode.practice.models.Question;
import com.opencode.practice.models.Questionnaire;
import com.opencode.practice.models.User;
import com.opencode.practice.projections.AnswerId;
import com.opencode.practice.projections.QuestionnaireView;
import com.opencode.practice.projections.UserView;
import com.opencode.practice.repos.ReposAnswer;
import com.opencode.practice.repos.ReposQuestion;
import com.opencode.practice.repos.ReposQuestionnaire;
import com.opencode.practice.repos.ReposUser;
import com.opencode.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ReposQuestionnaire reposQuestionnaire;
    @Autowired
    private ReposAnswer reposAnswer;
    @Autowired
    private ReposUser reposUser;
    @Autowired
    private ReposQuestion reposQuestion;


    @Override
    public List<QuestionnaireView> findAllQuestionnaire() {
        return reposQuestionnaire.findAllQuestionnairesAsQuestionnaireView();
    }

    @Override
    public List<QuestionnaireView> findQuestionnairesNoCompletedByUser(long userId) {
        return reposQuestionnaire.findNoQuestionnaireViewsByUserId(userId);
    }


    @Override
    public void saveAnswers(List<Integer> answers, long questionnaireId, long userId) {
        User user = reposUser.findById(userId).get();

        user.getAnswers().addAll(undestandingUserAnswers(answers, questionnaireId));
        reposUser.save(user);
    }


    @Override
    public void updateAnswers(List<Integer> answers, long questionnaireId, long userId) {
        User user = reposUser.findById(userId).get();

        reposAnswer.deleteAnswersInUsersAnswer(userId, questionnaireId);
        user.getAnswers().addAll(undestandingUserAnswers(answers, questionnaireId));
        reposUser.save(user);
    }


    @Override
    public Questionnaire getQuestionnaireById(long questionnaireId) {
        return reposQuestionnaire.findById(questionnaireId).get();
    }


    @Override
    public List<ScoreUser> getLeaderBordInOneQuestionnaire(long questionnaireId) {
        List<ScoreUser> scoreUsers = new LinkedList<>();
        Questionnaire questionnaire = reposQuestionnaire.findById(questionnaireId).get();

        List<UserView> users = reposUser.findUsersByQuestionnaireId(questionnaireId);

        List<ScoreUser> finalScoreUsers = scoreUsers;
        users.forEach(appUser -> finalScoreUsers.add(new ScoreUser(appUser, getUserScoreInOneQuestionnaire(
                getUserAnswersInOneQuestionnaire(appUser.getId(), questionnaireId), questionnaire))));
        scoreUsers = finalScoreUsers.stream()
                .sorted(Comparator.comparingInt(ScoreUser::getScore)
                        .reversed())
                .collect(Collectors.toList());

        return scoreUsers;
    }


    @Override
    public List<ScoreQuestionnaire> getUserScoreInAllQuestionnaires(long userId) {
        List<ScoreQuestionnaire> scoreQuestionnaires = new LinkedList<>();
        List<Questionnaire> questionnaires = reposQuestionnaire.findAllQuestionnairesByUserId(userId);

        for(Questionnaire questionnaire : questionnaires) {
            scoreQuestionnaires.add(new ScoreQuestionnaire(questionnaire,  getUserScoreInOneQuestionnaire(
                        getUserAnswersInOneQuestionnaire(userId, questionnaire.getId()), questionnaire)));
        }
        return scoreQuestionnaires;
    }


    @Override
    public List<AnswerId> getUserAnswersInOneQuestionnaire(long userId, long questionnaireId) {
        return reposAnswer.findAnswersInOneQuestionnaireByUserId(userId, questionnaireId);
    }

    @Override
    public int getUserScoreInOneQuestionnaire(List<AnswerId> answers, Questionnaire questionnaire) {
        int score = 0;
        for(Question question : questionnaire.getQuestions()) {
            if(answers.stream().anyMatch(answer -> answer.getId() == question.getAnswers().get(question.getRightAnswerIdx()).getId())) {
                score++;
            }
        }
        return score;
    }

    @Override
    public List<Answer> undestandingUserAnswers(List<Integer> answers, long questionnaireId) {
        List<Question> questions = reposQuestion.findQuestionsByQuestionnaireId(questionnaireId);

        List<Answer> usersAnswers = new LinkedList<>();

        for(int i = 0; i < questions.size(); i++) {
            List<Answer> answersInOneQuestion = questions.get(i).getAnswers();
            usersAnswers.add(answersInOneQuestion.get(answers.get(i)));
        }
        return usersAnswers;
    }
}
