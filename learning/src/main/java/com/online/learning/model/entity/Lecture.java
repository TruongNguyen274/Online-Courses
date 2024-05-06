package com.online.learning.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lecture")
public class Lecture extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Column(name = "owner_id")
    private long ownerId;

    @Column(name = "title")
    private String title;

    @Column(name = "link_video")
    private String linkVideo;

    @Column(name = "link_resource")
    private String linkResource;

    @Column(name = "time_lesson")
    private String timeLesson;

    @Column(name = "content")
    private String content;

    @Column(name = "sort_order")
    private int sortOrder;

    @Column(name = "status")
    private boolean status;

}
