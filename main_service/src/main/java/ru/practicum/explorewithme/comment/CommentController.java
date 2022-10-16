package ru.practicum.explorewithme.comment;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<?> getCommentsByUserForEvent(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {
        return ResponseEntity.ok(commentService.getCommentsByUserForEvent(userId, eventId).stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<?> getCommentsForEvent(@PathVariable @Positive Long eventId) {
        return ResponseEntity.ok(commentService.getCommentsForEvent(eventId).stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/users/{userId}/events/comments")
    public ResponseEntity<?> getCommentsForUser(@PathVariable @Positive Long userId) {
        return ResponseEntity.ok(commentService.getCommentsForUser(userId).stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<?> createCommentByUserForEvent(
            HttpServletRequest request,
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid NewCommentDto newCommentDto,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(convertToDto(commentService.createCommentByUserForEvent(userId, eventId, newCommentDto)));
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public ResponseEntity<?> deleteCommentByUserWithId(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long commentId) {
        return ResponseEntity.ok(convertToDto(commentService.deleteCommentByUserWithId(userId, commentId)));
    }

    @DeleteMapping("/admin/comments/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable @Positive Long commentId) {
        return ResponseEntity.ok(convertToDto(commentService.deleteCommentById(commentId)));
    }

    public CommentDto convertToDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }
}
