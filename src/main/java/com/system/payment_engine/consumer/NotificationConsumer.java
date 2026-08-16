package com.system.payment_engine.consumer;

import com.system.payment_engine.config.RabbitMQConfig;
import com.system.payment_engine.dto.OrderIDMessageDTO;
import com.system.payment_engine.model.Order;
import com.system.payment_engine.model.Product;
import com.system.payment_engine.repository.OrderRepository;
import com.system.payment_engine.repository.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public NotificationConsumer(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeNotification(OrderIDMessageDTO message) {
        if (message == null || message.orderId() == null) {
            System.err.println("❌ Erro: Mensagem recebida do RabbitMQ está com o orderId nulo!");
            return;
        }

        // Busca o Pedido pelo ID
        Order order = orderRepository.findById(message.orderId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado ID: " + message.orderId()));

        // Busca o Produto associado ao pedido
        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado ID: " + order.getProductId()));

        System.out.println("=================================================");
        System.out.println("[RABBITMQ CONSUMER] Notificação recebida!");
        System.out.println("Pedido ID: " + order.getId());
        System.out.println("Produto: " + product.getName());
        System.out.println("Valor Total: R$ " + order.getTotalAmount());
        System.out.println("Status: " + order.getStatus());
        System.out.println("Simulando envio de e-mail de confirmação ao cliente...");
        System.out.println("=================================================");
    }
}