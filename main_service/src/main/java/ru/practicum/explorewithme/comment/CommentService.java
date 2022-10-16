package ru.practicum.explorewithme.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.EventStorage;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserStorage;

import java.util.List;

@Service
public class CommentService {
    private final CommentStorage commentStorage;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    @Autowired
    public CommentService(CommentStorage commentStorage, UserStorage userStorage, EventStorage eventStorage) {
        this.commentStorage = commentStorage;
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
    }

    public List<Comment> getCommentsByUserForEvent(Long userId, Long eventId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        return commentStorage.getCommentsByUserForEvent(user, event);

    }

    public List<Comment> getCommentsForEvent(Long eventId) {
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        return commentStorage.getCommentsForEvent(event);
    }

    public List<Comment> getCommentsForUser(Long userId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        return commentStorage.getCommentsForUser(user);
    }

    public Comment createCommentByUserForEvent(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can comment only published events");
        }
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setText(newCommentDto.getText());
        return commentStorage.addComment(comment).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add comment")
        );
    }

    public Comment deleteCommentByUserWithId(Long userId, Long commentId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Comment comment = commentStorage.getCommentById(commentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find comment")
        );
        if (!comment.getAuthor().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only author or admin can delete own comment");
        }
        return commentStorage.deleteCommentById(commentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to delete comment")
        );

    }

    public Comment deleteCommentById(Long commentId) {
        Comment comment = commentStorage.getCommentById(commentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find comment")
        );
        return commentStorage.deleteCommentById(comment.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to delete comment")
        );
    }
}
