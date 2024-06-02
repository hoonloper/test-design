package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostResponseTest {
  @Test
  public void Post로_응답을_생성할_수_있다() {
    // given
    User writer = User.builder()
      .email("test@test.com")
      .address("Busan")
      .nickname("test")
      .status(UserStatus.ACTIVE)
      .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
      .build();
    Post post = Post.builder()
      .writer(writer)
      .content("hello")
      .build();

    // when
    PostResponse postResponse = PostResponse.from(post);

    // then
    assertThat(postResponse.getContent()).isEqualTo("hello");
    assertThat(postResponse.getWriter().getEmail()).isEqualTo("test@test.com");
    assertThat(postResponse.getWriter().getNickname()).isEqualTo("test");
    assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
  }
}
