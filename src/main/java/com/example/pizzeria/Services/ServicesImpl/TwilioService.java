package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.Configuration.TwilioConfiguration;
import com.example.pizzeria.Configuration.TwilioRequest;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Autowired
    private TwilioConfiguration twilioConfiguration;

    public void sendSMS(TwilioRequest twilioRequest) {
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(twilioRequest.getPhoneNumber()),
                        new com.twilio.type.PhoneNumber(twilioConfiguration.getTrialNumber()),
                        twilioRequest.getMessage())
                .create();
    }
}
