package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
  @Test
  public void UserCreate_객체로_생성할_수_있다() {
    // given
    UserCreate userCreate = UserCreate.builder()
      .nickname("test")
      .email("test@test.com")
      .address("Pangyo")
      .build();

    // when
    User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));

    // then
    assertThat(user.getId()).isNull();
    assertThat(user.getEmail()).isEqualTo("test@test.com");
    assertThat(user.getNickname()).isEqualTo("test");
    assertThat(user.getAddress()).isEqualTo("Pangyo");
    assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

  @Test
  public void UserUpdate_객체로_수정할_수_있다() {
    // given
    User user = User.builder()
      .id(1L)
      .email("test@test.com")
      .address("Busan")
      .nickname("test")
      .lastLoginAt(100L)
      .status(UserStatus.ACTIVE)
      .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
      .build();
    UserUpdate userUpdate = UserUpdate.builder()
      .nickname("test222")
      .address("Pangyo")
      .build();

    // when
    user = user.update(userUpdate);

    // then
    assertThat(user.getId()).isEqualTo(1L);
    assertThat(user.getEmail()).isEqualTo("test@test.com");
    assertThat(user.getNickname()).isEqualTo("test222");
    assertThat(user.getAddress()).isEqualTo("Pangyo");
    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    assertThat(user.getLastLoginAt()).isEqualTo(100L);
  }

  @Test
  public void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
    // given
    User user = User.builder()
      .id(1L)
      .email("test@test.com")
      .address("Busan")
      .nickname("test")
      .lastLoginAt(100L)
      .status(UserStatus.ACTIVE)
      .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
      .build();

    // when
    user = user.login(new TestClockHolder(1000));

    // then
    assertThat(user.getLastLoginAt()).isEqualTo(1000);
  }

  @Test
  public void 유효한_인증_코드로_계정을_활성화_할_수_있다() {
    // given
    User user = User.builder()
      .id(1L)
      .email("test@test.com")
      .address("Busan")
      .nickname("test")
      .lastLoginAt(100L)
      .status(UserStatus.PENDING)
      .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
      .build();

    // when
    user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // then
    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  public void 잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() {
    // given
    User user = User.builder()
      .id(1L)
      .email("test@test.com")
      .address("Busan")
      .nickname("test")
      .lastLoginAt(100L)
      .status(UserStatus.PENDING)
      .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
      .build();

    // when
    // then
    assertThatThrownBy(() -> {
      user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
    }).isInstanceOf(CertificationCodeNotMatchedException.class);

  }
}
