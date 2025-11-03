package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.repository.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    private CommentRepository commentRepository;
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        commentService = new CommentService(commentRepository);
    }

    @Test
    void findAll_shouldReturnCommentsFromRepository() {
        Long postId = 1L;
        List<Comment> expected = List.of(new Comment(), new Comment());

        when(commentRepository.findAll(postId)).thenReturn(expected);

        List<Comment> result = commentService.findAll(postId);

        assertEquals(expected, result);
        verify(commentRepository).findAll(postId);
    }

    @Test
    void findById_shouldReturnCommentFromRepository() {
        Long postId = 1L;
        Long id = 2L;
        Comment expected = new Comment();

        when(commentRepository.findById(postId, id)).thenReturn(expected);

        Comment result = commentService.findById(postId, id);

        assertSame(expected, result);
        verify(commentRepository).findById(postId, id);
    }

    @Test
    void save_shouldDelegateToRepository() {
        Long postId = 1L;
        Comment comment = new Comment();
        Comment saved = new Comment();

        when(commentRepository.save(postId, comment)).thenReturn(saved);

        Comment result = commentService.save(postId, comment);

        assertEquals(saved, result);
        verify(commentRepository).save(postId, comment);
    }

    @Test
    void update_shouldCallRepositoryUpdate() {
        Long postId = 1L;
        Long id = 10L;
        Comment comment = new Comment();
        Comment updated = new Comment();

        when(commentRepository.update(postId, id, comment)).thenReturn(updated);

        Comment result = commentService.update(postId, id, comment);

        assertSame(updated, result);
        verify(commentRepository).update(postId, id, comment);
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        Long postId = 1L;
        Long id = 5L;

        commentService.delete(postId, id);

        verify(commentRepository).deleteById(postId, id);
    }
}
