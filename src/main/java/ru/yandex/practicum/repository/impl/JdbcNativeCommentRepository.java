package ru.yandex.practicum.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findAll(Long postId) {
        String sql = """
                SELECT id, text, post_id
                FROM comments
                WHERE post_id = ?
                ORDER BY id;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Comment(
                rs.getLong("id"),
                rs.getString("text"),
                rs.getLong("post_id")
        ), postId);
    }

    @Override
    public Comment findById(Long postId, Long id) {
        String sql = """
                SELECT id, text, post_id
                FROM comments
                WHERE post_id = ? AND id = ?;
                """;
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Comment comment = new Comment();
                comment.setId(rs.getLong("id"));
                comment.setText(rs.getString("text"));
                comment.setPostId(rs.getLong("post_id"));
                return comment;
            }, postId, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Comment save(Long postId, Comment comment) {
        String sql = """
                INSERT INTO comments(text, post_id)
                VALUES (?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getText());
            ps.setLong(2, comment.getPostId());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated comment ID");
        }
        Long id = key.longValue();
        return findById(postId, id);
    }

    @Override
    public Comment update(Long postId, Long id, Comment comment) {
        String sql = """
                UPDATE comments
                SET text = ?
                WHERE post_id = ? AND id = ?;
                """;

        jdbcTemplate.update(sql, comment.getText(), postId, id);
        return findById(postId, id);
    }

    @Override
    public void deleteById(Long postId, Long id) {
        String sql = """
                DELETE FROM comments
                WHERE post_id = ? AND id = ?;
                """;
        jdbcTemplate.update(sql, postId, id);
    }
}
