package com.muntaqa.springcruddemo.services;

import com.muntaqa.springcruddemo.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
