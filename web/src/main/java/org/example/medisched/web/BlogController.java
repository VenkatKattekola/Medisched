package org.example.medisched.web;

import org.example.medisched.entity.BlogPost;
import org.example.medisched.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import PreAuthorize
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime; // Ensure this is imported for setting publishDate
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600) // maxAge for CORS pre-flight requests
@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired
    private BlogPostRepository blogPostRepository;

    /**
     * Get all blog posts. Accessible by anyone.
     * @return List of all blog posts.
     */
    @GetMapping("/articles")
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    /**
     * Get a single blog post by its slug. Accessible by anyone.
     * @param slug The URL-friendly identifier of the blog post.
     * @return The blog post if found, otherwise 404 Not Found.
     */
    @GetMapping("/articles/{slug}")
    public ResponseEntity<BlogPost> getBlogPostBySlug(@PathVariable String slug) {
        Optional<BlogPost> blogPost = blogPostRepository.findBySlug(slug);
        if (blogPost.isPresent()) {
            return ResponseEntity.ok(blogPost.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog post not found with slug: " + slug);
        }
    }

    /**
     * Create a new blog post. Requires ADMIN role.
     * @param blogPost The blog post entity to save.
     * @return The created blog post.
     */
    @PostMapping("/articles")
    @PreAuthorize("hasRole('ADMIN')") // Only ADMINs can create
    public ResponseEntity<BlogPost> createBlogPost(@RequestBody BlogPost blogPost) {
        // Ensure publishDate is set if not provided in request (or validate it)
        if (blogPost.getPublishDate() == null) {
            blogPost.setPublishDate(LocalDateTime.now());
        }
        BlogPost savedBlogPost = blogPostRepository.save(blogPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBlogPost);
    }

    /**
     * Update an existing blog post by its slug. Requires ADMIN role.
     * @param slug The slug of the blog post to update.
     * @param updatedBlogPost The updated blog post data.
     * @return The updated blog post if found, otherwise 404 Not Found.
     */
    @PutMapping("/articles/{slug}")
    @PreAuthorize("hasRole('ADMIN')") // Only ADMINs can update
    public ResponseEntity<BlogPost> updateBlogPost(@PathVariable String slug, @RequestBody BlogPost updatedBlogPost) {
        Optional<BlogPost> existingBlogPostOptional = blogPostRepository.findBySlug(slug);

        if (existingBlogPostOptional.isPresent()) {
            BlogPost existingBlogPost = existingBlogPostOptional.get();
            // Update fields from updatedBlogPost to existingBlogPost
            existingBlogPost.setTitle(updatedBlogPost.getTitle());
            existingBlogPost.setAuthor(updatedBlogPost.getAuthor());
            existingBlogPost.setContent(updatedBlogPost.getContent());
            existingBlogPost.setPublishDate(updatedBlogPost.getPublishDate() != null ? updatedBlogPost.getPublishDate() : existingBlogPost.getPublishDate());
            // Note: Slug is generally not updated after creation as it's part of the URL path

            BlogPost savedBlogPost = blogPostRepository.save(existingBlogPost);
            return ResponseEntity.ok(savedBlogPost);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog post not found with slug: " + slug);
        }
    }

    /**
     * Delete a blog post by its slug. Requires ADMIN role.
     * @param slug The slug of the blog post to delete.
     * @return 204 No Content if successful, otherwise 404 Not Found.
     */
    @DeleteMapping("/articles/{slug}")
    @PreAuthorize("hasRole('ADMIN')") // Only ADMINs can delete
    public ResponseEntity<Void> deleteBlogPost(@PathVariable String slug) {
        Optional<BlogPost> blogPostOptional = blogPostRepository.findBySlug(slug);
        if (blogPostOptional.isPresent()) {
            blogPostRepository.delete(blogPostOptional.get());
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog post not found with slug: " + slug);
        }
    }
}
