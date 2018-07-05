package com.gmail.nossr50.runnables;

import java.util.ArrayList;
import com.gmail.nossr50.MainApplicationWindow;
import com.gmail.nossr50.enums.AppState;
import com.gmail.nossr50.tools.Query;

import io.magicthegathering.javasdk.resource.Card;

public class QueryThread implements Runnable {

    ArrayList<String> curFilters;
    
    public QueryThread(ArrayList<String> filters) {
        curFilters = filters;
        
    }
    
    @Override
    public void run() {
        MainApplicationWindow.setState(AppState.RUNNING);
        MainApplicationWindow.results = (ArrayList<Card>) Query.initQuery(curFilters);
        MainApplicationWindow.initLegalities();
        MainApplicationWindow.asyncUpdateResults();
        MainApplicationWindow.setState(AppState.FINISHED);
    }

}
