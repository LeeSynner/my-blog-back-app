package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll(String search, int pageNumber, int pageSize);

    Integer countAll(String search);

    Post findById(Long id);

    Post save(Post post);

    void deleteById(Long id);

    Post update(Long id, Post post);

    void like(Long id);

    String imageName(Long id);

    void updateImageName(Long id, String imageName);
}
