package com.sns.domain.dto;

import com.sns.domain.entity.User;
import com.sns.domain.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserJoinReq {

    private String userName;
    private String password;
    private UserRole userRole;

    public User toEntity(String password) {
        return User.builder()
                .userName(this.userName)
                .password(password)
                .userRole(this.userRole)
                .build();
    }
}
