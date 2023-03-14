package by.yurkova.dreap.chip.service;

import by.yurkova.dreap.chip.dto.LocationPointDto;
import by.yurkova.dreap.chip.dto.LocationPointRequest;
import by.yurkova.dreap.chip.exception.DreapChipException;
import by.yurkova.dreap.chip.repository.LocationPointRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationPointService {
    private final LocationPointRepository locationPointRepository;

    public List<LocationPointDto> search(LocationPointRequest locationPointRequest) {
        if (locationPointRequest.getFrom() < 0 || locationPointRequest.getSize() <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        return locationPointRepository.search(locationPointRequest);
    }

    public LocationPointDto add(Long animalId, Long pointId) {
        if (animalId == null || animalId <= 0 || pointId == null || pointId <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        return locationPointRepository.add(animalId, pointId);
    }

    public LocationPointDto update(Long animalId, LocationPointDto locationPointDto) {
        return locationPointRepository.update(animalId, locationPointDto);
    }

    public void delete(Long animalId, Long visitedPointId) {
        if (animalId == null || animalId <= 0
            || visitedPointId == null || visitedPointId <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        locationPointRepository.delete(animalId, visitedPointId);
    }
}
