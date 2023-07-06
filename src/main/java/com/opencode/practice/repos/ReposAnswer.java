package com.opencode.practice.repos;

import com.opencode.practice.projections.Statistics;
import com.opencode.practice.models.Answer;
import com.opencode.practice.projections.AnswerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReposAnswer extends JpaRepository<Answer,Long> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete " +
            " FROM Users_answer " +
            " WHERE (app_user_id in (?1))" +
            " AND (answer_id in (SELECT id FROM answer" +
            " WHERE question_id in (SELECT id FROM question" +
            " WHERE questionnaire_id in (?2))))")
    void deleteAnswersInUsersAnswer(long userId, long questionnaireId);

    @Query(nativeQuery = true, value = "SELECT a.id" +
            " FROM app_user au JOIN users_answer ua JOIN answer a JOIN question q JOIN questionnaire qnn" +
            " ON qnn.id = q.questionnaire_id ON q.id = a.question_id ON a.id  = ua.answer_id ON ua.app_user_id = au.id" +
            " WHERE au.id  = ?1 AND qnn.id = ?2 ORDER BY a.id  ASC")
    List<AnswerId> findAnswersInOneQuestionnaireByUserId(long  Id, long questionnaireId);


    @Query(nativeQuery = true, value = "WITH first_question AS (" +
            " SELECT app_user_id, answer_id, a.text FROM users_answer ua join answer a on ua.answer_id  = a.id WHERE a.question_id = ?1), " +
            " second_question AS (SELECT app_user_id, answer_id, a.text FROM users_answer ua JOIN answer a ON ua.answer_id  = a.id WHERE a.question_id = ?2)," +
            " joining AS (select first_question.app_user_id, first_question.answer_id AS firstAnswer," +
            " first_question.text AS firstAText, second_question.answer_id AS secondAnswer, second_question.text AS secondAText" +
            " FROM first_question FULL JOIN second_question " +
            " ON first_question.app_user_id = second_question.app_user_id)" +
            " SELECT firstAnswer, firstAText, secondAnswer,secondAText, COUNT(*) AS score" +
            " FROM joining GROUP BY firstAnswer, secondAnswer, firstAText,secondAText ORDER BY score desc")
    List<Statistics> findUsersStatistics(long questionFirstId, long questionSecondId);
}
