package ru.yandex.practicum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentsController {

    CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> findAll(@PathVariable(name = "postId") Long postId) {
        return commentService.findAll(postId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> findById(@PathVariable(name = "postId") Long postId, @PathVariable(name = "id") Long id) {
        Comment comment = commentService.findById(postId, id);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public Comment save(@PathVariable(name = "postId") Long postId, @RequestBody Comment comment) {
        return commentService.save(postId, comment);
    }

    @PutMapping("/{id}")
    public Comment update(@PathVariable(name = "postId") Long postId, @PathVariable(name = "id") Long id, @RequestBody Comment comment) {
        return commentService.update(postId, id, comment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "postId") Long postId, @PathVariable(name = "id") Long id) {
        commentService.delete(postId, id);
    }
}
