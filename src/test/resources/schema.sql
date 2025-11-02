CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT primary key,
    title VARCHAR(256) not null,
    text TEXT not null,
    tags varchar(500) NOT NULL DEFAULT '',
    likes_count INT DEFAULT 0,
    image_name VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    text TEXT NOT NULL,

    CONSTRAINT fk_post
            FOREIGN KEY (post_id)
            REFERENCES posts(id)
            ON DELETE CASCADE
);
