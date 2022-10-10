package com.microel.speedtest.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.exceptions.CustomGraphqlException;
import com.microel.speedtest.common.models.updateprovides.UserUpdateProvider;
import com.microel.speedtest.repositories.entities.User;
import com.microel.speedtest.repositories.interfaces.UserRepository;

import reactor.core.publisher.Sinks;

@Component
public class UserRepositoryDispatcher {
    private final UserRepository userRepository;

    private final Sinks.Many<UserUpdateProvider> updateUserSink;

    public UserRepositoryDispatcher(UserRepository userRepository, Sinks.Many<UserUpdateProvider> updateUserSink) {
        this.userRepository = userRepository;
        this.updateUserSink = updateUserSink;
    }

    public User save(User user) {
        final User found = userRepository.findByUsername(user.getUsername());
        if (user.getUserId() == null && found != null) {
            return found;
        }
        user.setCreated(Timestamp.from(Instant.now()));
        return userRepository.save(user);
    }

    public User insert(User user) {
        final User found = userRepository.findByUsername(user.getUsername());
        if (user.getUserId() != null || found != null)
            throw new CustomGraphqlException("Пользователь с таким логином уже существует");
        user.setCreated(Timestamp.from(Instant.now()));
        final User saved = userRepository.save(user);
        updateUserSink.tryEmitNext(new UserUpdateProvider(ListMutationTypes.ADD, saved));
        return saved;
    }

    public User update(User user) {
        if (user.getUserId() == null)
            throw new CustomGraphqlException("Запрос на редактирования пользователя без идентификатора");
        if (userRepository.existsById(user.getUserId())) {
            final User saved = userRepository.save(user);
            updateUserSink.tryEmitNext(new UserUpdateProvider(ListMutationTypes.UPDATE, saved));
            return saved;
        } else {
            throw new CustomGraphqlException("Попытка отредактировать не существующего пользователя");
        }
    }

    public void updateLoginTime(User user) {
        user.setLastLogin(Timestamp.from(Instant.now()));
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User deleteUser(Long userId) {
        final User found = userRepository.findById(userId).orElse(null);
        if (found == null)
            throw new CustomGraphqlException("Попытка удалить не существующего пользователя");
        userRepository.deleteById(userId);
        updateUserSink.tryEmitNext(new UserUpdateProvider(ListMutationTypes.DELETE, found));
        return found;
    }

    public Boolean isLoginExist(String login) {
        return userRepository.existsByUsername(login);
    }
}
