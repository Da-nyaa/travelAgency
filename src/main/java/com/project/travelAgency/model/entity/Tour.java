package com.project.travelAgency.model.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TypeOfTour typeOfTour;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    private Country country;
    @Column
    private int days;
    @Column
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToMany(mappedBy = "tours", cascade = CascadeType.ALL)
    private List<Cart> carts;
    @OneToMany(mappedBy = "tour",cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetailsList;

}
