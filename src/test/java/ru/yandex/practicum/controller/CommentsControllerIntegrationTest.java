package ru.yandex.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.configuration.TestConfig;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class) // подключает Spring TestContext Framework
@ContextConfiguration(classes = TestConfig.class)
public class CommentsControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        CommentsController controller = new CommentsController(commentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        jdbcTemplate.execute("DROP ALL OBJECTS");
        jdbcTemplate.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
        jdbcTemplate.execute("RUNSCRIPT FROM 'classpath:data.sql'");
    }

    @Test
    void testGetAllComments() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}/comments", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].text").value("Комментарий 1 для поста 1"));
    }

    @Test
    void testGetCommentById() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}/comments/{id}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Комментарий 1 для поста 1"));
    }

    @Test
    void testCreateComment() throws Exception {
        Comment newComment = new Comment();
        newComment.setText("Новый коммент");
        newComment.setPostId(1L);

        mockMvc.perform(post("/api/posts/{postId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Новый коммент"));
    }

    @Test
    void testUpdateComment() throws Exception {
        Comment updated = new Comment(1L, "Обновленный текст", 1L);

        mockMvc.perform(put("/api/posts/{postId}/comments/{id}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Обновленный текст"));
    }

    @Test
    void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/posts/{postId}/comments/{id}", 1L, 1L))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/posts/{postId}/comments/{id}", 1L, 1L))
                .andExpect(status().isNotFound());
    }
}
