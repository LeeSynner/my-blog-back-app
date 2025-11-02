package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findAll(Long postId) {
        return commentRepository.findAll(postId);
    }

    public Comment findById(Long postId, Long id) {
        return commentRepository.findById(postId, id);
    }

    public Comment save(Long postId, Comment comment) {
        return commentRepository.save(postId, comment);
    }

    public Comment update(Long postId, Long id, Comment comment) {
        return commentRepository.update(postId, id, comment);
    }

    public void delete(Long postId, Long id) {
        commentRepository.deleteById(postId, id);
    }
}
