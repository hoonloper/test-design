package com.example.demo.medium;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
  @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostServiceTest {
  @Autowired
  private PostService postService;

  @Test
  void getById_는_존재하는_게시물을_내려준다() {
    // given
    // when
    Post result = postService.getById(1);

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
    Post result = postService.create(postCreate);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getContent()).isEqualTo("testtest");
    assertThat(result.getCreatedAt()).isGreaterThan(0);
    // assertThat(result.getCertificationCode()).isEqualTo("T.T");
  }

  @Test
  void postUpdate_를_이용하여_게시물을_수정할_수_있다() {
    // given
    PostUpdate postUpdate = PostUpdate.builder()
      .content("test!")
      .build();

    // when
    postService.update(1, postUpdate);

    // then
    Post postEntity = postService.getById(1);
    assertThat(postEntity.getId()).isNotNull();
    assertThat(postEntity.getContent()).isEqualTo("test!");
    assertThat(postEntity.getModifiedAt()).isGreaterThan(0);
  }
}
