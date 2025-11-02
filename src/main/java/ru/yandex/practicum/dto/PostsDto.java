package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.model.Post;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsDto {
    private List<Post> posts;
    private boolean hasPrev;
    private boolean hasNext;
    private int lastPage;
}
