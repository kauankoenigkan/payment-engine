package com.system.payment_engine.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        if (stripeApiKey == null || stripeApiKey.isBlank() || stripeApiKey.contains("SuaChaveAqui")) {
            System.err.println("❌ ERRO CRÍTICO: A chave do Stripe não foi configurada corretamente em application.properties!");
        } else {
            System.out.println("✅ Stripe inicializado com sucesso com a chave: " + stripeApiKey.substring(0, 10) + "...");
            Stripe.apiKey = this.stripeApiKey;
        }
    }

    public PaymentIntent createPaymentIntent(BigDecimal amount) throws Exception {
        long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("brl")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        return PaymentIntent.create(params);
    }
}