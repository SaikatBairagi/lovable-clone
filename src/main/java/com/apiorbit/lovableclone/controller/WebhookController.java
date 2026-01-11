package com.apiorbit.lovableclone.controller;

import com.apiorbit.lovableclone.service.PaymentService;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookUrl;

    @PostMapping("/api/webhook/payment")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                        @RequestHeader("Stripe-Signature") String header) {
        try{
            Event event = Webhook.constructEvent(payload, header, stripeWebhookUrl);

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if(dataObjectDeserializer.getObject().isPresent()){
                stripeObject = dataObjectDeserializer.getObject().get(); //Happy Case
            } else{
                try{
                    stripeObject = dataObjectDeserializer.deserializeUnsafe(); //fallback deserialized from raw JSON
                    if(stripeObject == null){
                        log.warn("Could not deserialize stripe object for event {}", event.getType());
                        return ResponseEntity.badRequest().build();
                    }
                } catch (EventDataObjectDeserializationException e) {
                    throw new RuntimeException(e);
                }
            }

            //Now extract the metadata if the response from Checkout Session
            Map<String, String> metadataMap = new HashMap<>();
            if(stripeObject instanceof Session session){
                metadataMap =  session.getMetadata();
            }

            paymentService.handleWebhookRequest(event.getType(), stripeObject, metadataMap);
            return  ResponseEntity.ok().build();
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }
}
