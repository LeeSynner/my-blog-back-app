package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostsDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class PostService {

    PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostsDto findAll(String search, int pageNumber, int pageSize) {
        PostsDto postsDto = new PostsDto();
        List<PostDto> postDtos = postRepository.findAll(search, pageNumber, pageSize).stream()
                .map(PostService::toDto)
                .toList();
        postsDto.setPosts(postDtos);
        int countAll = postRepository.countAll(search);
        int countOfPages = countAll / pageSize + (countAll % pageSize > 0 ? 1 : 0);

        postsDto.setHasNext(pageNumber < countOfPages);
        postsDto.setHasPrev(pageNumber > 1 && countOfPages > 1);
        postsDto.setLastPage(countOfPages);
        return postsDto;
    }

    public PostDto findById(Long id) {
        Post post = postRepository.findById(id);
        return toDto(post);
    }

    public PostDto save(PostDto postDto) {
        Post post = postRepository.save(toModel(postDto));
        return toDto(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public PostDto update(Long id, PostDto postDto) {
        Post post = postRepository.update(id, toModel(postDto));
        return toDto(post);
    }

    public void like(Long id) {
        postRepository.like(id);
    }

    public static PostDto toDto(Post post) {
        if (post == null) return null;
        List<String> tags = null;
        if (post.getTags() != null) {
            tags = Arrays.stream(post.getTags().split("#"))
                    .filter(s -> !s.isEmpty())
                    .toList();
        }
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getText())
                .tags(tags)
                .likesCount(post.getLikesCount())
                .commentsCount(post.getCommentsCount())
                .build();
    }

    public static Post toModel(PostDto postDto) {
        if (postDto == null) return null;
        String tags = null;
        if (postDto.getTags() != null) {
            tags = "#" + String.join("#", postDto.getTags()) + "#";
        }
        return Post.builder()
                .id(postDto.getId())
                .title(postDto.getTitle())
                .text(postDto.getText())
                .tags(tags)
                .likesCount(postDto.getLikesCount())
                .commentsCount(postDto.getCommentsCount())
                .build();
    }
}
