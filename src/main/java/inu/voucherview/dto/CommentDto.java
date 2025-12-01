package inu.voucherview.dto;

import inu.voucherview.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String nickname; // 작성자 닉네임
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPostId();
        this.userId = comment.getUserId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
