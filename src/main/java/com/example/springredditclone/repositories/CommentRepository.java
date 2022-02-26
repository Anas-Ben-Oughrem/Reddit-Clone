package com.example.springredditclone.repositories;

import com.example.springredditclone.entities.Comment;
import com.example.springredditclone.entities.Post;
import com.example.springredditclone.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
