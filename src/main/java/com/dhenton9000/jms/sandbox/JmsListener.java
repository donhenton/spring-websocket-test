/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jms.sandbox;

import static com.dhenton9000.jms.sandbox.JmsProducer.MESSAGE_COUNT_PROPERTY;
import java.io.UnsupportedEncodingException;
import org.apache.activemq.command.ActiveMQBytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class JmsListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(JmsListener.class);
    private JmsTemplate outTemplate;

    /**
     * Implementation of <code>MessageListener</code>.
     *
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            //int messageCount = 0;
            // messageCount = message.getIntProperty(JmsProducer.MESSAGE_COUNT_PROPERTY);

            if (message instanceof ActiveMQBytesMessage) {
                ActiveMQBytesMessage tm = (ActiveMQBytesMessage) message;

                Long messageLength = tm.getBodyLength();
                byte[] messageBytes = new byte[messageLength.intValue()];
                tm.readBytes(messageBytes, messageLength.intValue());
                String msg = null;
                try {
                    msg = new String(messageBytes, "UTF-8");
                    String[] t = msg.split(":");
                    msg = t[1];
                    msg = msg.replaceAll("\\{", "");
                    msg = msg.replaceAll("}", "");
                    msg = msg.replaceAll("\"", "");
                } catch (UnsupportedEncodingException ex) {
                    logger.error("encoding problem " + ex.getMessage());
                }

                logger.info("got message of '" + msg + "' in onMessage");

                final String msgFinal = msg;
                getOutTemplate().send(new MessageCreator() {

                    @Override
                    public Message createMessage(
                            Session session) throws JMSException {
                        String messageContents = "{\"messageContent\": \"Hello " + msgFinal + "\"}";
                        Message m
                                = session.createTextMessage(messageContents);

                        return m;
                    }
                });

            } else {
                logger.info("message was of type " + message.getClass().getName());
            }
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @return the outTemplate
     */
    public JmsTemplate getOutTemplate() {
        return outTemplate;
    }

    /**
     * @param outTemplate the outTemplate to set
     */
    public void setOutTemplate(JmsTemplate outTemplate) {
        this.outTemplate = outTemplate;
    }
}
