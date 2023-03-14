package by.yurkova.dreap.chip.service;

import by.yurkova.dreap.chip.dto.AnimalDto;
import by.yurkova.dreap.chip.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private AnimalRepository animalRepository;

    public AnimalDto findById(Long animalId) {

        return animalRepository.findById(animalId);
    }
}
