package by.yurkova.dreap.chip.repository;


import by.yurkova.dreap.chip.dto.AccountDto;
import by.yurkova.dreap.chip.dto.LocationPointDto;
import by.yurkova.dreap.chip.dto.LocationPointRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import by.yurkova.dreap.chip.exception.DreapChipException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import static java.lang.String.format;

@Repository
@RequiredArgsConstructor
public class LocationPointRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static final String FIND_LOCATION_POINT = """
        SELECT id, date_time_of_visit_location_point, location_point_id
        FROM location_points
        WHERE (:startDateTime IS NULL OR date_time_of_visit_location_point >= :startDateTime)
            AND (:endDateTime IS NULL OR date_time_of_visit_location_point < :endDateTime)
        LIMIT :size
        OFFSET :from
    """;

    public static final String SAVE = """
        INSERT INTO location_points (date_time_of_visit_location_point, location_point_id, animal_id)
        VALUES (:dateTimeOfVisitLocationPoint, :locationPointId, :animalId)
    """;

    public static final String UPDATE = """
        UPDATE location_points
        SET date_time_of_visit_location_point = :dateTimeOfVisitLocationPoint, animal_id = :animalId
        WHERE id = :id
    """;

    public static final String DELETE = """
        DELETE FROM location_points
        WHERE id = :id
    """;


    public List<LocationPointDto> search(LocationPointRequest locationPointRequest) {
        var parameters = new MapSqlParameterSource()
                .addValue("startDateTime", locationPointRequest.getStartDateTime())
                .addValue("endDateTime", locationPointRequest.getEndDateTime())
                .addValue("from", locationPointRequest.getFrom())
                .addValue("size", locationPointRequest.getSize());
        return namedParameterJdbcTemplate.query(FIND_LOCATION_POINT, parameters, this::buildLocationPointDto);
    }

    private LocationPointDto buildLocationPointDto(ResultSet rs, int rowNum) throws SQLException {
        return new LocationPointDto()
                .setId(rs.getLong("id"))
                .setDateTimeOfVisitLocationPoint(rs.getTimestamp("date_time_of_visit_location_point").toLocalDateTime())
                .setLocationPointId(rs.getLong("location_point_id"));
    }


    public LocationPointDto add(Long animalId, Long pointId) {
        LocationPointDto locationPointDto = new LocationPointDto();
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SAVE, fillParameters(animalId, pointId), keyHolder, new String[]{"id"});
        return locationPointDto.setId((Long) keyHolder.getKey());
    }

    private MapSqlParameterSource fillParameters(Long animalId, Long pointId) {
        return new MapSqlParameterSource()
                .addValue("animalId", animalId)
                .addValue("dateTimeOfVisitLocationPoint", LocalDateTime.now())
                .addValue("locationPointId", pointId);

    }

    public LocationPointDto update(Long animalId, LocationPointDto locationPointDto) {
        var updatedRow = namedParameterJdbcTemplate.update(UPDATE, fillParameters(animalId, locationPointDto));
        if (updatedRow != 1) {
            var message = "Point with the following locationPointId = '%s' does not found";
            throw new DreapChipException(format(message, locationPointDto.getLocationPointId()), HttpStatus.FORBIDDEN);
        }
        return locationPointDto;
    }

    private MapSqlParameterSource fillParameters(Long animalId, LocationPointDto locationPointDto) {
        return new MapSqlParameterSource()
                .addValue("animalId", animalId)
                .addValue("dateTimeOfVisitLocationPoint", LocalDateTime.now())
                .addValue("id", locationPointDto.getId());

    }

    public void delete(Long animalId, Long visitedPointId) {
        var parameters = new MapSqlParameterSource("id", visitedPointId);
        var updatedRow = namedParameterJdbcTemplate.update(DELETE, parameters);
        if (updatedRow != 1) {
            var message = "Animal with the following animalId = '%s' does not found";
            throw new DreapChipException(format(message, animalId), HttpStatus.FORBIDDEN);
        }
    }

}
