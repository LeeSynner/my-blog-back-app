package ru.yandex.practicum.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.repository.PostRepository;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImagesServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MultipartFile file;

    private ImagesService imagesService;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        tempDir = Files.createTempDirectory("test-images-");

        imagesService = new ImagesService(postRepository);

        setField(imagesService, "UPLOAD_DIR", tempDir.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .sorted((a, b) -> b.compareTo(a)) // сначала файлы, потом папки
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {}
                    });
        }
    }

    @Test
    void upload_ShouldSaveFileAndUpdateRepository() throws Exception {
        Long postId = 1L;
        String originalFilename = "image.png";
        when(file.getOriginalFilename()).thenReturn(originalFilename);
        when(postRepository.imageName(postId)).thenReturn(null);

        Path targetFile = tempDir.resolve("test_" + originalFilename);

        doAnswer(invocation -> {
            Files.createFile(targetFile);
            return null;
        }).when(file).transferTo(any(Path.class));

        imagesService.upload(postId, file);

        verify(file).transferTo(any(Path.class));
        verify(postRepository).updateImageName(eq(postId), anyString());
    }

    @Test
    void download_ShouldReturnNull_WhenNoImageName() {
        when(postRepository.imageName(1L)).thenReturn(null);

        Resource resource = imagesService.download(1L);

        assertNull(resource);
    }

    @Test
    void download_ShouldReturnFileSystemResource_WhenFileExists() throws IOException {
        Long postId = 1L;
        String imageName = "pic.png";
        when(postRepository.imageName(postId)).thenReturn(imageName);

        Path filePath = tempDir.resolve(imageName);
        Files.createFile(filePath);

        setField(imagesService, "UPLOAD_DIR", tempDir.toString());

        Resource resource = imagesService.download(postId);

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.getFile().getName().endsWith("pic.png"));
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
