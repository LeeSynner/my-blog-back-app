package ru.yandex.practicum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PostsDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    PostService postService;

    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public PostsDto posts(@RequestParam(required = false, defaultValue = "", name = "search") String search, @RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize) {
        return postService.findAll(search, pageNumber, pageSize);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Post> post(@PathVariable(name = "id") Long id) {
        Post post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public Post save(@RequestBody Post post) {
        return postService.save(post);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable(name = "id") Long id, @RequestBody Post post) {
        return postService.update(id, post);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        postService.deleteById(id);
    }

    @PostMapping("{id}/likes")
    public void like(@PathVariable(name = "id") Long id) {
        postService.like(id);
    }
}
