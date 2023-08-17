package com.example.pizzeria.Services;

import com.example.pizzeria.Entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    Image addImage(Image image);
    Image uploadFile(MultipartFile multipartFiles) throws IOException;
    void deleteImage(Long id);
}
