package com.microel.speedtest.services.security;

import org.springframework.stereotype.Component;

import graphql.kickstart.execution.subscriptions.SubscriptionSession;
import graphql.kickstart.execution.subscriptions.apollo.ApolloSubscriptionConnectionListener;
import graphql.kickstart.execution.subscriptions.apollo.OperationMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationConnectionListener implements ApolloSubscriptionConnectionListener {

    private JwtAuthenticationProvider jwtAuthenticationProvider;

    public AuthenticationConnectionListener(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    private String parseToken(OperationMessage operationMessage) {
        String tokenHeader = operationMessage.getPayload().toString();
        String token = tokenHeader.substring(1, tokenHeader.length() - 1).replace("Authorization=Bearer ", "");
        return token;
    }

    @Override
    public void onConnect(SubscriptionSession session, OperationMessage operationMessage) {
        log.info("onConnect: {}", parseToken(operationMessage));
        if (!jwtAuthenticationProvider.validate(parseToken(operationMessage))) {
            session.close("Invalid token");
        }
    }

    // @Override
    // public void onDisconnect(String clientId) {
    // log.info("onDisconnect: {}", clientId);
    // }

}
