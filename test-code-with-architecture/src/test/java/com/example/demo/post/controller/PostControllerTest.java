package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PostControllerTest {
  @Test
  void 사용자는_게시물을_단건_조회할_수_있다() {
    // given
    TestContainer testContainer = TestContainer.builder()
      .build();
    User user =User.builder()
      .id(1L)
      .email("test@test.com")
      .address("Busan")
      .nickname("test")
      .status(UserStatus.ACTIVE)
      .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
      .build();
    testContainer.postRepository.save(Post.builder()
        .id(1L)
        .content("hello")
        .writer(user)
        .createdAt(100L)
      .build()
    );

    // when
    ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1L);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getContent()).isEqualTo("hello");
    assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@test.com");
    assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
  }

  @Test
  void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() {
    TestContainer testContainer = TestContainer.builder()
      .build();

    assertThatThrownBy(() -> {
      testContainer.postController.getPostById(1L);
    }).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void 사용자는_게시물을_수정할_수_있다() {
    // given
    TestContainer testContainer = TestContainer.builder()
      .clockHolder(() -> 200L)
      .build();
    User user =User.builder()
      .id(1L)
      .email("test@test.com")
      .address("Busan")
      .nickname("test")
      .status(UserStatus.ACTIVE)
      .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
      .build();
    testContainer.postRepository.save(Post.builder()
      .id(1L)
      .content("hello")
      .writer(user)
      .createdAt(100L)
      .build()
    );

    // when
    ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1L, PostUpdate.builder()
        .content("bye")
      .build()
    );

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getContent()).isEqualTo("bye");
    assertThat(result.getBody().getWriter().getNickname()).isEqualTo("test");
    assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
    assertThat(result.getBody().getModifiedAt()).isEqualTo(200L);
  }
}
