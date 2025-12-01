package inu.voucherview.controller;

import inu.voucherview.annotation.LoginUser;
import inu.voucherview.domain.Comment;
import inu.voucherview.dto.CommentDto;
import inu.voucherview.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 작성
     * POST /api/posts/{postId}/comments
     */
    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @LoginUser Long userId,
            @PathVariable Long postId,
            @RequestBody Map<String, String> request
    ) {
        String content = request.get("content");
        Comment comment = commentService.createComment(postId, userId, content);
        return new CommentDto(comment);
    }

    /**
     * 댓글 수정
     * PUT /api/comments/{commentId}
     */
    @PutMapping("/comments/{commentId}")
    public void updateComment(
            @LoginUser Long userId,
            @PathVariable Long commentId,
            @RequestBody Map<String, String> request
    ) {
        String content = request.get("content");
        commentService.updateComment(commentId, userId, content);
    }

    /**
     * 댓글 삭제
     * DELETE /api/comments/{commentId}
     */
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @LoginUser Long userId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId, userId);
    }
}
