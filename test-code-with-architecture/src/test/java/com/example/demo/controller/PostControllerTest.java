package com.example.demo.controller;

import com.example.demo.post.domain.PostUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
  @Sql(value = "/sql/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostControllerTest {
  @Autowired
  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void 사용자는_게시물을_단건_조회할_수_있다() throws Exception {
    mockMvc.perform(get("/api/posts/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").value("test1"))
      .andExpect(jsonPath("$.writer.id").value(1))
      .andExpect(jsonPath("$.writer.nickname").value("test"))
      .andExpect(jsonPath("$.writer.email").value("test@test.com"));
  }

  @Test
  void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() throws Exception {
    mockMvc.perform(get("/api/posts/123432423"))
      .andExpect(status().isNotFound())
      .andExpect(content().string("Posts에서 ID 123432423를 찾을 수 없습니다."));
  }

  @Test
  void 사용자는_게시물을_수정할_수_있다() throws Exception {
    // given
    PostUpdate postUpdate = PostUpdate.builder()
      .content("hello222")
      .build();

    // when
    // then
    mockMvc.perform(put("/api/posts/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postUpdate))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").isNumber())
      .andExpect(jsonPath("$.content").value("hello222"))
      .andExpect(jsonPath("$.writer.id").value(1))
      .andExpect(jsonPath("$.writer.nickname").value("test"))
      .andExpect(jsonPath("$.writer.email").value("test@test.com"));
  }
}
