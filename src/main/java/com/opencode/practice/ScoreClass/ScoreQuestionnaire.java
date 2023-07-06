package com.opencode.practice.ScoreClass;

import com.opencode.practice.models.Questionnaire;
import com.opencode.practice.projections.QuestionnaireView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Component;


@Component
@Data
@NoArgsConstructor
public class ScoreQuestionnaire {
    private QuestionnaireView questionnaireView;

    private int score;

    public ScoreQuestionnaire(Questionnaire questionnaire, int score) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();

        this.questionnaireView = pf.createProjection(QuestionnaireView.class, questionnaire);
        this.score = score;
    }
}
