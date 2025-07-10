package org.example.medisched.entity;

import jakarta.persistence.*;
        import java.time.LocalDateTime;

@Entity
@Table(name = "blog_posts") // Table name for blog posts
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Slug should be unique and non-null for URL
    private String slug; // URL-friendly identifier (e.g., "the-importance-of-hydration")

    @Column(nullable = false)
    private String title;

    @Column
    private String author; // Can be nullable

    @Column(columnDefinition = "TEXT", nullable = false) // Use TEXT for potentially long content
    private String content;

    @Column(name = "publish_date", nullable = false)
    private LocalDateTime publishDate;

    // Default constructor (required by JPA)
    public BlogPost() {
    }

    // Constructor with fields (optional, but good for testing/initialization)
    public BlogPost(String slug, String title, String author, String content, LocalDateTime publishDate) {
        this.slug = slug;
        this.title = title;
        this.author = author;
        this.content = content;
        this.publishDate = publishDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishDate=" + publishDate +
                '}';
    }
}
