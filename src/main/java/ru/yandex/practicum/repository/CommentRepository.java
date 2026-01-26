package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findAll(Long postId);

    Comment findById(Long postId, Long id);

    Comment save(Long postId, Comment comment);

    Comment update(Long postId, Long id, Comment comment);

    void deleteById(Long postId, Long id);
}
