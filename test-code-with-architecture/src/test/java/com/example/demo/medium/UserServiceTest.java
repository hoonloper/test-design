package com.example.demo.medium;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
  @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {
  @Autowired
  private UserService userService;
  @MockBean
  private JavaMailSender mailSender;

  @Test
  void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
    // given
    String email = "test@test.com";

    // when
    User result = userService.getByEmail(email);

    // then
    assertThat(result.getNickname()).isEqualTo("test");
  }

  @Test
  void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
    // given
    String email = "test2@test.com";

    // when
    // then
    assertThatThrownBy(() -> {
      userService.getByEmail(email);
    }).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
    // given
    // when
    User result = userService.getById(1);
    // then
    assertThat(result.getNickname()).isEqualTo("test");
  }

  @Test
  void getById는_PENDING_상태인_유저를_찾아올_수_없다() {
    // given
    // when
    // then
    assertThatThrownBy(() -> {
      userService.getById(2);
    }).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void userCreate_를_이용하여_유저를_생성할_수_있다() {
    // given
    UserCreate userCreate = UserCreate.builder()
      .email("test@test.com")
      .address("Busan")
      .nickname("testtest")
      .build();
    BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

    // when
    User result = userService.create(userCreate);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    // assertThat(result.getCertificationCode()).isEqualTo("T.T");
  }

  @Test
  void userUpdate_를_이용하여_유저를_수정할_수_있다() {
    // given
    UserUpdate userUpdate = UserUpdate.builder()
      .address("Incheon")
      .nickname("sssssss")
      .build();
    BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

    // when
    userService.update(1, userUpdate);

    // then
    User userEntity = userService.getById(1);
    assertThat(userEntity.getId()).isNotNull();
    assertThat(userEntity.getAddress()).isEqualTo("Incheon");
    assertThat(userEntity.getNickname()).isEqualTo("sssssss");
  }

  @Test
  void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
    // given
    // when
    userService.login(1);

    // then
    User userEntity = userService.getById(1);
    assertThat(userEntity.getLastLoginAt()).isGreaterThan(0);
    // assertThat(userEntity.getLastLoginAt()).isGreaterThan(0);
  }

  @Test
  void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
    // given
    // when
    userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

    // then
    User userEntity = userService.getById(2);
    assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
    // given
    // when
    // then
    assertThatThrownBy(() -> {
      userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");
    }).isInstanceOf(CertificationCodeNotMatchedException.class);
  }
}
