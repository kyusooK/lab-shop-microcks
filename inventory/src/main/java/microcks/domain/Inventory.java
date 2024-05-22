package microcks.domain;

import javax.persistence.*;
import lombok.Data;
import microcks.InventoryApplication;

@Entity
@Table(name = "Inventory_table")
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer stock;

    @PostPersist
    public void onPostPersist() {}

    public static InventoryRepository repository() {
        InventoryRepository inventoryRepository = InventoryApplication.applicationContext.getBean(
            InventoryRepository.class
        );
        return inventoryRepository;
    }

    public void decreaseStock(DecreaseStockCommand decreaseStockCommand) {
        setStock(getStock() - decreaseStockCommand.getQty());
        
        StockDecreased stockDecreased = new StockDecreased(this);
        stockDecreased.publishAfterCommit();
    }

}
