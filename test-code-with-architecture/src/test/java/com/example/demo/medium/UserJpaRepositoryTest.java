package com.example.demo.medium;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
public class UserJpaRepositoryTest {
  @Autowired
  private UserJpaRepository userRepository;
  @Test
  void UserRepository_가_제대로_연결되었다() {
    // given
    UserEntity userEntity = new UserEntity();
    userEntity.setId(1L);
    userEntity.setEmail("test@test.com");
    userEntity.setAddress("Seoul");
    userEntity.setNickname("nickname");
    userEntity.setStatus(UserStatus.ACTIVE);
    userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // when
    UserEntity result = userRepository.save(userEntity);

    // then
    assertThat(result).isNotNull();
  }

  @Test
  void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
    Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

    // then
    assertThat(result).isPresent();
  }

  @Test
  void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
    Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
    Optional<UserEntity> result = userRepository.findByEmailAndStatus("test@test.com", UserStatus.ACTIVE);

    // then
    assertThat(result).isPresent();
  }

  @Test
  void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
    Optional<UserEntity> result = userRepository.findByEmailAndStatus("test@test.com", UserStatus.PENDING);

    // then
    assertThat(result).isEmpty();
  }
}
