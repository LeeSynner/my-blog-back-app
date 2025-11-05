package ru.yandex.practicum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CommentDto;
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
    public List<CommentDto> findAll(@PathVariable(name = "postId") Long postId) {
        return commentService.findAll(postId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable(name = "postId") Long postId, @PathVariable(name = "id") Long id) {
        CommentDto commentDto = commentService.findById(postId, id);
        if (commentDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commentDto);
    }

    @PostMapping
    public CommentDto save(@PathVariable(name = "postId") Long postId, @RequestBody CommentDto commentDto) {
        return commentService.save(postId, commentDto);
    }

    @PutMapping("/{id}")
    public CommentDto update(@PathVariable(name = "postId") Long postId, @PathVariable(name = "id") Long id, @RequestBody CommentDto commentDto) {
        return commentService.update(postId, id, commentDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "postId") Long postId, @PathVariable(name = "id") Long id) {
        commentService.delete(postId, id);
    }
}
