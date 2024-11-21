package com.assetvisor.marvin.robot.domain.watch;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.watch.LookAtWatch.Request;
import com.assetvisor.marvin.robot.domain.watch.LookAtWatch.Response;
import java.time.LocalDateTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LookAtWatch implements EnvironmentFunction<Request, Response> {

    private final Log LOG = LogFactory.getLog(getClass());

    @Override
    public String name() {
        return "LookAtWatch";
    }

    @Override
    public String description() {
        return "This function is used for getting current date and time from your watch";
    }

    @Override
    public Class<?> inputType() {
        return Request.class;
    }

    @Override
    public Response apply(Request request) {
        LocalDateTime now = LocalDateTime.now();
        LOG.info("Watch looked at: " + now);
        return new Response(now);
    }

    public record Request(String input) {}
    public record Response(LocalDateTime localDateTime) {}

}
