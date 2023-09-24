package com.example.pizzeria.Services;

import com.example.pizzeria.Entities.Config;
import com.example.pizzeria.Entities.Image;

import java.util.List;

public interface ConfigService {

    List<Config> getConfig();
    Config updateConfig(Config config);
    Config updateConfigImage(Config config, List<Image> image);
}
