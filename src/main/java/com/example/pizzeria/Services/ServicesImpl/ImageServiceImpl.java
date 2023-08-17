package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Repositories.ImageRepository;
import com.example.pizzeria.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Image addImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image uploadFile(MultipartFile multipartFile) throws IOException {
        if (!Objects.equals(multipartFile.getOriginalFilename(), "")) {
            Image image = new Image(
                    multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(),
                    multipartFile.getBytes()
            );
            return addImage(image);
        }
        return null;
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
