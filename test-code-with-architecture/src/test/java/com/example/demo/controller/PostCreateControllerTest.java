package com.example.demo.controller;

import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.UserCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
  @Sql(value = "/sql/post-create_controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostCreateControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private JavaMailSender mailSender;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void 사용자는_게시물을_작성할_수_있다() throws Exception {
    // given
    PostCreateDto postCreateDto = PostCreateDto.builder()
      .writerId(1)
      .content("hello")
      .build();

    // when
    // then
    mockMvc.perform(post("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postCreateDto))
      )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNumber())
      .andExpect(jsonPath("$.content").value("hello"))
      .andExpect(jsonPath("$.writer.id").value(1))
      .andExpect(jsonPath("$.writer.nickname").value("test"))
      .andExpect(jsonPath("$.writer.email").value("test@test.com"));
  }
}
