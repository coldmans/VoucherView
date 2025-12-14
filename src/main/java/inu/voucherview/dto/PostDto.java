package inu.voucherview.dto;

import inu.voucherview.domain.Post;
import inu.voucherview.domain.PostCategory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDto {
    private Long postId;
    private Long userId;
    private String nickname; // 작성자 닉네임
    private PostCategory category;
    private String title;
    private String content;
    private Integer upvoteCount;
    private Integer downvoteCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostDto(Post post) {
        this.postId = post.getPostId();
        this.userId = post.getUserId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.upvoteCount = post.getUpvoteCount();
        this.downvoteCount = post.getDownvoteCount();
        this.commentCount = post.getCommentCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
