package ru.yandex.practicum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.impl.JdbcNativeCommentRepository;
import ru.yandex.practicum.repository.impl.JdbcNativePostRepository;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

import javax.sql.DataSource;

@Configuration
public class TestConfig {

    @Bean
    public DataSource dataSource() {
        var ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PostRepository postRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcNativePostRepository(jdbcTemplate);
    }

    @Bean
    public PostService postService(PostRepository postRepository) {
        return new PostService(postRepository);
    }

    @Bean
    public CommentRepository commentRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcNativeCommentRepository(jdbcTemplate);
    }

    @Bean
    public CommentService commentService(CommentRepository repository) {
        return new CommentService(repository);
    }
}
