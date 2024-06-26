package com.example.demo.mock;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {
  // 소형 테스트는 단일 스레드에서 돌아가므로 동시성 이슈를 생각하지 않아도 됨
  // 다만 병렬 처리되는 테스트를 진행할 때는 아래처럼 동시성에 안전하게 처리하는 것이 맞음
  private final AtomicLong autoGeneratedId = new AtomicLong(0);
  private final List<User> data = Collections.synchronizedList(new ArrayList<>());

  @Override
  public User getById(long id) {
    return findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
  }

  @Override
  public Optional<User> findById(long id) {
    return data.stream().filter(item -> item.getId().equals(id)).findAny();
  }

  @Override
  public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
    return data.stream().filter(item -> item.getId().equals(id) && item.getStatus().equals(userStatus)).findAny();
  }

  @Override
  public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
    return data.stream().filter(item -> item.getEmail().equals(email) && item.getStatus().equals(userStatus)).findAny();
  }

  @Override
  public User save(User user) {
    if (user.getId() == null || user.getId() == 0) {
      User newUser = User.builder()
        .id(autoGeneratedId.incrementAndGet())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .address(user.getAddress())
        .certificationCode(user.getCertificationCode())
        .status(user.getStatus())
        .lastLoginAt(user.getLastLoginAt())
        .build();
      data.add(newUser);
      return newUser;
    } else {
      data.removeIf(item -> Objects.equals(item.getId(), user.getId()));
      data.add(user);
      return user;
    }
  }
}
