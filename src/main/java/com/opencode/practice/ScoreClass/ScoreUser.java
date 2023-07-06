package com.opencode.practice.ScoreClass;

import com.opencode.practice.projections.UserView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@Data
@NoArgsConstructor
public class ScoreUser {
    private UserView user;

    private int score;

    public ScoreUser(UserView user, int score) {
        this.user = user;
        this.score = score;
    }
}
