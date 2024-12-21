package com.assetvisor.marvin.toolkit.internet;

import com.assetvisor.marvin.toolkit.internet.SearchInternetTool.Query;
import com.assetvisor.marvin.toolkit.internet.SearchInternetTool.Result;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SearchInternetTool implements Tool<Query, Result> {

    private static final Log LOG = LogFactory.getLog(SearchInternetTool.class);
    private final ForSearchingInternet forSearchingInternet;

    public SearchInternetTool(ForSearchingInternet forSearchingInternet) {
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
