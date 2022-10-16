package ru.practicum.explorewithme.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.User;

import java.util.List;
import java.util.Optional;

@Component
public class PersistentCommentStorage implements CommentStorage {
    private final CommentRepository commentRepository;

    @Autowired
    public PersistentCommentStorage(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<Comment> addComment(Comment comment) {
        return Optional.of(commentRepository.save(comment));
    }

    @Override
    public List<Comment> getCommentsByUserForEvent(User user, Event event) {
        return commentRepository.findAllByAuthorAndEventOrderByIdDesc(user, event);
    }

    @Override
    public List<Comment> getCommentsForEvent(Event event) {
        return commentRepository.findAllByEventOrderByIdDesc(event);
    }

    @Override
    public List<Comment> getCommentsForUser(User user) {
        return commentRepository.findAllByAuthorOrderByIdDesc(user);
    }

    @Override
    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public Optional<Comment> deleteCommentById(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(c -> commentRepository.deleteById(commentId));
        return comment;
    }
}
