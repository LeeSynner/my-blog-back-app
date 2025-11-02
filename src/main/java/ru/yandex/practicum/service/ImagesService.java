package ru.yandex.practicum.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.repository.PostRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImagesService {

    public static final String UPLOAD_DIR = "uploads/images/";

    PostRepository postRepository;

    public ImagesService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void upload(Long postId, MultipartFile file) {
        try {
            Path uploadDir = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String imageName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(imageName);
            file.transferTo(filePath);

            deleteOldImage(postId, uploadDir);

            postRepository.updateImageName(postId, imageName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void deleteOldImage(Long postId, Path uploadDir) throws IOException {
        String imageName = postRepository.imageName(postId);

        if (imageName != null && !imageName.isEmpty()) {
            Path filePath = uploadDir.resolve(imageName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }
    }

    public Resource download(Long postId) {
        String imageName = postRepository.imageName(postId);
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }
        Path filePath = Paths.get(UPLOAD_DIR).resolve(imageName).normalize();
        if (Files.exists(filePath)) {
            return new FileSystemResource(filePath);
        }
        return null;
    }
}
