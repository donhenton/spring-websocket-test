/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jms.sandbox;

import com.dhenton9000.spring.websocket.model.MessageBroadcast;
import com.dhenton9000.spring.websocket.model.SimpleMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            final ObjectMapper mapper = new ObjectMapper();

            if (message instanceof ActiveMQBytesMessage) {
                ActiveMQBytesMessage tm = (ActiveMQBytesMessage) message;

                Long messageLength = tm.getBodyLength();
                byte[] messageBytes = new byte[messageLength.intValue()];
                tm.readBytes(messageBytes, messageLength.intValue());
                String msg = null;
                try {
                    msg = new String(messageBytes, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    logger.error("encoding problem " + ex.getMessage());
                }

                logger.info("got message of '" + msg + "' in onMessage");

                final String msgInboundFinal = msg;
                getOutTemplate().send(new MessageCreator() {

                    @Override
                    public Message createMessage(
                            Session session) throws JMSException {
                        String info = "";
                        SimpleMessage simpleMessage;
                        try {
                            simpleMessage = mapper.readValue(msgInboundFinal, SimpleMessage.class);
                            info = simpleMessage.getMessage();
                        } catch (IOException ex) {
                            logger.error("io problem translating to Simplemessage: " + ex.getMessage());
                        }
                        if (info == null) {
                            info = "null";
                        }
                        MessageBroadcast mB = new MessageBroadcast("Hello "+info);
                        
                        Writer w = new StringWriter();
                        try {
                            mapper.writeValue(w, mB);
                        } catch (IOException e) {
                            logger.error(e.getClass().getName() + " " + e.getMessage());

                        }

                        Message m
                                = session.createTextMessage(w.toString());

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
