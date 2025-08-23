package proyekuas.uas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import proyekuas.uas.entity.Chat;
import proyekuas.uas.entity.User;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE c.user1 = :user OR c.user2 = :user")
    List<Chat> findByUser(User user);

    @Query("SELECT c FROM Chat c WHERE (c.user1 = :user1 AND c.user2 = :user2) OR (c.user1 = :user2 AND c.user2 = :user1)")
    Optional<Chat> findChatBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);
}
