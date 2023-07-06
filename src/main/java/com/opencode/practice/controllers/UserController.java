package com.opencode.practice.controllers;


import com.opencode.practice.ScoreClass.ScoreQuestionnaire;
import com.opencode.practice.ScoreClass.ScoreUser;
import com.opencode.practice.exceptions.AccessExeption;
import com.opencode.practice.models.Questionnaire;
import com.opencode.practice.projections.QuestionnaireView;
import com.opencode.practice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "questionnaire")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping
    public List<QuestionnaireView> getQuestionnaireList() {
        return userService.findAllQuestionnaire();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('developers:read')")
    public Questionnaire getQuestionaryById(@PathVariable long id) {
        if (userService.getQuestionnaireById(id) == null)
            throw new AccessExeption("incorrect Data");
        else
            return userService.getQuestionnaireById(id);


    }


    @PostMapping("{userId}/{questionnaireId}")
    @PreAuthorize("hasAuthority('developers:read')")
    public void saveAnswers(@PathVariable long userId,
                            @PathVariable long questionnaireId,
                            @RequestBody List<Integer> answers) {
        userService.saveAnswers(answers, questionnaireId, userId);
    }

    @PutMapping("{userId}/{questionnaireId}")
    @PreAuthorize("hasAuthority('developers:read')")
    public void updateAnswers(@PathVariable long userId,
                              @PathVariable long questionnaireId,
                              @RequestBody List<Integer> answers) {
        userService.updateAnswers(answers, questionnaireId, userId);
    }

    @GetMapping("userscore/{userId}")
    @PreAuthorize("hasAuthority('developers:read')")
    List<ScoreQuestionnaire> getUserScoreInAllQuestionnaires(@PathVariable long userId) {
        return userService.getUserScoreInAllQuestionnaires(userId);
    }

    @GetMapping("getNoCompletedQ/{id}")
    public List<QuestionnaireView> getQuestionnaireListThatUserNoCompleted(@PathVariable long id) {
        return userService.findQuestionnairesNoCompletedByUser(id);
    }
    @GetMapping("leaderBoard/{id}")
    @PreAuthorize("hasAuthority('developers:read')")
    public List<ScoreUser> getLeaderBoard(@PathVariable long id) {
        return userService.getLeaderBordInOneQuestionnaire(id);
    }
}
