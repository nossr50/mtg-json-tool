package com.gmail.nossr50.runnables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        List<Card> newResults = Query.initQuery(curFilters);
        if(newResults != null && newResults.size() > 0)
        {
            if(QueryFlags.isEnabled(curFlags, QueryFlags.CLEAR_SEARCH))
            {
                //Reset old search results
                MainApplicationWindow.results = new HashMap<Integer, Card>();
                
                for(Card c : newResults)
                {
                    MainApplicationWindow.results.put(c.getMultiverseid(), c);
                }
            } else if(QueryFlags.isEnabled(curFlags, QueryFlags.ADDITIVE_SEARCH))
            {
                
                for(Card c : newResults)
                {
                    MainApplicationWindow.addCardAdditive(c);
                }
            }
        }
        
    }

}
