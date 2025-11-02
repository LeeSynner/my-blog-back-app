package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.PostsDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

@Service
public class PostService {

    PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostsDto findAll(String search, int pageNumber, int pageSize) {
        PostsDto postsDto = new PostsDto();
        postsDto.setPosts(postRepository.findAll(search, pageNumber, pageSize));
        int countAll = postRepository.countAll(search);
        int countOfPages = countAll / pageSize + (countAll % pageSize > 0 ? 1 : 0);

        postsDto.setHasNext(pageNumber < countOfPages);
        postsDto.setHasPrev(pageNumber > 1 && countOfPages > 1);
        postsDto.setLastPage(countOfPages);
        return postsDto;
    }

    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public Post update(Long id, Post post) {
        return postRepository.update(id, post);
    }

    public void like(Long id) {
        postRepository.like(id);
    }
}
