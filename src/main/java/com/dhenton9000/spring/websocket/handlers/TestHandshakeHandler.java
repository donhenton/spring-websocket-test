/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dhenton9000.spring.websocket.handlers;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
/**
 *
 * @author dhenton
 */
public class TestHandshakeHandler implements HandshakeHandler {

    public boolean doHandshake(ServerHttpRequest shr, ServerHttpResponse shr1, WebSocketHandler wsh, Map<String, Object> map) throws HandshakeFailureException {
        return true; 
    }
    
}
