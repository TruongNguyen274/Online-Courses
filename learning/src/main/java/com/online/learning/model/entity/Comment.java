package com.online.learning.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Account owner;

    @Column(name = "course_id")
    private long courseId;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    private Date createdDate;

    @Column(name = "status")
    private boolean status;

    @Column(name = "isCommentCourse")
    private boolean isCommentCourse;

}
