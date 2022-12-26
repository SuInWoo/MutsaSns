package com.sns.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sns.domain.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long postId;
    private String title;
    private String body;
    private String userName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;


    public static PostDto of(Post post, String userName) {
        return PostDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(userName)
                .build();
    }


    public static Page<PostDto> toDtoList(Page<Post> postEntities) {
        Page<PostDto> postDtoList = postEntities.map(m -> PostDto.builder()
                .postId(m.getId())
                .title(m.getTitle())
                .body(m.getBody())
                .userName(m.getUser().getUserName())
                .createdAt(m.getCreatedAt())
                .build());
        return postDtoList;
    }
}

