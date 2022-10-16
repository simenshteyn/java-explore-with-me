package ru.practicum.explorewithme.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.User;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthorAndEventOrderByIdDesc(User author, Event event);
    List<Comment> findAllByEventOrderByIdDesc(Event event);
    List<Comment> findAllByAuthorOrderByIdDesc(User author);
}
