package com.system.payment_engine.service;

import com.stripe.model.PaymentIntent;
import com.system.payment_engine.config.RabbitMQConfig;
import com.system.payment_engine.dto.CreateOrderDTO;
import com.system.payment_engine.dto.OrderNotificationDTO;
import com.system.payment_engine.model.Order;
import com.system.payment_engine.model.Product;
import com.system.payment_engine.repository.OrderRepository;
import com.system.payment_engine.repository.ProductRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StripeService stripeService;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(ProductRepository productRepository,
                        OrderRepository orderRepository,
                        StripeService stripeService,
                        RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.stripeService = stripeService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Order processOrder(CreateOrderDTO dto) {
        // Busca Produtos e confirma disponibilidade no estoque
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (product.getStockQuantity() < dto.quantity()) {
            throw new RuntimeException("Estoque insuficiente");
        }

        // Abate o estoque
        product.setStockQuantity(product.getStockQuantity() - dto.quantity());
        productRepository.save(product);

        // Calcula valor do pedido
        BigDecimal totalAmount = product.getPrice().multiply(new BigDecimal(dto.quantity()));

        // Salva pedido
        Order order = Order.builder()
                .productId(product.getId())
                .quantity(dto.quantity())
                .totalAmount(totalAmount)
                .status(Order.OrderStatus.CREATED)
                .build();
        order = orderRepository.save(order);

        try {
            // Chama API
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(totalAmount);
            order.setStripePaymentIntentId(paymentIntent.getId());
            order.setStatus(Order.OrderStatus.PAID);
            orderRepository.save(order);

            // Envia para o RABBITMQ
            OrderNotificationDTO notification = new OrderNotificationDTO(
                    order.getId(),
                    order.getProductId(),
                    product.getName(),
                    order.getTotalAmount(),
                    order.getStatus().name()
            );
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, notification);


        } catch (Exception e) {
            // Fase final da Transação (como exceção de erro)
            product.setStockQuantity(product.getStockQuantity() + dto.quantity());
            productRepository.save(product);

            order.setStatus(Order.OrderStatus.FAILED);
            orderRepository.save(order);

            throw new RuntimeException("Falha ao processar pagamento com o Stripe: " + e.getMessage());
        }

        return order;
    }
}