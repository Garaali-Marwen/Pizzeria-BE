package com.example.pizzeria;

import com.example.pizzeria.Configuration.TwilioConfiguration;
import com.stripe.Stripe;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PizzeriaBeApplication {

    @Autowired
    private TwilioConfiguration twilioConfiguration;

    @PostConstruct
    public void setup() {
        Stripe.apiKey = "sk_test_51NeT2MGlUjM6c4dzFqUq8lra3UKnvskBgPhw2WOav5pEM0J19qfAo8uyQTHxQiplxwNqsCkQ3z1eQnRS8Qg1BiM2008WVJRPlB";
        Twilio.init(twilioConfiguration.getAccountSid(), twilioConfiguration.getAuthToken());
    }
    public static void main(String[] args) {SpringApplication.run(PizzeriaBeApplication.class, args);}

    @Bean
    public CommandLineRunner stripeWebhookInitializer() {
        return args -> {
            String stripeCliCommand = "stripe listen --forward-to http://localhost:8080/webhook";

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", stripeCliCommand);

            try {
                Process process = processBuilder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
