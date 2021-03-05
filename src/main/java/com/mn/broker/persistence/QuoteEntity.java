package com.mn.broker.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quotes", schema = "mn")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal bid;
    private BigDecimal ask;

    @Column(name = "last_price")
    private BigDecimal lastPrice;
    private BigDecimal volume;

    @ManyToOne(targetEntity = SymbolEntity.class)
    @JoinColumn(name = "symbol", referencedColumnName = "value")
    private SymbolEntity symbol;
}
