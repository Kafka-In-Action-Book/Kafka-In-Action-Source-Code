package com.kafkainaction.kstreams;

public class EmailService {
	
    public void sendMessage(Email email) {
        System.out.println(email.getCustomter().getEmailAddress());
        System.out.println(email.getMailingList().getMessage());
    }

}
