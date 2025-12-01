package inu.voucherview.service;

import inu.voucherview.domain.Comment;
import inu.voucherview.dto.CommentDto;

import java.util.List;

public interface CommentService {
    /**
     * 댓글 작성
     */
    Comment createComment(Long postId, Long userId, String content);

    /**
     * 게시물의 댓글 목록 조회
     */
    List<CommentDto> getCommentsByPostId(Long postId);

    /**
     * 댓글 수정
     */
    void updateComment(Long commentId, Long userId, String content);

    /**
     * 댓글 삭제
     */
    void deleteComment(Long commentId, Long userId);
}
