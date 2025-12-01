package inu.voucherview.service;

import inu.voucherview.domain.Post;
import inu.voucherview.domain.PostCategory;
import inu.voucherview.domain.PostVote;
import inu.voucherview.domain.User;
import inu.voucherview.dto.PostDto;
import inu.voucherview.exception.BusinessException;
import inu.voucherview.exception.ErrorCode;
import inu.voucherview.mapper.PostMapper;
import inu.voucherview.mapper.PostVoteMapper;
import inu.voucherview.mapper.UserMapper;
import inu.voucherview.response.PostListResponse;
import inu.voucherview.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostVoteMapper postVoteMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public Post createPost(Long userId, PostCategory category, String title, String content) {
        Post post = Post.builder()
                .userId(userId)
                .category(category)
                .title(title)
                .content(content)
                .upvoteCount(0)
                .downvoteCount(0)
                .commentCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        postMapper.insert(post);
        return post;
    }

    @Override
    public PostListResponse getPostList(int page, int limit, PostCategory category, String keyword, String sortBy) {
        if (page <= 0 || limit <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        int totalCount = postMapper.countWithFilters(category, keyword);
        Pagination pagination = new Pagination(page, limit, totalCount);

        List<Post> posts = postMapper.findWithFilters(pagination, category, keyword, sortBy);

        List<PostDto> postDtos = posts.stream()
                .map(post -> {
                    PostDto dto = new PostDto(post);
                    User user = userMapper.findByUserId(post.getUserId());
                    if (user != null) {
                        dto.setNickname(user.getNickname());
                    }
                    return dto;
                })
                .toList();

        return new PostListResponse(postDtos, pagination);
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        PostDto dto = new PostDto(post);
        User user = userMapper.findByUserId(post.getUserId());
        if (user != null) {
            dto.setNickname(user.getNickname());
        }

        return dto;
    }

    @Override
    @Transactional
    public void updatePost(Long postId, Long userId, PostCategory category, String title, String content) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        if (!post.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        post.changeCategory(category);
        post.changeTitle(title);
        post.changeContent(content);

        postMapper.update(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        if (!post.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        postMapper.delete(postId);
    }

    @Override
    @Transactional
    public void votePost(Long postId, Long userId, PostVote.VoteType voteType) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        PostVote vote = PostVote.builder()
                .postId(postId)
                .userId(userId)
                .voteType(voteType)
                .build();

        postVoteMapper.upsert(vote);

        // 투표 수 재계산
        int upvotes = postVoteMapper.countUpvotes(postId);
        int downvotes = postVoteMapper.countDownvotes(postId);
        postMapper.updateVoteCounts(postId, upvotes, downvotes);
    }

    @Override
    @Transactional
    public void unvotePost(Long postId, Long userId) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        postVoteMapper.delete(userId, postId);

        // 투표 수 재계산
        int upvotes = postVoteMapper.countUpvotes(postId);
        int downvotes = postVoteMapper.countDownvotes(postId);
        postMapper.updateVoteCounts(postId, upvotes, downvotes);
    }
}
