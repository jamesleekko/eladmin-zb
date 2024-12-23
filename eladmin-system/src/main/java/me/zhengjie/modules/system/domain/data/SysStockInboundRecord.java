package me.zhengjie.modules.system.domain.data;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sys_stock_inbound_records")
public class SysStockInboundRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inboundId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private SysStockItem item;

    private Integer quantity;
    private LocalDateTime inboundTime;
    private String operator;
    private String note;
    private Long deptId;
}
