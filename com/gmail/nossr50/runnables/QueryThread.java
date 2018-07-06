package com.gmail.nossr50.runnables;

import java.util.ArrayList;
import java.util.HashMap;
import com.gmail.nossr50.MainApplicationWindow;
import com.gmail.nossr50.enums.AppState;
import com.gmail.nossr50.flags.QueryFlags;
import com.gmail.nossr50.tools.Query;

import io.magicthegathering.javasdk.resource.Card;

public class QueryThread implements Runnable {

    ArrayList<String> curFilters;
    int curFlags;
    
    public QueryThread(ArrayList<String> filters, int flags) {
        curFilters = filters;
        curFlags = flags;
    }
    
    @Override
    public void run() {
        MainApplicationWindow.setState(AppState.RUNNING);
        
        initSearch();
        
        if(MainApplicationWindow.results != null && MainApplicationWindow.results.size() > 0)
        {
            MainApplicationWindow.setNumResults(MainApplicationWindow.results.size());
            MainApplicationWindow.initLegalities();
            MainApplicationWindow.asyncUpdateResults();
        }

        MainApplicationWindow.setState(AppState.FINISHED);
    }
    
    private void initSearch()
    {
        ArrayList<Card> newResults = Query.initQuery(curFilters);
        
        System.out.println("SIZE: "+ newResults.size());
        if(newResults != null && newResults.size() > 0)
        {
            if(QueryFlags.isEnabled(curFlags, QueryFlags.CLEAR_SEARCH))
            {
                //Reset old search results
                MainApplicationWindow.results = new ArrayList<Card>();
                MainApplicationWindow.resultTracker = new HashMap<String, Card>();
            }
            
            ArrayList<Card> sorted = MainApplicationWindow.sortResults(newResults);
            
            MainApplicationWindow.addUniqueCards(sorted);
        }
    }

}
