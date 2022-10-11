package com.microel.speedtest.services.resolvers.mutation;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.exceptions.CustomGraphqlException;
import com.microel.speedtest.common.models.updateprovides.ComplaintUpdateProvider;
import com.microel.speedtest.repositories.ComplaintRepositoryDispatcher;
import com.microel.speedtest.repositories.UserRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.Complaint;
import com.microel.speedtest.repositories.entities.User;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class ComplaintMutation implements GraphQLMutationResolver {

    private final ComplaintRepositoryDispatcher complaintRepositoryDispatcher;
    private final UserRepositoryDispatcher userRepositoryDispatcher;
    private final Sinks.Many<ComplaintUpdateProvider> updateComplaintSink;


    public ComplaintMutation(ComplaintRepositoryDispatcher complaintRepositoryDispatcher, UserRepositoryDispatcher userRepositoryDispatcher, Sinks.Many<ComplaintUpdateProvider> updateComplaintSink) {
        this.complaintRepositoryDispatcher = complaintRepositoryDispatcher;
        this.userRepositoryDispatcher = userRepositoryDispatcher;
        this.updateComplaintSink = updateComplaintSink;
    }

    public boolean doProcessedComplaint(Long id, String username) {
        final User user = userRepositoryDispatcher.findByUsername(username);
        if (user == null) throw new CustomGraphqlException("Пользователь не найден");
        updateComplaintSink.tryEmitNext(new ComplaintUpdateProvider(ListMutationTypes.UPDATE, complaintRepositoryDispatcher.doProcessed(id,user)));
        return true;
    }
}
