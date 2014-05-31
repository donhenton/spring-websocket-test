/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.spring.mvc.controllers;


import com.dhenton9000.spring.websocket.model.MessageBroadcast;
import com.dhenton9000.spring.websocket.model.SimpleMessage;
import com.dhenton9000.spring.websocket.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;

/**
 *
 * @author dhenton
 */
public class WebsocketController {

    private static final Logger LOG = LoggerFactory
            .getLogger(WebsocketController.class);
 
    @MessageMapping("/simplemessages")
    @SendTo("/topic/simplemessagesresponse")
    public MessageBroadcast processMessageFromClient(SimpleMessage message) throws Exception {
        // Simulate a delay of 3 seconds
        Thread.sleep(1200);
        LOG.info("#########Sending server side response '{}' ", message);
        return new MessageBroadcast("Server response: Did you send &lt;b&gt;'"
                + message.getMessage() + "'&lt;/b&gt;? (Server Response at: "
                + Util.getSimpleDate() + ")");
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
