package ru.yandex.practicum.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll(String search, int pageNumber, int pageSize) {
        var filter = buildFilter(search);
        String sql = """
                SELECT id, title, text, tags, likes_count,
                    (SELECT COUNT(*) FROM  comments as c WHERE c.post_id = p.id) as comments_count
                FROM posts as p
                WHERE 1=1
                """ + filter.sql +
                " ORDER BY id ASC LIMIT ? OFFSET ?;";

        List<Object> params = new ArrayList<>(filter.params);
        params.add(pageSize);
        params.add((pageNumber - 1) * pageSize);

        System.out.println(sql);
        System.out.println(params);
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Post(
                rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        Arrays.stream(rs.getString("tags").split("#"))
                                .filter(s -> !s.isEmpty())
                                .toList(),
                        rs.getInt("likes_count"),
                        rs.getInt("comments_count")),
                params.toArray()
        );
    }

    @Override
    public Integer countAll(String search) {
        var filter = buildFilter(search);
        String sql = "SELECT COUNT(*) FROM posts WHERE 1=1" + filter.sql;
        return jdbcTemplate.queryForObject(sql, Integer.class, filter.params.toArray());
    }

    private record FilterResult(String sql, List<Object> params) {}

    private FilterResult buildFilter(String search) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        StringBuilder substring = new StringBuilder("%");
        String[] words = search != null ? search.split(" ") : new String[0];

        for (String word : words) {
            if (word == null || word.isBlank()) continue;
            if (word.startsWith("#")) {
                if (word.length() == 1) continue;
                sql.append(" AND tags LIKE ?");
                params.add("%" + word + "#%");
            } else {
                if (substring.length() > 1) substring.append(" ");
                substring.append(word.trim());
            }
        }
        substring.append("%");
        if (substring.length() > 2) {
            sql.append(" AND title LIKE ?");
            params.add(substring.toString());
        }

        return new FilterResult(sql.toString(), params);
    }

    @Override
    public Post findById(Long id) {
        String sql = """
                SELECT id, title, text, tags, likes_count,
                    (SELECT COUNT(*) FROM  comments as c WHERE c.post_id = p.id) as comments_count
                FROM posts as p
                WHERE p.id = ?;
                """;
        try {

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Post post = new Post();
                post.setId(rs.getLong("id"));
                post.setTitle(rs.getString("title"));
                post.setText(rs.getString("text"));
                post.setTags(Arrays.stream(rs.getString("tags").split("#"))
                        .filter(s -> !s.isEmpty())
                        .toList());
                post.setLikesCount(rs.getInt("likes_count"));
                post.setCommentsCount(rs.getInt("comments_count"));
                return post;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    };

    @Override
    public Post save(Post post) {
        String sql = """
                INSERT INTO posts(title, text, tags, likes_count) values(?, ?, ?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (post.getTags() == null) {
            post.setTags(List.of());
        }
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setString(3, "#" + String.join("#", post.getTags()) + "#");
            ps.setInt(4, 0);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated post ID");
        }
        Long id = key.longValue();
        return findById(id);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
    }

    @Override
    public Post update(Long id, Post post) {
        String sql = """
                UPDATE posts
                SET title = ?, text = ?, tags = ?
                WHERE id = ?;
                """;

        jdbcTemplate.update(sql, post.getTitle(), post.getText(), "#" + String.join("#", post.getTags()) + "#", id);
        return findById(id);
    }

    @Override
    synchronized public void like(Long id) {
        Post post = findById(id);
        String sql = """
                UPDATE posts
                SET likes_count = ?
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, post.getLikesCount() + 1, id);
    }

    @Override
    public String imageName(Long id) {
        String sql = """
                SELECT image_name
                FROM posts
                WHERE id = ?;
                """;
        try {
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateImageName(Long id, String imageName) {
        String sql = """
                UPDATE posts
                SET image_name = ?
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, imageName, id);
    }
}
