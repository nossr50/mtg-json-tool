package com.gmail.nossr50.tools;

import java.util.ArrayList;
import java.util.List;

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
    public static List<Card> initQuery(ArrayList<String> curFilters) {
        
        List<Card> queryResults = getCards(curFilters);

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

        for (String s : filters) {
            curFilters.add(s);
        }

        ArrayList<Card> cards = (ArrayList<Card>) CardAPI.getAllCards(curFilters);

        if (cards == null || cards.isEmpty()) {
            System.out.println("No results for current filters!");
            return null;
        } else {
            return cards;
        }
    }
}
