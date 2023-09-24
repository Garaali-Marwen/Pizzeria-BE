package com.example.pizzeria.Controllers;

import com.example.pizzeria.Entities.Config;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Services.ConfigService;
import com.example.pizzeria.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/config")
@CrossOrigin("*")
@AllArgsConstructor
public class ConfigController {

    private final ConfigService configService;
    private final ImageService imageService;


    @GetMapping("/all")
    public List<Config> getConfig() {
        return configService.getConfig();
    }

    @PutMapping("/update")
    public Config updateConfig(@RequestBody Config config) {
        return configService.updateConfig(config);
    }

    @PutMapping(value = "/update/image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Config updateConfigImage(@RequestPart("config") Config config,
                                    @RequestPart(name = "images", required = false) MultipartFile[] images) {
        try {
            List<Image> imageList = new ArrayList<>();
            if (images != null) {
                for (MultipartFile multipartFile : images) {
                    imageList.add(imageService.uploadFile(multipartFile));
                }
            }
            return configService.updateConfigImage(config, imageList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
