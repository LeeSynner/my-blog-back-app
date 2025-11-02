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


INSERT INTO posts(title, text, tags, likes_count)
    VALUES ('Название поста 1', 'Текст 1 поста в формате Markdown...', '#tag_1#tag_2#', 5),
           ('Название поста 2', 'Текст 2 поста в формате Markdown...', '', 1);

INSERT INTO comments(post_id, text)
    VALUES (1, 'Комментарий 1 для поста 1'),
           (2, 'Комментарий 1 для поста 2'),
           (2, 'Комментарий 2 для поста 2'),
           (2, 'Комментарий 3 для поста 2'),
           (2, 'Комментарий 4 для поста 2'),
           (2, 'Комментарий 5 для поста 2');
