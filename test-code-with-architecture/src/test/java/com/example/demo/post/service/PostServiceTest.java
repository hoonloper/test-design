package com.example.demo.post.service;

import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostServiceTest {
  private PostServiceImpl postServiceImpl;

  @BeforeEach
  void init() {
    FakePostRepository fakePostRepository = new FakePostRepository();
    FakeUserRepository fakeUserRepository = new FakeUserRepository();

    this.postServiceImpl = PostServiceImpl.builder()
      .postRepository(fakePostRepository)
      .userRepository(fakeUserRepository)
      .clockHolder(new TestClockHolder(1678530673958L))
      .build();

    User writer =
      User.builder()
        .id(1L)
        .email("test@test.com")
        .nickname("test")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(0L)
        .build();
    User writer2 =
      User.builder()
        .id(2L)
        .email("test2@test.com")
        .nickname("test2")
        .address("Busan")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.PENDING)
        .lastLoginAt(0L)
        .build();

    fakePostRepository.save(
      Post.builder()
        .id(1L)
        .content("test1")
        .createdAt(1678530673958L)
        .modifiedAt(0L)
        .writer(writer)
        .build()
    );
    fakeUserRepository.save(writer);
    fakeUserRepository.save(writer2);
  }
  @Test
  void getById_는_존재하는_게시물을_내려준다() {
    // given
    // when
    Post result = postServiceImpl.getById(1);

    // then
    assertThat(result.getContent()).isEqualTo("test1");
    assertThat(result.getWriter().getEmail()).isEqualTo("test@test.com");
  }

  @Test
  void postCreate_를_이용하여_게시물을_생성할_수_있다() {
    // given
    PostCreate postCreate = PostCreate.builder()
      .writerId(1)
      .content("testtest")
      .build();

    // when
    Post result = postServiceImpl.create(postCreate);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getContent()).isEqualTo("testtest");
    assertThat(result.getCreatedAt()).isEqualTo(1678530673958L);
    assertThat(result.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

  @Test
  void postUpdate_를_이용하여_게시물을_수정할_수_있다() {
    // given
    PostUpdate postUpdate = PostUpdate.builder()
      .content("test!")
      .build();

    // when
    postServiceImpl.update(1, postUpdate);

    // then
    Post postEntity = postServiceImpl.getById(1);
    assertThat(postEntity.getId()).isNotNull();
    assertThat(postEntity.getContent()).isEqualTo("test!");
    assertThat(postEntity.getModifiedAt()).isEqualTo(1678530673958L);
  }
}
