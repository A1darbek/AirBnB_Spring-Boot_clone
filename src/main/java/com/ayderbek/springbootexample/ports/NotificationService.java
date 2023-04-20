package com.ayderbek.springbootexample.ports;


import com.ayderbek.springbootexample.domain.Notification;
import com.ayderbek.springbootexample.domain.Reservation;
import com.ayderbek.springbootexample.domain.User;
import com.ayderbek.springbootexample.repos.NotificationRepository;
import com.twilio.exception.TwilioException;
import com.twilio.http.TwilioRestClient;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;

//    private final TwilioRestClient twilioRestClient;
//
//    private final NotificationRepository notificationRepository;

    public void sendNotificationEmail(String recipientEmail, Reservation reservation) {

        String subject = "Reservation Confirmation - " + reservation.getProperty().getName();
        String message = "Dear " + reservation.getUser().getFirstname() + ",\n\n" +
                "Your reservation for " + reservation.getProperty().getName() + " has been confirmed.\n\n" +
                "Check-in date: " + reservation.getStartDate() + "\n" +
                "Check-out date: " + reservation.getEndDate() + "\n" +
                "Number of guests: " + reservation.getNumberOfGuests() + "\n" +
                "Total price: " + reservation.getTotalPrice() + "\n\n" +
                "Thank you for choosing our service!\n" +
                "Best regards,\n" +
                "The Airbnb Team";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);

        javaMailSender.send(email);
    }

//    public void sendReservationNotification(User user, Reservation reservation) throws TwilioException {
//        // Create the notification
//        String content = "Your reservation #" + reservation.getId() + " has been confirmed.";
//        Notification notification = new Notification(content, user, reservation);
//
//        // Save the notification to the database
//        notificationRepository.save(notification);
//
//        // Send the SMS notification
//        Message message = new Message.Builder()
//                .body(content)
//                .from(new PhoneNumber("<your Twilio phone number>"))
//                .to(new PhoneNumber(user.getPhoneNumber()))
//                .build();
//        twilioRestClient.messages().create(message);
//    }
}
