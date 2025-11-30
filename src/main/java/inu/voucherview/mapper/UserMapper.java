package inu.voucherview.mapper;

import inu.voucherview.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 카카오 ID로 유저 조회
    User findByUserId(Long userId);

    // 유저 생성 (회원가입)
    void insertUser(User user);
}