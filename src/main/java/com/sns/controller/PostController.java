package com.sns.controller;

import com.sns.domain.dto.Response;
import com.sns.domain.dto.post.*;
import com.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Slf4j
public class PostController {

    private final PostService postService;

    //CRUD

    //Create
    @PostMapping
    public Response<PostCreateRes>write(@RequestBody PostCreateReq createReq, @ApiIgnore Authentication authentication) {
        String name = authentication.getName();
        PostDto postDto = postService.write(createReq, name);
        return Response.success(new PostCreateRes("포스트 등록 완료", postDto.getId()));
    }

    //Read
    @GetMapping("/{postId}")
    public Response<PostDto> findById(@PathVariable Long postId) {
        return Response.success(postService.get(postId));
    }

    @GetMapping("")
    public Response<Page<PostDto>> list(Pageable pageable) {
        return Response.success(postService.getAllPost(pageable));
    }

    //Update
    @PutMapping("/{postId}")
    public Response<PostUpdateRes> update(@PathVariable Long postId, @RequestBody PostUpdateReq updateReq, @ApiIgnore Authentication authentication) {
        PostDto postDto = postService.update(postId, updateReq, authentication.getName());
        return Response.success(new PostUpdateRes("포스트 수정 완료", postDto.getId()));
    }

    //Delete
    @DeleteMapping("/{postId}")
    public Response<PostDeleteRes> delete(@PathVariable Long postId, @ApiIgnore Authentication authentication) {
        PostDto postDto = postService.delete(postId, authentication.getName());
        return Response.success(new PostDeleteRes("포스트 삭제 완료", postDto.getId()));
    }
}
