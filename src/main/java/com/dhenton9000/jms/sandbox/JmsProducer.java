/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jms.sandbox;

import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 *
 * @author dhenton
 */
public class JmsProducer {

    public static final String MESSAGE_COUNT_PROPERTY = "MESSAGE_COUNT";
    private static final Logger logger = LoggerFactory.getLogger(JmsProducer.class);
    private JmsTemplate template = null;

    public JmsTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JmsTemplate template) {
        this.template = template;
    }

    public void doProduce() {

        Properties props = new Properties();
        for (int i = 1; i <= 3; i++) {
            final int iVal = i;
            template.send(new MessageCreator() {

                public Message createMessage(
                        Session session) throws JMSException {
                    String messageContents = "Hello " + iVal;
                    Message m =
                            session.createTextMessage(messageContents);
                    m.setStringProperty(MESSAGE_COUNT_PROPERTY, iVal + "");
                     logger.info("Producer Sent: "+messageContents);
                    return m;
                }
            });
           
        }

    }
}