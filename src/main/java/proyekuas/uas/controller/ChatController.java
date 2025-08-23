package proyekuas.uas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import proyekuas.uas.entity.Chat;
import proyekuas.uas.entity.User;
import proyekuas.uas.service.ChatService;
import proyekuas.uas.service.UserService;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @GetMapping("/chat")
    public String chat(HttpSession session, Model model){
        if(session.getAttribute("loggedUser") == null){
            return "redirect:/login";
        }

        User loggedUser = userService.findByUsername(session.getAttribute("loggedUser").toString());
        model.addAttribute("user", loggedUser);

        List<Chat> userChats = chatService.getChatsByUser(loggedUser);
        model.addAttribute("userChats", userChats);

        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);

        return "chat";
    }

    @GetMapping("/chat/{chatId}")
    public String loadChat(@PathVariable Long chatId, Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        User loggedUser = userService.findByUsername(session.getAttribute("loggedUser").toString());
        Chat chat = chatService.getChatById(chatId);
        if(chat == null || !chat.getUser1().equals(loggedUser) && !chat.getUser2().equals(loggedUser)){
            return "redirect:/chat";
        }
        model.addAttribute("chat", chat);
        model.addAttribute("user", loggedUser);
        return "chatDetail";
    }

    @GetMapping("/searchUsers")
    @ResponseBody
    public List<User> searchUsers(@RequestParam String query) {
        return userService.searchUsers(query);
    }

    @GetMapping("/startChat/{userId}")
    public String startNewChat(HttpSession session, @PathVariable Long userId, Model model) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        User loggedUser = userService.findByUsername(session.getAttribute("loggedUser").toString());
        User otherUser = userService.getUserById(userId);

        Chat existingChat = chatService.findChatBetweenUsers(loggedUser, otherUser);

        if (existingChat == null) {
            Chat newChat = new Chat();
            newChat.setUser1(loggedUser);
            newChat.setUser2(otherUser);
            chatService.saveChat(newChat);
            model.addAttribute("chat", newChat);
            return "redirect:/chat/" + newChat.getId();
        } else {
            model.addAttribute("chat", existingChat);
            return "redirect:/chat/" + existingChat.getId();
        }
    }
}