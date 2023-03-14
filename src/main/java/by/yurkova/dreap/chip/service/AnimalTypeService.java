package by.yurkova.dreap.chip.service;

import by.yurkova.dreap.chip.dto.AnimalTypeDto;
import by.yurkova.dreap.chip.exception.DreapChipException;
import by.yurkova.dreap.chip.repository.AnimalTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class AnimalTypeService {
    private final AnimalTypeRepository animalTypeRepository;
    public AnimalTypeDto findById(Long typeId) {
        if (typeId == null || typeId <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        return animalTypeRepository.findById(typeId);
    }

    public AnimalTypeDto save(AnimalTypeDto animalTypeDto) {
        validateAnimalTypeDto(animalTypeDto);
        return animalTypeRepository.save(animalTypeDto);
    }

    private void validateAnimalTypeDto(AnimalTypeDto animalTypeDto) {
        if (!hasText(animalTypeDto.getType())) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        if (animalTypeRepository.exists(animalTypeDto.getType())) {
            var message = "Animal has already existed with the following type = '%s'";
            throw new DreapChipException(format(message, animalTypeDto.getType()), HttpStatus.CONFLICT);
        }
    }

    public AnimalTypeDto update(AnimalTypeDto animalTypeDto) {
        validateAnimalTypeDto(animalTypeDto);
        return animalTypeRepository.update(animalTypeDto);
    }

    public void delete(Long typeId) {
        if (typeId == null || typeId <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        if (animalTypeRepository.hasAnimal(typeId)) {
            var message = "There are animals with typeId = '%s'";
            throw new DreapChipException(format(message, typeId), HttpStatus.BAD_REQUEST);
        }
        animalTypeRepository.delete(typeId);
    }
}
