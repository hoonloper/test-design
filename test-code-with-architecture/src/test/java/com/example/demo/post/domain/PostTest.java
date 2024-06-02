package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {
  @Test
  public void PostCreate로_게시물을_만들_수_있다() {
    // given
    PostCreate postCreate = PostCreate.builder()
      .writerId(1)
      .content("hello")
      .build();
    User writer = User.builder()
      .email("test@test.com")
      .address("Busan")
      .nickname("test")
      .status(UserStatus.ACTIVE)
      .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
      .build();

    // when
    Post post = Post.from(writer, postCreate, new TestClockHolder(1678530679999L));

    // then
    assertThat(post.getContent()).isEqualTo("hello");
    assertThat(post.getCreatedAt()).isEqualTo(1678530679999L);
    assertThat(post.getWriter().getEmail()).isEqualTo("test@test.com");
    assertThat(post.getWriter().getNickname()).isEqualTo("test");
    assertThat(post.getWriter().getAddress()).isEqualTo("Busan");
    assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

  @Test
  public void PostUpdate로_게시물을_수정할_수_있다() {
    // given
    PostUpdate postUpdate = PostUpdate.builder()
      .content("hello")
      .build();
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
      .createdAt(1678530679908L)
      .build();

    // when
    post = post.update(postUpdate, new TestClockHolder(1678530679999L));

    // then
    assertThat(post.getContent()).isEqualTo("hello");
    assertThat(post.getModifiedAt()).isEqualTo(1678530679999L);
  }
}
