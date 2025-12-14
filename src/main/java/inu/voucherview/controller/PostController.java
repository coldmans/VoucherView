package inu.voucherview.controller;

import inu.voucherview.annotation.LoginUser;
import inu.voucherview.domain.Post;
import inu.voucherview.domain.PostCategory;
import inu.voucherview.domain.PostVote;
import inu.voucherview.dto.CommentDto;
import inu.voucherview.dto.PostDto;
import inu.voucherview.response.PostListResponse;
import inu.voucherview.service.CommentService;
import inu.voucherview.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    /**
     * 게시물 작성
     * POST /api/posts
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(
            @LoginUser Long userId,
            @RequestBody Map<String, Object> request
    ) {
        PostCategory category = PostCategory.valueOf(request.get("category").toString());
        String title = request.get("title").toString();
        String content = request.get("content").toString();

        Post post = postService.createPost(userId, category, title, content);
        return new PostDto(post);
    }

    /**
     * 게시물 목록 조회
     * GET /api/posts?page=1&limit=10&category=QNA&keyword=검색어&sortBy=latest
     */
    @GetMapping
    public PostListResponse getPostList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit,
            @RequestParam(name = "category", required = false) PostCategory category,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortBy", defaultValue = "latest") String sortBy
    ) {
        return postService.getPostList(page, limit, category, keyword, sortBy);
    }

    /**
     * 게시물 상세 조회
     * GET /api/posts/{postId}
     */
    @GetMapping("/{postId}")
    public PostDto getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    /**
     * 게시물 수정
     * PUT /api/posts/{postId}
     */
    @PutMapping("/{postId}")
    public void updatePost(
            @LoginUser Long userId,
            @PathVariable Long postId,
            @RequestBody Map<String, Object> request
    ) {
        PostCategory category = PostCategory.valueOf(request.get("category").toString());
        String title = request.get("title").toString();
        String content = request.get("content").toString();

        postService.updatePost(postId, userId, category, title, content);
    }

    /**
     * 게시물 삭제
     * DELETE /api/posts/{postId}
     */
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @LoginUser Long userId,
            @PathVariable Long postId
    ) {
        postService.deletePost(postId, userId);
    }

    /**
     * 게시물 추천/비추천
     * POST /api/posts/{postId}/vote
     */
    @PostMapping("/{postId}/vote")
    public Map<String, String> votePost(
            @LoginUser Long userId,
            @PathVariable Long postId,
            @RequestBody Map<String, String> request
    ) {
        PostVote.VoteType voteType = PostVote.VoteType.valueOf(request.get("voteType"));
        postService.votePost(postId, userId, voteType);
        return Map.of("message", "투표가 완료되었습니다.");
    }

    /**
     * 게시물 추천/비추천 취소
     * DELETE /api/posts/{postId}/vote
     */
    @DeleteMapping("/{postId}/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unvotePost(
            @LoginUser Long userId,
            @PathVariable Long postId
    ) {
        postService.unvotePost(postId, userId);
    }

    /**
     * 게시물의 댓글 목록 조회
     * GET /api/posts/{postId}/comments
     */
    @GetMapping("/{postId}/comments")
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }
}
