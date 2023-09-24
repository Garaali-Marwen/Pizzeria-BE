package com.example.pizzeria.Controllers;

import com.example.pizzeria.Configuration.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin("*")
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/add")
    public void contact(@RequestBody ContactRequest contactRequest) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("pizzeria.contact96@gmail.com");
        mailMessage.setTo("garaali.marwen@gmail.com");
        mailMessage.setText(contactRequest.message + "\n Nom: " + contactRequest.nom + "\n Email: " + contactRequest.email + "\n Tel: " + contactRequest.tel);
        mailMessage.setSubject(contactRequest.subject);

        mailSender.send(mailMessage);
    }


}
