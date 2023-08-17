package com.example.pizzeria.Controllers;

import com.example.pizzeria.Configuration.CreatePaymentResponse;
import com.example.pizzeria.Entities.Address;
import com.example.pizzeria.Entities.Order;
import com.example.pizzeria.Enum.OrderType;
import com.example.pizzeria.Repositories.ClientRepository;
import com.example.pizzeria.Services.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class PaymentController {

    private final OrderService orderService;
    private final ClientRepository clientRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public PaymentController(OrderService orderService, ClientRepository clientRepository, SimpMessagingTemplate messagingTemplate) {
        this.orderService = orderService;
        this.clientRepository = clientRepository;
        this.messagingTemplate = messagingTemplate;
    }


    @PostMapping("/create-payment-intent")
    public CreatePaymentResponse createPaymentIntent(@RequestBody Order order) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .putMetadata("longitude", String.valueOf(order.getAddress().getLongitude()))
                .putMetadata("latitude", String.valueOf(order.getAddress().getLatitude()))
                .putMetadata("clientId", String.valueOf(order.getClient().getId()))
                .putMetadata("smsNotification", String.valueOf(order.isSmsNotification()))
                .putMetadata("orderType", String.valueOf(order.getOrderType()))
                .putMetadata("comment", String.valueOf(order.getComment()))
                .setCurrency("eur")
                .setAmount((long) (order.getPrice() * 100L))
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return new CreatePaymentResponse(paymentIntent.getClientSecret());
    }

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/webhook")
    public String handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        if (sigHeader == null) {
            return "";
        }
        Event event;
        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
        } catch (SignatureVerificationException e) {
            // Invalid signature
            System.out.println("⚠️  Webhook error while validating signature.");
            return "";
        }
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                System.out.println("Payment for  succeeded.");
                Order order = new Order();
                order.setAddress(Address.builder()
                        .latitude(paymentIntent.getMetadata().get("latitude"))
                        .longitude(paymentIntent.getMetadata().get("longitude"))
                        .build());
                order.setOrderType(OrderType.valueOf(paymentIntent.getMetadata().get("orderType")));
                order.setSmsNotification(Boolean.parseBoolean(paymentIntent.getMetadata().get("smsNotification")));
                order.setComment(paymentIntent.getMetadata().get("comment"));
                order.setClient(clientRepository.findById(Long.valueOf(paymentIntent.getMetadata().get("clientId"))).orElseThrow(() -> new NoSuchElementException("Not found")));
                order.setPrice((double) paymentIntent.getAmount() / 100L);
                messagingTemplate.convertAndSend("/newOrder", order.getClient().getId());
                orderService.addOrder(order);
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                // Then define and call a method to handle the successful attachment of a PaymentMethod.
                // handlePaymentMethodAttached(paymentMethod);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }
        return "";
    }
}