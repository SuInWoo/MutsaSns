package com.sns.controller;

import com.sns.domain.Response;
import com.sns.domain.dto.UserDto;
import com.sns.domain.dto.UserJoinReq;
import com.sns.domain.dto.UserJoinRes;
import com.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinRes> join(@RequestBody UserJoinReq joinReq){
        UserDto userDto = userService.join(joinReq);
        return Response.success(new UserJoinRes(userDto.getId(), userDto.getUserName(), userDto.getUserRole()));
    }
}
