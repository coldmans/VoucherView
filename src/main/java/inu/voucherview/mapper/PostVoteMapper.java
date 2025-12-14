package inu.voucherview.mapper;

import inu.voucherview.domain.PostVote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostVoteMapper {

    // 투표 추가 또는 변경
    int upsert(PostVote postVote);

    // 투표 조회
    PostVote findByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);

    // 투표 삭제
    int delete(@Param("userId") Long userId, @Param("postId") Long postId);

    // 게시물의 추천 수 조회
    int countUpvotes(@Param("postId") Long postId);

    // 게시물의 비추천 수 조회
    int countDownvotes(@Param("postId") Long postId);
}
