package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyProfileResponseTest {
  @Test
  public void User로_응답을_생성할_수_있다() {
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
    MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

    // then
    assertThat(myProfileResponse.getId()).isEqualTo(1L);
    assertThat(myProfileResponse.getEmail()).isEqualTo("test@test.com");
    assertThat(myProfileResponse.getNickname()).isEqualTo("test");
    assertThat(myProfileResponse.getAddress()).isEqualTo("Busan");
    assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
    assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }
}
