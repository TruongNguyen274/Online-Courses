package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.CommentDTO;
import com.online.learning.model.dto.LectureDTO;
import com.online.learning.model.entity.Comment;
import com.online.learning.model.mapper.AccountMapper;
import com.online.learning.model.mapper.CommentMapper;
import com.online.learning.model.mapper.LectureMapper;
import com.online.learning.service.AccountService;
import com.online.learning.service.CommentService;
import com.online.learning.service.LectureService;
import com.online.learning.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapperImpl implements CommentMapper {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureMapper lectureMapper;

    @Override
    public CommentDTO toDTO(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedDate(DateUtil.convertDateToString(comment.getCreatedDate(), "hh:mm:ss dd-mm-yyyy"));
        commentDTO.setStatus(comment.isStatus());
        commentDTO.setCourseId(comment.getCourseId());
        commentDTO.setCommentCourse(comment.isCommentCourse());

        commentDTO.setOwnerId(comment.getOwner().getId());
        commentDTO.setOwnerDTO(accountMapper.toDTO(comment.getOwner()));

        if (comment.getLecture() != null) {
            commentDTO.setLectureId(comment.getLecture().getId());
            commentDTO.setLectureDTO(lectureMapper.toDTO(comment.getLecture()));
        } else {
            LectureDTO lectureDTO = new LectureDTO();
            commentDTO.setLectureDTO(lectureDTO);
        }

        return commentDTO;
    }

    @Override
    public List<CommentDTO> toListDTO(List<Comment> commentList) {
        if (commentList == null) {
            return null;
        }
        List<CommentDTO> result = new ArrayList<>();
        for (Comment comment : commentList) {
            if (comment != null) {
                result.add(toDTO(comment));
            }
        }

        return result;
    }

    @Override
    public Comment toEntity(CommentDTO commentDTO) {
        if (commentDTO == null) {
            return null;
        }

        Comment comment = commentService.findById(commentDTO.getId());
        if (comment == null) {
            comment = new Comment();
        }

        comment.setContent(commentDTO.getContent());
        comment.setStatus(commentDTO.isStatus());
        comment.setCourseId(commentDTO.getCourseId());
        comment.setOwner(accountService.findById(commentDTO.getOwnerId()));
        comment.setCreatedDate(DateUtil.convertStringToDate(commentDTO.getCreatedDate(), "yyyy-MM-dd hh:mm:ss"));
        comment.setLecture(lectureService.findById(commentDTO.getLectureId()));
        comment.setCommentCourse(commentDTO.isCommentCourse());

        return comment;
    }
}
