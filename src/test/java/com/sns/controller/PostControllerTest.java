package com.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.domain.dto.post.PostCreateReq;
import com.sns.domain.dto.post.PostDto;
import com.sns.exception.AppException;
import com.sns.exception.ErrorCode;
import com.sns.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    /*
        1. 등록(Create)
         - 성공
         - 실패
          -- (인증 실패 JWT !-> Bearer Token)
          -- (JWT 유효 기간 만료)
     */

    @Test
    @WithMockUser
    void 등록성공() throws Exception {
        PostCreateReq req = PostCreateReq.builder()
                .title("titleTest")
                .body("bodyTest")
                .build();

        when(postService.write(any(), any()))
                .thenReturn(PostDto.builder()
                        .postId(1L)
                        .build());

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").value("포스트 등록 완료"))
                .andExpect(jsonPath("$.result.postId").value(1L));
    }

    @Test
    @WithMockUser
    void 등록실패_인증실패() throws Exception {
        PostCreateReq req = PostCreateReq.builder()
                .title("titleTest")
                .body("bodyTest")
                .build();

        when(postService.write(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()))
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andExpect(jsonPath("$.result.message").value("사용자가 권한이 없습니다."));
    }

    @Test
    @WithMockUser
    void 등록실패_토큰유효기간만료() throws Exception {
        PostCreateReq req = PostCreateReq.builder()
                .title("titleTest")
                .body("bodyTest")
                .build();

        when(postService.write(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()))
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_TOKEN"))
                .andExpect(jsonPath("$.result.message").value("잘못된 토큰입니다."));
    }

    /*
        2. 읽기(Read)
         - 성공
     */

    @Test
    @WithMockUser
    void 읽기성공() throws Exception {
        PostDto postDto = PostDto.builder()
                .postId(1L)
                .title("titleTest")
                .body("bodyTest")
                .userName("userNameTest")
                .createdAt(LocalDateTime.now())
                .build();

        when(postService.get(any()))
                .thenReturn(postDto);

        mockMvc.perform(get(String.format("/api/v1/posts/%s", postDto.getPostId()))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.postId").value(postDto.getPostId()))
                .andExpect(jsonPath("$.result.title").value(postDto.getTitle()))
                .andExpect(jsonPath("$.result.body").value(postDto.getBody()))
                .andExpect(jsonPath("$.result.userName").value(postDto.getUserName()))
                .andExpect(jsonPath("$.result.createdAt").exists());

    }

    /*
        3. 수정(Update)
         - 성공
         - 실패
          -- (인증 실패)
          -- (작성자 불일치)
          -- (DB 에러)
     */

    /*
        4. 삭제(Delete)
         - 성공
         - 실패
          -- (인증실패)
          -- (작성자 불일치)
          -- (DB 에러)
     */


}