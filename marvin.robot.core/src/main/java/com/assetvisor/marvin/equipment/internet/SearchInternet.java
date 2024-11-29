package com.assetvisor.marvin.equipment.internet;

import com.assetvisor.marvin.equipment.internet.SearchInternet.Query;
import com.assetvisor.marvin.equipment.internet.SearchInternet.Result;
import com.assetvisor.marvin.equipment.watch.LookAtWatch.Request;
import com.assetvisor.marvin.equipment.watch.LookAtWatch.Response;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import java.time.LocalDateTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SearchInternet implements EnvironmentFunction<Query, Result> {

    private static final Log LOG = LogFactory.getLog(SearchInternet.class);
    private final ForSearchingInternet forSearchingInternet;

    public SearchInternet(ForSearchingInternet forSearchingInternet) {
        this.forSearchingInternet = forSearchingInternet;
    }

    @Override
    public String name() {
        return "SearchInternet";
    }

    @Override
    public String description() {
        return "This function is used for searching the internet. Make the response simple and easy to understand.";
    }

    @Override
    public Class<?> inputType() {
        return Query.class;
    }

    @Override
    public Result apply(Query request) {
        String result = forSearchingInternet.search(request.query());
        return new Result(result);
    }

    public record Query(String query) {}
    public record Result(String result) {}
}
