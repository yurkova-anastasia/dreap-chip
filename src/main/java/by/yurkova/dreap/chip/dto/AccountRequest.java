package by.yurkova.dreap.chip.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountRequest {
    private String firstName;
    private String lastName;
    private String email;
    private int from;
    private int size;
}
