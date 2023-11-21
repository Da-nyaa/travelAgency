package com.project.travelAgency.model.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;
    private BigDecimal amount;
    private BigDecimal price;

    public OrderDetails(Order order, Tour tour, Long amount, Long price) {
        this.order = order;
        this.tour = tour;
        this.amount = new BigDecimal(amount);
        this.price = tour.getPrice();
    }
}
