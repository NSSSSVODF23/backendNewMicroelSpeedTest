package com.microel.speedtest.services.resolvers.mutation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microel.speedtest.repositories.UserRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.User;

import graphql.kickstart.tools.GraphQLMutationResolver;

@Service
public class UserMutation implements GraphQLMutationResolver {

    private final UserRepositoryDispatcher userRepositoryDispatcher;

    public UserMutation(UserRepositoryDispatcher userRepositoryDispatcher) {
        this.userRepositoryDispatcher = userRepositoryDispatcher;
    }

    public User createUser(User user) {
        return userRepositoryDispatcher.insert(user);
    }

    public User editUser(User user) {
        return userRepositoryDispatcher.update(user);
    }

    public User deleteUser(Long userId) {
        return userRepositoryDispatcher.deleteUser(userId);
    }
}
