package ru.yandex.practicum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostsDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

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
    public ResponseEntity<PostDto> post(@PathVariable(name = "id") Long id) {
        PostDto post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public PostDto save(@RequestBody PostDto postDto) {
        return postService.save(postDto);
    }

    @PutMapping("/{id}")
    public PostDto update(@PathVariable(name = "id") Long id, @RequestBody PostDto postDto) {
        return postService.update(id, postDto);
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
