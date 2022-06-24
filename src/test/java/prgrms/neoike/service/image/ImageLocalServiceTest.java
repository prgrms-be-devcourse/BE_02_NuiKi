package prgrms.neoike.service.image;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import prgrms.neoike.service.dto.sneakerimage.SneakerImageResponse;
import prgrms.neoike.service.dto.sneakerimage.SneakerImageUploadDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.deleteIfExists;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class ImageLocalServiceTest {

    @Autowired
    ImageLocalService imageLocalService;

    private static SneakerImageResponse imageResponse;
    private static MultipartFile multipartFile;

    @BeforeAll
    static void setup() throws IOException {
        String fullPath = System.getProperty("user.dir") + "/src/test/resources/test.PNG";
        multipartFile = getMockMultipartFile("test", "PNG", fullPath);
    }

    @AfterAll
    static void cleanup() throws IOException {
        List<String> paths = imageResponse.paths();

        for (String path : paths) {
            deleteIfExists(Path.of(System.getProperty("user.dir") + path));
        }
    }

    @Test
    @DisplayName("이미지 파일을 저장한다.")
    void testStoreImageToLocal() throws IOException {
        imageResponse = imageLocalService.upload(new SneakerImageUploadDto(List.of(multipartFile)));

        assertThat(imageResponse).isNotNull();
        assertAll(
            () -> imageResponse
                .paths()
                .forEach(
                    path -> assertThat(new File(System.getProperty("user.dir") + path)).isFile()
                )
            );
    }

    private static MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fis = new FileInputStream(new File(path));

        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fis);
    }
}