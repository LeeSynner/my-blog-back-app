package ru.yandex.practicum.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.service.ImagesService;

@RestController
@RequestMapping("/api/posts/{postId}/image")
public class ImagesController {

    ImagesService imagesService;

    public ImagesController(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @GetMapping
    public ResponseEntity<Resource> downloadImage(@PathVariable(name = "postId") Long postId) {
        Resource file = imagesService.download(postId);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }

    @PutMapping
    public void updateImage(@PathVariable(name = "postId") Long postId, @RequestParam("image") MultipartFile image) {
        imagesService.upload(postId, image);
    }
}
