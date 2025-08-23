package proyekuas.uas.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyekuas.uas.entity.Chat;
import proyekuas.uas.entity.User;
import proyekuas.uas.repository.ChatRepository;
import proyekuas.uas.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService{

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Chat addChat(User user1, User user2) {
        Chat chat = new Chat();
        chat.setUser1(user1);
        chat.setUser2(user2);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }

    @Override
    public List<Chat> getChatsByUser(User user) {
        return chatRepository.findByUser(user);
    }

    @Override
    public Chat getChatById(Long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }

    @Override
    public Chat findChatBetweenUsers(User user1, User user2) {
        return chatRepository.findChatBetweenUsers(user1, user2).orElse(null);
    }

    @Override
    public void saveChat(Chat newChat) {
        chatRepository.save(newChat);
    }
}
