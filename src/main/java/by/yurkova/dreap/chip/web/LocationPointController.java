package by.yurkova.dreap.chip.web;

import by.yurkova.dreap.chip.dto.AccountDto;
import by.yurkova.dreap.chip.dto.AccountRequest;
import by.yurkova.dreap.chip.dto.LocationPointDto;
import by.yurkova.dreap.chip.dto.LocationPointRequest;
import by.yurkova.dreap.chip.service.LocationPointService;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LocationPointController {

    private final LocationPointService locationPointService;

    @GetMapping("/animals/{animalId}/locations")
    public List<LocationPointDto> findAnimalLocation(
            @PathVariable Long animalId,
            @RequestParam(required = false) LocalDateTime startDateTime,
            @RequestParam(required = false) LocalDateTime endDateTime,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        var locationPointRequest = new LocationPointRequest()
                .setAnimalId(animalId)
                .setStartDateTime(startDateTime)
                .setEndDateTime(endDateTime)
                .setFrom(from)
                .setSize(size);
        return locationPointService.search(locationPointRequest);
    }

    @PostMapping("/animals/{animalId}/locations/{pointId}")
    public LocationPointDto addAnimalType(
            @PathVariable Long animalId,
            @PathVariable Long pointId
    ) {
        return locationPointService.add(animalId, pointId);
    }

    @PutMapping("/animals/{animalId}/locations")
    public LocationPointDto update(
            @PathVariable Long animalId,
            @RequestBody LocationPointDto locationPointDto
    ) {
       return locationPointService.update(animalId, locationPointDto);
    }

    @DeleteMapping("/animals/{animalId}/locations/{visitedPointId}")
    public void delete(
            @PathVariable Long animalId,
            @PathVariable Long visitedPointId
    ) {
        locationPointService.delete(animalId, visitedPointId);
    }
}
