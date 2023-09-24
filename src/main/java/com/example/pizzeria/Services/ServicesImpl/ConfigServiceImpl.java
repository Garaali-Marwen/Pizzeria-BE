package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.Entities.Config;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Repositories.ConfigRepository;
import com.example.pizzeria.Services.ConfigService;
import com.example.pizzeria.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepository;
    private final ImageService imageService;


    @Override
    public List<Config> getConfig() {
        return configRepository.findAll();
    }

    @Override
    public Config updateConfig(Config config) {
        Config configUpdate = configRepository.findById(config.getId()).orElseThrow(() -> new NoSuchElementException("notFound"));
        configUpdate.setContactEmail(config.getContactEmail());
        configUpdate.setContactPhone(config.getContactPhone());
        configUpdate.setDeliveryPhone(config.getDeliveryPhone());
        configUpdate.setCarouselImages(config.getCarouselImages());
        return configRepository.save(configUpdate);
    }


    @Override
    public Config updateConfigImage(Config config, List<Image> images) {
        Config configUpdate = configRepository.findById(config.getId()).orElseThrow(() -> new NoSuchElementException("notFound"));
        Set<Long> newImageIds = config.getCarouselImages().stream()
                .map(Image::getId)
                .collect(Collectors.toSet());
        List<Image> imagesToDelete = new ArrayList<>();
        for (Image oldImage : configUpdate.getCarouselImages()) {
            if (!newImageIds.contains(oldImage.getId())) {
                imagesToDelete.add(oldImage);
            }
        }


        if (!images.isEmpty())
            configUpdate.getCarouselImages().addAll(images);

        Config configUpdated = configRepository.save(configUpdate);

        if (!imagesToDelete.isEmpty())
            for (Image image : imagesToDelete) {
                configUpdated.getCarouselImages().remove(image);
                configRepository.save(configUpdate);
                imageService.deleteImage(image.getId());
            }

        return configUpdated;
    }

}
