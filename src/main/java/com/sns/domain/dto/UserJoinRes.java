package com.sns.domain.dto;

import com.sns.domain.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserJoinRes {
    private Long userId;
    private String userName;
    private UserRole userRole;
}
