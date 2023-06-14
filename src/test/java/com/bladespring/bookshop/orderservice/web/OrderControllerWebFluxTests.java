package com.bladespring.bookshop.orderservice.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bladespring.bookshop.orderservice.domain.Order;
import com.bladespring.bookshop.orderservice.domain.OrderService;
import com.bladespring.bookshop.orderservice.domain.OrderStatus;

import reactor.core.publisher.Mono;

@WebFluxTest(OrderController.class)
public class OrderControllerWebFluxTests {
    @Autowired
    private WebTestClient webClient;

    @MockBean
    private OrderService orderService;

    @Test
    void whenBookNotAvailableThenRejectOrder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = OrderService.buildRejectedOrder(
                orderRequest.getIsbn(), orderRequest.getQuantity());
        BDDMockito.given(orderService.submitOrder(orderRequest.getIsbn(), orderRequest.getQuantity()))
                .willReturn(Mono.just(expectedOrder));

        webClient.post()
                .uri("/orders/")
                .bodyValue(orderRequest).exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(actualOrder -> {
                    Assertions.assertThat(actualOrder).isNotNull();
                    Assertions.assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });

    }
}
