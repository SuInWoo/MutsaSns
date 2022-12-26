package com.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.domain.dto.user.UserDto;
import com.sns.domain.dto.user.UserJoinReq;
import com.sns.domain.dto.user.UserLoginReq;
import com.sns.exception.AppException;
import com.sns.exception.ErrorCode;
import com.sns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    /*
        1. 회원가입
         - 성공
         - 실패
          -- (userName 중복)
    */

    @Test
    @WithMockUser
    void 회원가입성공() throws Exception {
        //요청
        UserJoinReq req = UserJoinReq.builder()
                .userName("userNameTest")
                .password("passwordTest")
                .build();

        when(userService.join(any()))
                .thenReturn(UserDto.builder()
                        .id(0L)
                        .userName(req.getUserName())
                        .password(req.getPassword())
                        .build());

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.userId").value(0L))
                .andExpect(jsonPath("$.result.userName").value(req.getUserName()));
    }

    @Test
    @WithMockUser
    void 회원가입실패_유저이름중복() throws Exception {
        //요청
        UserJoinReq req = UserJoinReq.builder()
                .userName("userNameTest")
                .password("passwordTest")
                .build();

        when(userService.join(any()))
                .thenThrow(new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage()));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getStatus().value()))
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("DUPLICATED_USER_NAME"))
                .andExpect(jsonPath("$.result.message").value("UserName이 중복됩니다."));
    }

    /*
        2. 로그인
         - 성공
         - 실패
          -- (userName 없음)
          -- (password 틀림)
     */

    @Test
    @WithMockUser
    void 로그인성공() throws Exception {
        // 요청
        UserLoginReq req = UserLoginReq.builder()
                .userName("userNameTest")
                .password("passwordTest")
                .build();

        when(userService.login(any(), any()))
                .thenReturn("token");

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.jwt").value("token"));
    }

    @Test
    @WithMockUser
    void 로그인실패_유저이름없음() throws Exception {
        // 요청
        UserLoginReq req = UserLoginReq.builder()
                .userName("userNameTest")
                .password("passwordTest")
                .build();

        when(userService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getStatus().value()))
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("USERNAME_NOT_FOUND"))
                .andExpect(jsonPath("$.result.message").value("Not founded"));
    }

    @Test
    @WithMockUser
    void 로그인실패_비밀번호틀림() throws Exception {
        // 요청
        UserLoginReq req = UserLoginReq.builder()
                .userName("userNameTest")
                .password("passwordTest")
                .build();

        when(userService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage()));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getStatus().value()))
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PASSWORD"))
                .andExpect(jsonPath("$.result.message").value("패스워드가 잘못되었습니다."));
    }
}