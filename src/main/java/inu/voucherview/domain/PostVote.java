package inu.voucherview.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = "voteId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostVote {
    private Long voteId;
    private Long postId;
    private Long userId;
    private VoteType voteType;
    private LocalDateTime createdAt;

    public enum VoteType {
        UP, DOWN
    }
}
