package inu.voucherview.service;

import inu.voucherview.domain.Comment;
import inu.voucherview.domain.User;
import inu.voucherview.dto.CommentDto;
import inu.voucherview.exception.BusinessException;
import inu.voucherview.exception.ErrorCode;
import inu.voucherview.mapper.CommentMapper;
import inu.voucherview.mapper.PostMapper;
import inu.voucherview.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public Comment createComment(Long postId, Long userId, String content) {
        // 게시물 존재 여부 확인
        if (postMapper.findById(postId) == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Comment comment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        commentMapper.insert(comment);

        // 게시물의 댓글 수 업데이트
        int commentCount = commentMapper.countByPostId(postId);
        postMapper.updateCommentCount(postId, commentCount);

        return comment;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentMapper.findByPostId(postId);

        return comments.stream()
                .map(comment -> {
                    CommentDto dto = new CommentDto(comment);
                    User user = userMapper.findByUserId(comment.getUserId());
                    if (user != null) {
                        dto.setNickname(user.getNickname());
                    }
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, Long userId, String content) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        comment.changeContent(content);
        commentMapper.update(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        Long postId = comment.getPostId();
        commentMapper.delete(commentId);

        // 게시물의 댓글 수 업데이트
        int commentCount = commentMapper.countByPostId(postId);
        postMapper.updateCommentCount(postId, commentCount);
    }
}
