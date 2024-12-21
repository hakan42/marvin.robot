package com.assetvisor.marvin.toolkit.watch;

import com.assetvisor.marvin.robot.domain.tools.Tool;
import com.assetvisor.marvin.toolkit.watch.LookAtWatchTool.Request;
import com.assetvisor.marvin.toolkit.watch.LookAtWatchTool.Response;
import java.time.LocalDateTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LookAtWatchTool implements Tool<Request, Response> {

    private static final Log LOG = LogFactory.getLog(LookAtWatchTool.class);

    @Override
    public String name() {
        return "LookAtWatch";
    }

    @Override
    public String description() {
        return "This tool is used for getting current date and time from your watch";
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
