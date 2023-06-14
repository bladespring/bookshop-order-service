package com.bladespring.bookshop.orderservice.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import com.bladespring.bookshop.orderservice.PostgreSQLTestContainerBase;
import com.bladespring.bookshop.orderservice.config.DataConfig;

import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(DataConfig.class)
public class OrderRepositoryR2dbcTests extends PostgreSQLTestContainerBase {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void createRejectedOrder() {
        var rejectedOrder = OrderService.buildRejectedOrder("1234567890", 3);
        StepVerifier.create(orderRepository.save(rejectedOrder))
                .expectNextMatches(
                        order -> order.status().equals(OrderStatus.REJECTED))
                .verifyComplete();
    }
}
