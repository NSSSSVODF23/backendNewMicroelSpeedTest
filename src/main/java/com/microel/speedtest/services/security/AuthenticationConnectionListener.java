package com.microel.speedtest.services.security;

import org.springframework.stereotype.Component;

import graphql.kickstart.execution.subscriptions.SubscriptionSession;
import graphql.kickstart.execution.subscriptions.apollo.ApolloSubscriptionConnectionListener;
import graphql.kickstart.execution.subscriptions.apollo.OperationMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationConnectionListener implements ApolloSubscriptionConnectionListener {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public AuthenticationConnectionListener(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    private String parseToken(OperationMessage operationMessage) {
        String tokenHeader = operationMessage.getPayload().toString();
        return tokenHeader.substring(1, tokenHeader.length() - 1).replace("Authorization=Bearer ", "");
    }

    @Override
    public void onConnect(SubscriptionSession session, OperationMessage operationMessage) {
        String token = parseToken(operationMessage);
        if (!jwtAuthenticationProvider.validate(token)) {
            if(session.isOpen()) session.close("1006");
        }
    }

    // @Override
    // public void onDisconnect(String clientId) {
    // log.info("onDisconnect: {}", clientId);
    // }

}
