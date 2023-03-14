package by.yurkova.dreap.chip.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimalDto {

    private Long id;
    private List<Long> animalTypes;
    private Float weight;
    private Float length;
    private Float height;
    //создать Enum
    private String gender;
    //создать Enum
    private String lifeStatus;
    private LocalDateTime chippingDateTime;
    private Integer chipperId;
    private Long chippingLocationId;
    private List<LocationDto> visitedLocations;
    private LocalDateTime deathDateTime;
}
