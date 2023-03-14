package by.yurkova.dreap.chip.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LocationPointRequest {
    private Long animalId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int from;
    private int size;
}
