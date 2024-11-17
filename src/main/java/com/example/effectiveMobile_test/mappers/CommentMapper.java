package com.example.effectiveMobile_test.mappers;

import com.example.effectiveMobile_test.dto.CommentDTO;
import com.example.effectiveMobile_test.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDTO commentToCommentDTO(Comment comment);

    Comment commentDTOToComment(CommentDTO commentDTO);
}
