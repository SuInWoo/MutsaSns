package com.sns.service;

import com.sns.domain.dto.post.PostCreateReq;
import com.sns.domain.dto.post.PostDto;
import com.sns.domain.dto.post.PostUpdateReq;
import com.sns.domain.entity.post.Post;
import com.sns.domain.entity.user.User;
import com.sns.exception.AppException;
import com.sns.exception.ErrorCode;
import com.sns.repository.PostRepo;
import com.sns.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepo userRepo;
    private final PostRepo postRepo;

    //Create
    public PostDto write(PostCreateReq createReq, String name) {

        User user = userRepo.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s는 존재하지 않습니다.", name)));

        Post savedPost = postRepo.save(Post.of(createReq.getTitle(), createReq.getBody(), user));

        PostDto postDto = PostDto.of(savedPost, user.getUserName());

        return postDto;
    }

    //Read
    public PostDto get(Long postId) {

        //Post 존재 X
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, String.format("%d 포스트가 존재하지 않습니다.", postId)));

        return PostDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }

    public Page<PostDto> getAllPost(Pageable pageable) {
        Page<Post> posts = postRepo.findAll(pageable);
        Page<PostDto> postDtos = PostDto.toDtoList(posts);
        return postDtos;
    }

    //Update
    public PostDto update(Long postId, PostUpdateReq updateReq, String name) {
        //Post 존재 X
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, String.format("%d 포스트가 존재하지 않습니다.", postId)));

        //User 존재 X
        User user = userRepo.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s는 존재하지 않습니다.", name)));

        if (!post.getUser().getUserName().equals(name)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, String.format("%s에게 %d에 대한 권한이 없습니다.", name, postId));
        }

        post.setTitle(updateReq.getTitle());
        post.setBody(updateReq.getBody());
        post.setLastModifiedAt(LocalDateTime.now());

        Post savedPost = postRepo.saveAndFlush(post);

        return PostDto.of(savedPost, user.getUserName());
    }

    //Delete
    public PostDto delete(Long postId, String name) {
        //Post 존재 X
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, String.format("%d 포스트가 존재하지 않습니다.", postId)));

        //User 존재 X
        User user = userRepo.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s는 존재하지 않습니다.", name)));

        if (!post.getUser().getUserName().equals(name)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, String.format("%s에게 %d에 대한 권한이 없습니다.", name, postId));
        }

        postRepo.delete(post);

        return PostDto.of(post, user.getUserName());
    }


}
