package ru.practicum.explorewithme.comment;

import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.User;

import java.util.List;
import java.util.Optional;

public interface CommentStorage {
    Optional<Comment> addComment(Comment comment);
    List<Comment> getCommentsByUserForEvent(User user, Event event);
    List<Comment> getCommentsForEvent(Event event);
    List<Comment> getCommentsForUser(User user);
    Optional<Comment> getCommentById(Long commentId);
    Optional<Comment> deleteCommentById(Long commentId);
}
