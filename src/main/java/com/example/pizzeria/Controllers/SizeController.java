package com.example.pizzeria.Controllers;

import com.example.pizzeria.Entities.Size;
import com.example.pizzeria.Services.SizeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/size")
@CrossOrigin(origins = "*")
public class SizeController {

    private final SizeService sizeService;

    @PostMapping("/add")
    public Size addSize(@RequestBody Size size) {
        return sizeService.addSize(size);
    }

    @GetMapping("/{id}")
    public Size getSizeById(@PathVariable("id") Long id){
        return sizeService.getSizeById(id);
    }
}
