package org.example.medisched.repository;

import org.example.medisched.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    // Custom method to find a blog post by its slug
    Optional<BlogPost> findBySlug(String slug);
}
