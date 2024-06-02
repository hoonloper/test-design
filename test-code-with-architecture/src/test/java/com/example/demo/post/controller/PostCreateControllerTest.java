package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostCreateControllerTest {
  @Test
  void 사용자는_게시물을_작성할_수_있다() {
    // given
    TestContainer testContainer = TestContainer.builder()
      .clockHolder(() -> 1678530673958L)
      .build();
    testContainer.userRepository.save(User.builder()
      .id(1L)
      .email("test@test.com")
      .address("Busan")
      .nickname("test")
      .status(UserStatus.ACTIVE)
      .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
      .build());
    PostCreate postCreate = PostCreate.builder()
      .writerId(1L)
      .content("hello")
      .build();

    // when
    ResponseEntity<PostResponse> result = testContainer.postCreateController.create(postCreate);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getContent()).isEqualTo("hello");
    assertThat(result.getBody().getCreatedAt()).isEqualTo(1678530673958L);
    assertThat(result.getBody().getWriter().getNickname()).isEqualTo("test");
  }
}
