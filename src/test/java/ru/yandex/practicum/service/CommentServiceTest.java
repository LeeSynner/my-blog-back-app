package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.dto.CommentDto;
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
        List<CommentDto> expected = List.of(new CommentDto(), new CommentDto());

        when(commentRepository.findAll(postId)).thenReturn(expected.stream().map(CommentService::toModel).toList());

        List<CommentDto> result = commentService.findAll(postId);

        assertEquals(expected, result);
        verify(commentRepository).findAll(postId);
    }

    @Test
    void findById_shouldReturnCommentFromRepository() {
        Long postId = 1L;
        Long id = 2L;
        CommentDto expected = new CommentDto();
        Comment comment = CommentService.toModel(expected);
        when(commentRepository.findById(postId, id)).thenReturn(comment);

        CommentDto result = commentService.findById(postId, id);

        assertEquals(expected, result);
        verify(commentRepository).findById(postId, id);
    }

    @Test
    void save_shouldDelegateToRepository() {
        Long postId = 1L;
        CommentDto commentDto = new CommentDto();
        CommentDto saved = new CommentDto();
        Comment comment = CommentService.toModel(commentDto);
        Comment savedComment = CommentService.toModel(saved);

        when(commentRepository.save(postId, comment))
                .thenReturn(savedComment);

        CommentDto result = commentService.save(postId, commentDto);

        assertEquals(saved, result);
        verify(commentRepository).save(postId, comment);
    }

    @Test
    void update_shouldCallRepositoryUpdate() {
        Long postId = 1L;
        Long id = 10L;
        CommentDto commentDto = new CommentDto();
        CommentDto updatedDto = new CommentDto();

        Comment comment = CommentService.toModel(commentDto);
        Comment updated = CommentService.toModel(updatedDto);

        when(commentRepository.update(postId, id, comment)).thenReturn(updated);

        CommentDto result = commentService.update(postId, id, commentDto);

        assertEquals(updatedDto, result);
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
