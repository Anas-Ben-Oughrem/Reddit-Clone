package com.example.springredditclone.services;

import com.example.springredditclone.dto.CommentsDto;
import com.example.springredditclone.entities.Comment;
import com.example.springredditclone.entities.NotificationEmail;
import com.example.springredditclone.entities.Post;
import com.example.springredditclone.entities.User;
import com.example.springredditclone.exceptions.PostNotFoundException;
import com.example.springredditclone.mappers.CommentMapper;
import com.example.springredditclone.repositories.CommentRepository;
import com.example.springredditclone.repositories.PostRepository;
import com.example.springredditclone.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private static final String POST_URL = "";

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(()-> new PostNotFoundException(commentDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentDto,post,authService.getCurrentUser());
        commentRepository.save(comment);

        String message =
                mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(),
                message));

    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()-> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
