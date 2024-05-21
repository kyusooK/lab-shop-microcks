package microcks.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import microcks.OrderApplication;
import microcks.domain.OrderPlaced;

@Entity
@Table(name = "Order_table")
@Data
//<<< DDD / Aggregate Root
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productId;

    private Integer qty;

    private String customerId;

    @PostPersist
    public void onPostPersist() {

        microcks.external.DecreaseStockCommand decreaseStockCommand = new microcks.external.DecreaseStockCommand();
        
        decreaseStockCommand.setQty(getQty());

        OrderApplication.applicationContext
            .getBean(microcks.external.InventoryService.class)
            .decreaseStock(Long.valueOf(getProductId()), decreaseStockCommand);
        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();
    }

    public static OrderRepository repository() {
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(
            OrderRepository.class
        );
        return orderRepository;
    }
}
//>>> DDD / Aggregate Root
