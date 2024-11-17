package com.example.effectiveMobile_test.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User commentAuthor;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}