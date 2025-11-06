package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentDto> findAll(Long postId) {
        List<Comment> comments = commentRepository.findAll(postId);
        return comments.stream()
                .map(CommentService::toDto)
                .toList();
    }

    public CommentDto findById(Long postId, Long id) {
        return toDto(commentRepository.findById(postId, id));
    }

    public CommentDto save(Long postId, CommentDto commentDto) {
        return toDto(commentRepository.save(postId, toModel(commentDto)));
    }

    public CommentDto update(Long postId, Long id, CommentDto commentDto) {
        return toDto(commentRepository.update(postId, id, toModel(commentDto)));
    }

    public void delete(Long postId, Long id) {
        commentRepository.deleteById(postId, id);
    }

    public static CommentDto toDto(Comment comment) {
        if (comment == null) return null;
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .postId(comment.getPostId())
                .build();

    }

    public static Comment toModel(CommentDto commentDto) {
        if (commentDto == null) return null;
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .postId(commentDto.getPostId())
                .build();

    }
}
