package by.yurkova.dreap.chip.repository;

import by.yurkova.dreap.chip.dto.AnimalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnimalRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public AnimalDto findById(Long animalId) {
        return null;
    }
}
