package com.sns.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateReq {
    private String title;
    private String body;
}
