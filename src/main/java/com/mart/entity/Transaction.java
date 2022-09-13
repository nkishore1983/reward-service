package com.mart.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private BigInteger id;
    @Column(name = "purchase_amount", nullable = false)
    private BigDecimal purchaseAmount;
    @Column(name = "tx_date", nullable = false)
    private OffsetDateTime transactionDate;
    @ManyToOne
    @JoinColumn(name="customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

}
