package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.Entities.Size;
import com.example.pizzeria.Repositories.SizeRepository;
import com.example.pizzeria.Services.SizeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;
    @Override
    public Size addSize(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public void deleteSize(Long id) {
        sizeRepository.deleteById(id);
    }
}
