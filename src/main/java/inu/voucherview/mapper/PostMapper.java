package inu.voucherview.mapper;

import inu.voucherview.domain.Post;
import inu.voucherview.domain.PostCategory;
import inu.voucherview.util.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    // 게시물 생성
    int insert(Post post);

    // 게시물 조회
    Post findById(@Param("postId") Long postId);

    // 게시물 목록 조회 (필터링, 정렬, 검색)
    List<Post> findWithFilters(
            @Param("pagination") Pagination pagination,
            @Param("category") PostCategory category,
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy // "latest" 또는 "popular"
    );

    // 게시물 개수 조회
    int countWithFilters(
            @Param("category") PostCategory category,
            @Param("keyword") String keyword
    );

    // 게시물 수정
    int update(Post post);

    // 게시물 삭제
    int delete(@Param("postId") Long postId);

    // 추천/비추천 수 업데이트
    int updateVoteCounts(@Param("postId") Long postId,
                         @Param("upvoteCount") Integer upvoteCount,
                         @Param("downvoteCount") Integer downvoteCount);

    // 댓글 수 업데이트
    int updateCommentCount(@Param("postId") Long postId, @Param("commentCount") Integer commentCount);
}
