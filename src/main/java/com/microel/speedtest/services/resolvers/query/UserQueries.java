package com.microel.speedtest.services.resolvers.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microel.speedtest.repositories.RoleGroupRepositoryDispatcher;
import com.microel.speedtest.repositories.UserRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.RoleGroup;
import com.microel.speedtest.repositories.entities.User;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Service
public class UserQueries implements GraphQLQueryResolver {

    private final RoleGroupRepositoryDispatcher roleGroupRepositoryDispatcher;

    private final UserRepositoryDispatcher userRepositoryDispatcher;

    public UserQueries(RoleGroupRepositoryDispatcher roleGroupRepositoryDispatcher, UserRepositoryDispatcher userRepositoryDispatcher) {
        this.roleGroupRepositoryDispatcher = roleGroupRepositoryDispatcher;
        this.userRepositoryDispatcher = userRepositoryDispatcher;
    }

    public List<RoleGroup> getAllRoles() {
        return roleGroupRepositoryDispatcher.getAllRoles();
    }

    public List<User> getAllUsers() {
        return userRepositoryDispatcher.getAllUsers();
    }

    public Boolean validateLogin(String login) {
        return userRepositoryDispatcher.isLoginExist(login);
    }

    public User getUserByUsername(String username) {
        return userRepositoryDispatcher.findByUsername(username);
    }
}
