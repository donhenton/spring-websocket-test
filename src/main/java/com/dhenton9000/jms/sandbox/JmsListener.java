/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jms.sandbox;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import org.apache.activemq.command.ActiveMQBytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class JmsListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(JmsListener.class);
     
    

    /**
     * Implementation of
     * <code>MessageListener</code>.
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
                 tm.readBytes(messageBytes,messageLength.intValue());
                 String msg = null;
                try {
                    msg = new String(messageBytes,"UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    logger.error("encoding problem "+ex.getMessage());
                }
                //int counterVal = counter.incrementAndGet();
               // Object[] infoArray = {msg, messageCount, counterVal};
               // logger.info("Processed message '{}'.  value={}, counter={}", infoArray);
                logger.info("got message of '"+msg+"' in onMessage");
            } else {
                logger.info("message was of type " + message.getClass().getName());
            }
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
    }
}