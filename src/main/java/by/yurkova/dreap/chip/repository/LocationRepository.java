package by.yurkova.dreap.chip.repository;

import by.yurkova.dreap.chip.dto.LocationDto;
import by.yurkova.dreap.chip.exception.DreapChipException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.format;

@Repository
@RequiredArgsConstructor
public class LocationRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static final String FIND_LOCATION_BY_ID = """
        SELECT id, latitude, longitude
        FROM locations
        WHERE id = :id
    """;

    public static final String SAVE = """
        INSERT INTO locations (latitude, longitude)
        VALUES (:latitude, :longitude)
    """;

    public static final String UPDATE = """
        UPDATE locations
        SET latitude = :latitude, longitude = :longitude
        WHERE id = :id
    """;

    public static final String DELETE = """
        DELETE FROM locations
        WHERE id = :id
    """;

    public static final String EXISTS_LOCATION = """
        SELECT EXISTS (SELECT * FROM locations WHERE latitude = :latitude AND longitude = :longitude);
    """;

    public static final String HAS_ANIMAL = """
        SELECT EXISTS (SELECT * FROM location_points WHERE location_point_id = :id);
    """;

    public LocationDto findById(Long pointId) {
        var parameters = new MapSqlParameterSource("id", pointId);
        try {
            return namedParameterJdbcTemplate.queryForObject(FIND_LOCATION_BY_ID, parameters, this::buildLocationDto);
        } catch (EmptyResultDataAccessException e) {
            var message = "Location with the following pointId = '%s' does not found";
            throw new DreapChipException(format(message, pointId), HttpStatus.NOT_FOUND);
        }
    }

    private LocationDto buildLocationDto(ResultSet rs, int rowNum) throws SQLException {
        return new LocationDto()
                .setId(rs.getLong("id"))
                .setLatitude(rs.getDouble("latitude"))
                .setLongitude(rs.getDouble("longitude"));
    }


    public LocationDto save(LocationDto locationDto) {
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SAVE, fillParameters(locationDto), keyHolder, new String[]{"id"});
        return locationDto.setId((Long) keyHolder.getKey());
    }

    private MapSqlParameterSource fillParameters(LocationDto locationDto) {
        return new MapSqlParameterSource()
                .addValue("latitude", locationDto.getLatitude())
                .addValue("longitude", locationDto.getLongitude());
    }

    public LocationDto update(LocationDto locationDto) {
        var updatedRow = namedParameterJdbcTemplate.update(UPDATE, fillParameters(locationDto).addValue("id", locationDto.getId()));
        if (updatedRow != 1) {
            var message = "Location with the following pointId = '%s' does not found";
            throw new DreapChipException(format(message, locationDto.getId()), HttpStatus.FORBIDDEN);
        }
        return locationDto;
    }

    public void delete(Long pointId) {
        var parameters = new MapSqlParameterSource("id", pointId);
        var updatedRow = namedParameterJdbcTemplate.update(DELETE, parameters);
        if (updatedRow != 1) {
            var message = "Location with the following pointId = '%s' does not found";
            throw new DreapChipException(format(message, pointId), HttpStatus.FORBIDDEN);
        }
    }

    public Boolean exists(Double latitude, Double longitude) {
        var parameters = new MapSqlParameterSource()
                .addValue("latitude", latitude)
                .addValue("longitude", longitude);
        return namedParameterJdbcTemplate.queryForObject(EXISTS_LOCATION, parameters, Boolean.class);
    }

    public Boolean hasAnimal(Long pointId) {
        var parameters = new MapSqlParameterSource("id", pointId);
        return namedParameterJdbcTemplate.queryForObject(HAS_ANIMAL, parameters, Boolean.class);
    }
}
