package com.assetvisor.marvin.robot.domain.relationships;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.relationships.AddFriendFunction.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddFriendFunction implements EnvironmentFunction<Request, Void> {

    private final Log LOG = LogFactory.getLog(getClass());
    private final ForAddingPerson forAddingFriend;

    public AddFriendFunction(ForAddingPerson forAddingFriend) {
        this.forAddingFriend = forAddingFriend;
    }

    @Override
    public String name() {
        return "AddFriendFunction";
    }

    @Override
    public String description() {
        return """
            This function is used for adding a friend to your relationships.
            """;
    }

    @Override
    public Class<?> inputType() {
        return Request.class;
    }

    @Override
    public Void apply(Request request) {
        Person friend = new Person(request.personName(), request.email(), Person.Relationship.FRIEND);
        forAddingFriend.addPerson(friend);
        LOG.info("Added friend: " + friend);
        return null;
    }

    public record Request(String personName, String email) {}
    public record Response(String personId) {}

}
