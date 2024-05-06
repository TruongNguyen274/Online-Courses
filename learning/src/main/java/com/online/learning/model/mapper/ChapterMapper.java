package com.online.learning.model.mapper;

import com.online.learning.model.dto.ChapterDTO;
import com.online.learning.model.entity.Chapter;

import java.util.List;

public interface ChapterMapper {

    ChapterDTO toDTO(Chapter chapter);

    List<ChapterDTO> toListDTO(List<Chapter> chapters);

    Chapter toEntity(ChapterDTO chapterDTO);
    
}
