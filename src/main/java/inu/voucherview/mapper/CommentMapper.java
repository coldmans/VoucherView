package inu.voucherview.mapper;

import inu.voucherview.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    // 댓글 생성
    int insert(Comment comment);

    // 댓글 조회
    Comment findById(@Param("commentId") Long commentId);

    // 게시물의 댓글 목록 조회
    List<Comment> findByPostId(@Param("postId") Long postId);

    // 게시물의 댓글 개수 조회
    int countByPostId(@Param("postId") Long postId);

    // 댓글 수정
    int update(Comment comment);

    // 댓글 삭제
    int delete(@Param("commentId") Long commentId);
}
