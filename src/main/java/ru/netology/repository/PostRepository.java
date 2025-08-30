package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {

  private final List<Post> posts = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong(0);

  public List<Post> all() {
    return List.copyOf(posts);
  }

  public Optional<Post> getById(long id) {
    return posts.stream().filter(post->post.getId()==id).findFirst();
  }

  public Post save(Post post) {

    if (post.getId() == 0) {
      // Creating a new post
      long newId = idCounter.incrementAndGet();  // Atomic increment
      post.setId(newId);
      posts.add(post);
      return post;
    } else {
      // Updating an existing post
      Optional<Post> existingPostOptional = getById(post.getId());
      if (existingPostOptional.isPresent()) {
        Post existingPost = existingPostOptional.get();
        existingPost.setContent(post.getContent()); // Update content
        return existingPost;
      } else {
        throw new NotFoundException("Post with id " + post.getId() + " not found");
      }
    }
  }
  public void removeById(long id){

    posts.removeIf(post -> post.getId()==id);
  }
}
