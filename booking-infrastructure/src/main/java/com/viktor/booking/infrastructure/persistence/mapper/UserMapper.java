package com.viktor.booking.infrastructure.persistence.mapper;

import com.viktor.booking.domain.model.User;
import com.viktor.booking.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toNewEntity(User user) {
        if (user == null) {
            return null;
        }

        return new UserEntity(
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole()
        );
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole()
        );
    }
}
