package com.assetvisor.marvin.robot.domain.functions;

import com.assetvisor.marvin.robot.domain.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.functions.Clock.Request;
import com.assetvisor.marvin.robot.domain.functions.Clock.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class Clock implements EnvironmentFunction<Request, Response> {

    private final Log LOG = LogFactory.getLog(getClass());
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String name() {
        return "Clock";
    }

    @Override
    public String description() {
        return "This function is used for getting current date and time";
    }

    @Override
    public Response apply(Request request) {
        LOG.info("Clock function invoked");
        return new Response("Date and time is now: " + sdf.format(new Date()));
    }

    public record Request(String input) {}
    public record Response(String dateAndTime) {}

}
