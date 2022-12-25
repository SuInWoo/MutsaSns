package com.sns.domain.entity.post;

import com.sns.domain.entity.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PostEntity")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Post of(String title, String body, User user) {
        Post post = new Post();
        post.setTitle(title);
        post.setBody(body);
        post.setUser(user);
        return post;
    }
}
