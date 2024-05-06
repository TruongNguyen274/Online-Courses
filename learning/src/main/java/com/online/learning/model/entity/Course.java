package com.online.learning.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course")
public class Course extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Account owner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "intro")
    private String intro;

    @Column(name = "price")
    private Integer price;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "duration")
    private String duration;

    @Column(name = "lecture")
    private String lecture;

    @Column(name = "enrolled")
    private String enrolled;

    @Column(name = "summary")
    private String summary;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "status")
    private boolean status;

}
