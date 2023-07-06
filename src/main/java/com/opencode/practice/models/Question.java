package com.opencode.practice.models;

import com.opencode.practice.audit.Auditable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;



@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Question extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String text;

    private int rightAnswerIdx;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private List<Answer> answers = new LinkedList<>();
}
