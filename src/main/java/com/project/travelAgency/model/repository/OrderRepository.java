package com.project.travelAgency.model.repository;

import com.project.travelAgency.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserName(String name);

}
