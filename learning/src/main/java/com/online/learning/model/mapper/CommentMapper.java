package com.online.learning.model.mapper;

import com.online.learning.model.dto.CommentDTO;
import com.online.learning.model.entity.Comment;

import java.util.List;

public interface CommentMapper {

    CommentDTO toDTO(Comment comment);

    List<CommentDTO> toListDTO(List<Comment> commentList);

    Comment toEntity(CommentDTO commentDTO);
    
}
