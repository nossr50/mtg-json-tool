package com.gmail.nossr50.tools;

import java.util.ArrayList;
import com.gmail.nossr50.MainApplicationWindow;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

/**
 * This class contains helper functions for executing filter-based queries for the MTG JSON DB via the MTG-API SDK
 * @author nossr50
 *
 */
public class Query {

    /**
     * Sends a query to the MTG Json DB based on provided filters
     * @param curFilters The filters for the query
     * @return The cards matching the query
     */
    public static ArrayList<Card> initQuery(ArrayList<String> curFilters) {
        
        ArrayList<Card> queryResults = getCards(curFilters);

        if (queryResults == null || queryResults.isEmpty()) {
            System.out.println("[WARNING] No results found for query!");
            return null;
        } else {
            return queryResults;
        }
    }

    /**
     * 
     * @param filters Filters for our query, see MTG-API documentation
     * @return Cards returned from our Query (null or empty if there aren't any)
     * @see <a href=https://magicthegathering.io/>MTG-API</a>
     */
    public static ArrayList<Card> getCards(ArrayList<String> filters) {
        ArrayList<String> curFilters = new ArrayList<String>();
        
        if(filters.size() == 0)
        {
            MainApplicationWindow.asyncToggleWarning(true);
            
            //Do a query for all cards
            ArrayList<Card> cards = (ArrayList<Card>) CardAPI.getAllCards();
            return cards;
        }
        
        //If the search is across multiple sets...
        ArrayList<String> sets = new ArrayList<String>();
        
        for (String s : filters) {
            if(s.contains("setName") && s.contains(","))
            {
                String isolatedString = s.substring(8); //Isolate the set names
                String[] splitCommasString = isolatedString.split(","); //Split by spaces
                for(String subSplit : splitCommasString) {
                    
                    if(subSplit.startsWith(" "))
                    {
                        subSplit = subSplit.substring(1);
                    }
                    
                    //Add all splits to sets
                    System.out.println(subSplit);
                    sets.add("setName="+subSplit); //Add the filter
                }
                
            }  
        }
        
        ArrayList<Card> cards = new ArrayList<Card>();
        
        if(sets.size() > 1)
        {
            for (String s : filters) {
                //Ignore the set filters
                if(!s.contains("setName"))
                    curFilters.add(s);
                //System.out.println(s);
            }
            
            for(String s : sets)
            {
                System.out.println("Issuing Set query");
                ArrayList<String> setFilters = new ArrayList<String>();
                
                setFilters.add(s);
                setFilters.addAll(curFilters);
                
                //Warn them if they are doing what we consider a "generic" query
                if(setFilters.size() <= 1 && isGenericQuery(setFilters))
                {
                    MainApplicationWindow.asyncToggleWarning(true);
                }
                
                ArrayList<Card> curResults = (ArrayList<Card>) CardAPI.getAllCards(setFilters);
                
                if(curResults != null && !curResults.isEmpty())
                    cards.addAll(curResults);
            }
            
        } else {
            for (String s : filters) {
                curFilters.add(s);
            }
            
            //Warn them if they are doing what we consider a "generic" query
            if(curFilters.size() <= 1 && isGenericQuery(curFilters))
            {
                MainApplicationWindow.asyncToggleWarning(true);
            }
            
            ArrayList<Card> curResults = (ArrayList<Card>) CardAPI.getAllCards(curFilters);
            
            if(curResults != null && !curResults.isEmpty())
                cards.addAll(curResults);
        }
        
        if (cards == null || cards.isEmpty()) {
            System.out.println("No results for current filters!");
            return null;
        } else {
            return cards;
        }
    }
    
    public static boolean isGenericQuery(ArrayList<String> curFilters)
    {
        for(String s : curFilters)
        {
            if(s.startsWith("setName=") || s.startsWith("artist=") || s.startsWith("name="))
                return false;
        }
        
        return true;
    }
}
