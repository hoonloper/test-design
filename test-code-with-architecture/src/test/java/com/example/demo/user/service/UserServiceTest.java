package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceTest {
  private UserService userService;

  @BeforeEach
  void init() {
    FakeMailSender fakeMailSender = new FakeMailSender();
    FakeUserRepository fakeUserRepository = new FakeUserRepository();

    this.userService = UserService.builder()
      .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
      .clockHolder(new TestClockHolder(1000))
      .userRepository(fakeUserRepository)
      .certificationService(new CertificationService(fakeMailSender))
      .build();

    fakeUserRepository.save(
      User.builder()
        .id(1L)
        .email("test@test.com")
        .nickname("test")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(0L)
        .build()
    );
    fakeUserRepository.save(
      User.builder()
        .id(2L)
        .email("test2@test.com")
        .nickname("test2")
        .address("Busan")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.PENDING)
        .lastLoginAt(0L)
        .build()
    );
  }

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

    // when
    User result = userService.create(userCreate);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
     assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

  @Test
  void userUpdate_를_이용하여_유저를_수정할_수_있다() {
    // given
    UserUpdate userUpdate = UserUpdate.builder()
      .address("Incheon")
      .nickname("sssssss")
      .build();

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
    assertThat(userEntity.getLastLoginAt()).isEqualTo(1000);
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
