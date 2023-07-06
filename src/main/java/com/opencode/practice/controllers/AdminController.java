package com.opencode.practice.controllers;

import com.opencode.practice.projections.Statistics;
import com.opencode.practice.models.Questionnaire;
import com.opencode.practice.models.User;
import com.opencode.practice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;



    @PostMapping
    @PreAuthorize("hasAuthority('developers:write')")
    public ResponseEntity<String> createQuestionary(@RequestBody Questionnaire questionary) {
        adminService.addQuestionnaire(questionary);
        return new ResponseEntity<>("Анкета создана", HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('developers:write')")
    public ResponseEntity<String> deleteQuestionaty(@PathVariable long id) {
        adminService.deleteQuestionnaireById(id);
        return new ResponseEntity<>("Анкета удалена", HttpStatus.OK);

    }

    @GetMapping("getusers")
    @PreAuthorize("hasAuthority('developers:write')")
    public List<User> findAllUsers() {
        return adminService.findAllUsers();
    }

    @GetMapping("findUsersStatistics/{questionFirstId}/{questionSecondId}")
    @PreAuthorize("hasAuthority('developers:write')")
    public List<Statistics> findUsersStatistics(@PathVariable long questionFirstId, @PathVariable long questionSecondId) {
        return adminService.getUsersStatistics(questionFirstId, questionSecondId);
    }
}
