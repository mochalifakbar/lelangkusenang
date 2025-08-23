package proyekuas.uas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import proyekuas.uas.entity.Chat;
import proyekuas.uas.entity.User;

@Service
public interface ChatService {
    Chat addChat(User user1, User user2);
    List<Chat> getChatsByUser(User user);
    Chat getChatById(Long chatId);
    Chat findChatBetweenUsers(User user1, User user2);
    void saveChat(Chat newChat);
}
