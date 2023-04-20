package com.ayderbek.springbootexample.config;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class TwilioConfiguration {
//    @Value("${twilio.account_sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth_token}")
//    private String authToken;
//
//    @Value("${twilio.phone_number}")
//    private String twilioPhoneNumber;
//
//    public void sendSms(String toPhoneNumber, String messageBody) {
//        Twilio.init(accountSid, authToken);
//        Message message = Message.creator(new PhoneNumber(toPhoneNumber), new PhoneNumber(twilioPhoneNumber), messageBody).create();
//        System.out.println("SMS sent to " + message.getTo() + ", message SID: " + message.getSid());
//    }
//}
