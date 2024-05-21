package microcks.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class DecreaseStockCommand {

    private Long id;
    private Integer qty;
}
