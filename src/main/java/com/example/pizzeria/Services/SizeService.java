package com.example.pizzeria.Services;

import com.example.pizzeria.Entities.Size;

public interface SizeService {

    Size addSize(Size size);
    void deleteSize(Long id);
}
