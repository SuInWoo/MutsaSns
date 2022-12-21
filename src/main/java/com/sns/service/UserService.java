package com.sns.service;

import com.sns.domain.dto.UserDto;
import com.sns.domain.dto.UserJoinReq;
import com.sns.domain.entity.User;
import com.sns.exception.ErrorCode;
import com.sns.exception.UserException;
import com.sns.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;

//    @Value("${jwt.token.secret}")
//    private String secretKey;

    public UserDto join(UserJoinReq userJoinReq) {
        // 로직 - 회원 가입

        // 회원 userName(id) 중복 check
        // 중복이면 회원가입 x --> Exception(예외)발생
        // 있으면 에러 처리
        userRepo.findByUserName(userJoinReq.getUserName())
                .ifPresent(user -> {
                    throw new UserException(ErrorCode.DUPLICATED_USER_NAME,
                            String.format("UserName: %s", userJoinReq.getUserName()));
                });

        // 회원가입 .save()
        User savedUser = userRepo.save(userJoinReq.toEntity(encoder.encode(userJoinReq.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .password(savedUser.getPassword())
                .userRole(savedUser.getUserRole())
                .build();
    }
}
