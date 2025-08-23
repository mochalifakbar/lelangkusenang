package proyekuas.uas.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import proyekuas.uas.entity.Chat;
import proyekuas.uas.entity.Message;
import proyekuas.uas.entity.User;
import proyekuas.uas.repository.ChatRepository;
import proyekuas.uas.repository.MessageRepository;
import proyekuas.uas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{chatId}")
@Component
public class ChatEndpoint {
    private static final Map<Long, Set<Session>> chatSessions = new ConcurrentHashMap<>();
    private static ChatRepository staticChatRepository;
    private static MessageRepository staticMessageRepository;
    private static UserRepository staticUserRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setRepositories(
        ChatRepository chatRepository, 
        MessageRepository messageRepository, 
        UserRepository userRepository
    ) {
        staticChatRepository = chatRepository;
        staticMessageRepository = messageRepository;
        staticUserRepository = userRepository;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") Long chatId) {
        chatSessions.computeIfAbsent(chatId, k -> ConcurrentHashMap.newKeySet()).add(session);
        System.out.println("Chat " + chatId + " connected, session id: " + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String messageJson, @PathParam("chatId") Long chatId) {
        try {
            // Parse incoming message JSON
            Map<String, Object> messageData = objectMapper.readValue(messageJson, Map.class);
            Long senderId = Long.valueOf(messageData.get("senderId").toString());
            String messageText = messageData.get("message").toString();

            // Find chat and sender
            Chat chat = staticChatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
            User sender = staticUserRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

            // Create and save message
            Message newMessage = new Message();
            newMessage.setPesan(messageText);
            newMessage.setWaktu(LocalDateTime.now());
            newMessage.setDibaca(false);
            newMessage.setChat(chat);
            newMessage.setPengirim(sender);
            staticMessageRepository.save(newMessage);

            // Prepare message for broadcasting
            Map<String, Object> broadcastMessage = new HashMap<>();
            broadcastMessage.put("senderName", sender.getNama());
            broadcastMessage.put("message", messageText);

            // Broadcast to all sessions in this chat
            Set<Session> sessions = chatSessions.get(chatId);
            if (sessions != null) {
                for (Session s : sessions) {
                    if (s.isOpen()) {
                        s.getBasicRemote().sendText(objectMapper.writeValueAsString(broadcastMessage));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("chatId") Long chatId) {
        Set<Session> sessions = chatSessions.get(chatId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                chatSessions.remove(chatId);
            }
        }
        System.out.println("Session " + session.getId() + " closed.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}