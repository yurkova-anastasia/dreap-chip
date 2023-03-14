package by.yurkova.dreap.chip.web;

import by.yurkova.dreap.chip.dto.AccountDto;
import by.yurkova.dreap.chip.dto.LocationDto;
import by.yurkova.dreap.chip.service.LocationService;
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
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/locations/{pointId}")
    public LocationDto findById(
        @PathVariable Long pointId
    ) {
        return locationService.findById(pointId);
    }

    @PostMapping("/locations")
    public LocationDto registration(
        @RequestBody LocationDto locationDto
    ) {
        return locationService.save(locationDto);
    }

    @PutMapping("/locations/{pointId}")
    public LocationDto update(
        @PathVariable Long pointId,
        @RequestBody LocationDto locationDto
    ) {
        return locationService.update(locationDto.setId(pointId));
    }

    @DeleteMapping("/locations/{pointId}")
    public void delete(
        @PathVariable Long pointId
    ) {
        locationService.delete(pointId);
    }
}
