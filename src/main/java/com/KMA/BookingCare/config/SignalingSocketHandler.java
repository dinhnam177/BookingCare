package com.KMA.BookingCare.config;

import com.KMA.BookingCare.Dto.SignalMessage;
import com.KMA.BookingCare.common.JsonUtils;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Component
public class SignalingSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SignalingSocketHandler.class);

    private static final String TYPE_LOGOUT = "logout";

    /**
     * Cache of sessions by users.
     */
    private final Map<String, WebSocketSession> connectedUsers = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("[" + session.getId() + "] Connection established " + session.getId());

        final SignalMessage newMenOnBoard = new SignalMessage();
        newMenOnBoard.setType("show_id");
        newMenOnBoard.setSender(session.getId());
        newMenOnBoard.setData(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.info("[" + session.getId() + "] Connection closed " + session.getId() + " with status: " + status.getReason());
        removeUserAndSendLogout(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOG.info("[" + session.getId() + "] Connection error " + session.getId() + " with status: " + exception.getLocalizedMessage());
        removeUserAndSendLogout(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.info("handleTextMessage : {}", message.getPayload());

        SignalMessage signalMessage = JsonUtils.getObject(message.getPayload());
        if (signalMessage.getType().equals("save")) {
            connectedUsers.put(signalMessage.getSender(), session);
            return;
        }

        // with the destinationUser find the targeted socket, if any
        String destinationUser = signalMessage.getReceiver();
        WebSocketSession destSocket = connectedUsers.get(destinationUser);
        // if the socket exists and is open, we go on
        if (destSocket != null && destSocket.isOpen()) {
            final String resendingMessage = JsonUtils.getString(signalMessage);
            LOG.info("send message {} to {}", resendingMessage, destinationUser);
            destSocket.sendMessage(new TextMessage(resendingMessage));
        }
    }

    private void removeUserAndSendLogout(final String sessionId) {

        // send the message to all other peers, somebody(sessionId) leave.
        String senderId = "";
        for (Map.Entry<String, WebSocketSession> entry : connectedUsers.entrySet()) {
            if(sessionId != null && sessionId.equals(entry.getValue().getId())) {
                senderId = entry.getKey();
            }
        }

        if (Strings.isNotBlank(senderId)) {
            final SignalMessage menOut = new SignalMessage();
            menOut.setType(TYPE_LOGOUT);
            menOut.setSender(senderId);

            connectedUsers.values().forEach(webSocket -> {
                try {
                    webSocket.sendMessage(new TextMessage(JsonUtils.getString(menOut)));
                } catch (Exception e) {
                    LOG.warn("Error while message sending.", e);
                }
            });
        }
    }

}
