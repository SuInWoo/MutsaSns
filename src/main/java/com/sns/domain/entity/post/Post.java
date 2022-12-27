package com.sns.domain.entity.post;

import com.sns.domain.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PostEntity")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
