package by.yurkova.dreap.chip.service;

import by.yurkova.dreap.chip.dto.LocationDto;
import by.yurkova.dreap.chip.exception.DreapChipException;
import by.yurkova.dreap.chip.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationDto findById(Long pointId) {
        if (pointId == null || pointId <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        return locationRepository.findById(pointId);
    }


    public LocationDto save(LocationDto locationDto) {
        checkLocationPoint(locationDto);
        validateLocationDto(locationDto);
        return locationRepository.save(locationDto);
    }

    //я хз как назвать метод нормально
    private static void checkLocationPoint(LocationDto locationDto) {
        if (locationDto.getLongitude() == null || locationDto.getLongitude() < -90 || locationDto.getLongitude() > 90
                || locationDto.getLatitude() == null || locationDto.getLatitude() < -180 || locationDto.getLatitude() > 180
        ) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
    }

    private void validateLocationDto(LocationDto locationDto) {
        //это же не нужно уже получается

//        if (!hasText(locationDto.getFirstName()) || !hasText(locationDto.getLastName())
//                || !hasText(locationDto.getEmail()) || !hasText(locationDto.getPassword())
//        ) {
//            throw new DreapChipException(HttpStatus.BAD_REQUEST);
//        }
        if (locationRepository.exists(locationDto.getLatitude(), locationDto.getLongitude())) {
            var message = "Location has already existed with the following latitude = '%s' and longitude = '%s'";
            throw new DreapChipException
                    (format(message, locationDto.getLatitude(), locationDto.getLongitude()), HttpStatus.CONFLICT);
        }
    }

    public LocationDto update(LocationDto locationDto) {
        if (locationDto.getId() == null || locationDto.getId() < 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        checkLocationPoint(locationDto);
        validateLocationDto(locationDto);
        return locationRepository.update(locationDto);
    }

    public void delete(Long pointId) {
        if (pointId == null || pointId <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        if (locationRepository.hasAnimal(pointId)) {
            throw new DreapChipException("Location linked with animal", HttpStatus.BAD_REQUEST);
        }
        locationRepository.delete(pointId);
    }
}
