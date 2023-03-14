package by.yurkova.dreap.chip.web;

import by.yurkova.dreap.chip.dto.AnimalDto;
import by.yurkova.dreap.chip.dto.AnimalTypeUpdateDto;
import by.yurkova.dreap.chip.service.AnimalService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("/animals/{animalId}")
    public AnimalDto findById(
        @PathVariable Long animalId
    ) {
        return animalService.findById(animalId);
    }

    @GetMapping("/animals/search")
    public AnimalDto search(
        @RequestParam(required = false) LocalDateTime startDateTime,
        @RequestParam(required = false) LocalDateTime endDateTime,
        @RequestParam(required = false) Integer chipperId,
        @RequestParam(required = false) Long chippingLocationId,
        @RequestParam(required = false) String lifeStatus,
        @RequestParam(required = false) String gender,
        @RequestParam(required = false, defaultValue = "0") int from,
        @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return null;
    }

    @PostMapping("/animals")
    public void registration(
        @RequestBody AnimalDto animalDto
    ) {

    }

    @PutMapping("/animals/{animalId}")
    public AnimalDto update(
        @PathVariable Long animalId,
        @RequestBody AnimalDto animalDto
    ) {
        return null;
    }

    @DeleteMapping("/animals/{animalId}")
    public void delete(
        @PathVariable Long animalId
    ) {
    }


    @PostMapping("/animals/{animalId}/types/{typeId}")
    public AnimalDto addAnimalType(
        @PathVariable Long animalId,
        @PathVariable Long typeId
    ) {
        return null;
    }

    @PutMapping("/animals/{animalId}/types")
    public AnimalDto updateAnimalType(
        @PathVariable Long animalId,
        @RequestBody AnimalTypeUpdateDto animalTypeUpdateDto
    ) {
        return null;
    }

    @DeleteMapping("/animals/{animalId}/types/{typeId}")
    public void deleteAnimalType(
        @PathVariable Long animalId,
        @PathVariable Long typeId
    ) {

    }
}
