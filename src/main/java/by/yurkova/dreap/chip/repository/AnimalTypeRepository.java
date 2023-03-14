package by.yurkova.dreap.chip.repository;

import by.yurkova.dreap.chip.dto.AnimalTypeDto;
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
public class AnimalTypeRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static final String FIND_ANIMAL_TYPE_BY_ID = """
        SELECT id, type
        FROM animal_types
        WHERE id = :id
    """;

    public static final String SAVE = """
        INSERT INTO animal_types (type)
        VALUES (:type)
    """;

    public static final String UPDATE = """
        UPDATE animal_types
        SET type = :type
        WHERE id = :id
    """;

    public static final String DELETE = """
        DELETE FROM animal_types
        WHERE id = :id
    """;

    public static final String EXISTS_ANIMAL_TYPE = """
        SELECT EXISTS (SELECT * FROM animal_types WHERE type = :type);
    """;

    public static final String HAS_ANIMAL = """
        SELECT EXISTS (SELECT * FROM Animal WHERE id = :id);
    """;

    public AnimalTypeDto findById(Long typeId) {
        var parameters = new MapSqlParameterSource("id", typeId);
        try {
            return namedParameterJdbcTemplate.queryForObject(FIND_ANIMAL_TYPE_BY_ID, parameters, this::buildAnimalTypeDto);
        } catch (EmptyResultDataAccessException e) {
            var message = "Animal with the following typeId = '%s' does not found";
            throw new DreapChipException(format(message, typeId), HttpStatus.NOT_FOUND);
        }
    }

    private AnimalTypeDto buildAnimalTypeDto(ResultSet rs, int rowNum) throws SQLException {
        return new AnimalTypeDto()
                .setId(rs.getLong("id"))
                .setType(rs.getString("type"));
    }

    public AnimalTypeDto save(AnimalTypeDto accountDto) {
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SAVE, fillParameters(accountDto), keyHolder, new String[]{"id"});
        return accountDto.setId((Long) keyHolder.getKey());
    }

    private MapSqlParameterSource fillParameters(AnimalTypeDto accountDto) {
        return new MapSqlParameterSource()
                .addValue("type", accountDto.getType());
    }

    public AnimalTypeDto update(AnimalTypeDto animalTypeDto) {
        var updatedRow = namedParameterJdbcTemplate.update(UPDATE, fillParameters(animalTypeDto));
        if (updatedRow != 1) {
            var message = "Animal with the following type = '%s' does not found";
            throw new DreapChipException(format(message, animalTypeDto.getType()), HttpStatus.FORBIDDEN);
        }
        return animalTypeDto;
    }

    public void delete(Long typeId) {
        var parameters = new MapSqlParameterSource("id", typeId);
        var updatedRow = namedParameterJdbcTemplate.update(DELETE, parameters);
        if (updatedRow != 1) {
            var message = "Animal type with the following id = '%s' does not found";
            throw new DreapChipException(format(message, typeId), HttpStatus.FORBIDDEN);
        }
    }

    public Boolean exists(String type) {
        var parameters = new MapSqlParameterSource("type", type);
        return namedParameterJdbcTemplate.queryForObject(EXISTS_ANIMAL_TYPE, parameters, Boolean.class);
    }

    public Boolean hasAnimal(Long typeId) {
        var parameters = new MapSqlParameterSource("id", typeId);
        return namedParameterJdbcTemplate.queryForObject(HAS_ANIMAL, parameters, Boolean.class);
    }
}
