package inu.voucherview.service;

import inu.voucherview.domain.Post;
import inu.voucherview.domain.PostCategory;
import inu.voucherview.domain.PostVote;
import inu.voucherview.dto.PostDto;
import inu.voucherview.response.PostListResponse;

public interface PostService {
    /**
     * 게시물 작성
     */
    Post createPost(Long userId, PostCategory category, String title, String content);

    /**
     * 게시물 목록 조회 (필터링, 정렬, 검색)
     */
    PostListResponse getPostList(int page, int limit, PostCategory category, String keyword, String sortBy);

    /**
     * 게시물 상세 조회
     */
    PostDto getPostById(Long postId);

    /**
     * 게시물 수정
     */
    void updatePost(Long postId, Long userId, PostCategory category, String title, String content);

    /**
     * 게시물 삭제
     */
    void deletePost(Long postId, Long userId);

    /**
     * 게시물 추천/비추천
     */
    void votePost(Long postId, Long userId, PostVote.VoteType voteType);

    /**
     * 게시물 추천/비추천 취소
     */
    void unvotePost(Long postId, Long userId);
}
