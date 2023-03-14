package by.yurkova.dreap.chip.web;

import by.yurkova.dreap.chip.dto.AnimalTypeDto;
import by.yurkova.dreap.chip.service.AnimalTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AnimalTypeController {

    private final AnimalTypeService animalTypeService;

    @GetMapping("/animals/types/{typeId}")
    public AnimalTypeDto findById(
        @PathVariable Long typeId
    ) {
        return animalTypeService.findById(typeId);
    }

    @PostMapping("/animals/types")
    public AnimalTypeDto save(
        @RequestBody AnimalTypeDto animalTypeDto
    ) {
        return animalTypeService.save(animalTypeDto);
    }

    @PutMapping("/animals/types/{typeId}")
    public AnimalTypeDto update(
        @PathVariable Long typeId,
        @RequestBody AnimalTypeDto animalTypeDto
    ) {
        return animalTypeService.update(animalTypeDto.setId(typeId));
    }

    @DeleteMapping("/animals/types/{typeId}")
    public void delete(
        @PathVariable Long typeId
    ) {
        animalTypeService.delete(typeId);
    }
}
