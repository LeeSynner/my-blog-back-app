package ru.yandex.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.configuration.TestConfig;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(TestConfig.class)
class PostsControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PostService postService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP ALL OBJECTS");
        jdbcTemplate.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
        jdbcTemplate.execute("RUNSCRIPT FROM 'classpath:data.sql'");

        PostsController controller = new PostsController(postService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetAllPosts() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("pageNumber", "1")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts.length()").value(3))
                .andExpect(jsonPath("$.posts[0].title").value("Название поста 1"));
    }

    @Test
    void testGetPostById() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Название поста 1"));
    }

    @Test
    void testGetPostById_NotFound() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddPost() throws Exception {
        Post newPost = new Post();
        newPost.setTitle("New post");
        newPost.setText("Some text");
        newPost.setTags(List.of("java"));

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New post"));

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Long.class);
        assertThat(count).isEqualTo(4);
    }

    @Test
    void testUpdatePost() throws Exception {
        Post updated = new Post(1L, "Updated", "Updated text", List.of("tag"), 0, 0);

        mockMvc.perform(put("/api/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));

        String title = jdbcTemplate.queryForObject("SELECT title FROM posts WHERE id = 1", String.class);
        assertThat(title).isEqualTo("Updated");
    }

    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 1))
                .andExpect(status().isOk());

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Long.class);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testLikePost() throws Exception {
        int before = jdbcTemplate.queryForObject("SELECT likes_count FROM posts WHERE id = 1", Integer.class);

        mockMvc.perform(post("/api/posts/{id}/likes", 1))
                .andExpect(status().isOk());

        int after = jdbcTemplate.queryForObject("SELECT likes_count FROM posts WHERE id = 1", Integer.class);
        assertThat(after).isEqualTo(before + 1);
    }
}
