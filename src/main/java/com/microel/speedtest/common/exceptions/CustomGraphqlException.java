package com.microel.speedtest.common.exceptions;

import java.util.List;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomGraphqlException extends RuntimeException implements GraphQLError {

    public CustomGraphqlException(String message) {
        super(message);
    }

    @Override
    public ErrorClassification getErrorType() {
        return null;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

}
