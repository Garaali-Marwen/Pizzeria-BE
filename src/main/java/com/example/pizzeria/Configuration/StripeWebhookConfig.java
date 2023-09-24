package com.example.pizzeria.Configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class StripeWebhookConfig {

  /*  private Process stripeCliProcess;

    @Bean
    public CommandLineRunner stripeWebhookInitializer() {
        return args -> {
            if (stripeCliProcess != null) {
                stripeCliProcess.destroy();
                stripeCliProcess.waitFor(5, TimeUnit.SECONDS);
            }

            String stripeCliCommand = "stripe listen --forward-to http://localhost:8080/webhook";

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", stripeCliCommand);

            try {
                stripeCliProcess = processBuilder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }*/
}
