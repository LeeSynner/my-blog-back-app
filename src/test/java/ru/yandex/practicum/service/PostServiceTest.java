package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.dto.PostsDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostRepository postRepository;
    private PostService postService;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        postService = new PostService(postRepository);
    }

    @Test
    void findAll_shouldReturnCorrectDto() {
        when(postRepository.findAll("test", 1, 3)).thenReturn(List.of(new Post(), new Post(), new Post()));
        when(postRepository.countAll("test")).thenReturn(7);

        PostsDto dto = postService.findAll("test", 1, 3);

        assertEquals(3, dto.getPosts().size());
        assertTrue(dto.isHasNext());
        assertFalse(dto.isHasPrev());
        assertEquals(3, dto.getLastPage());
        verify(postRepository).findAll("test", 1, 3);
        verify(postRepository).countAll("test");
    }

    @Test
    void findById_shouldReturnPost() {
        Post post = new Post();
        when(postRepository.findById(1L)).thenReturn(post);

        Post result = postService.findById(1L);

        assertSame(post, result);
        verify(postRepository).findById(1L);
    }

    @Test
    void save_shouldDelegateToRepository() {
        Post post = new Post();
        when(postRepository.save(post)).thenReturn(post);

        Post result = postService.save(post);

        assertSame(post, result);
        verify(postRepository).save(post);
    }

    @Test
    void deleteById_shouldCallRepository() {
        postService.deleteById(5L);
        verify(postRepository).deleteById(5L);
    }

    @Test
    void update_shouldDelegateToRepository() {
        Post post = new Post();
        when(postRepository.update(1L, post)).thenReturn(post);

        Post result = postService.update(1L, post);

        assertSame(post, result);
        verify(postRepository).update(1L, post);
    }

    @Test
    void like_shouldCallRepositoryLike() {
        postService.like(10L);
        verify(postRepository).like(10L);
    }
}
