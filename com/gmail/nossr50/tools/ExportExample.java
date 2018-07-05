package com.gmail.nossr50.tools;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

public class ExportExample {
    
    public static Card exampleCard_a;
    public static Card exampleCard_b;
    public static Card exampleCard_c;
    public static Card exampleCard_d;
    
    public ExportExample()
    {
        initExampleCards();
    }
    
    public String getExample(int exportFlags, int styleFlags)
    {
        //ArrayList<String> newExample = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        
        //Hard Coded examples
        Exporter.exportFlags(sb, exampleCard_c, exportFlags, styleFlags);
        Exporter.exportFlags(sb, exampleCard_d, exportFlags, styleFlags);
        Exporter.exportFlags(sb, exampleCard_a, exportFlags, styleFlags);
        Exporter.exportFlags(sb, exampleCard_b, exportFlags, styleFlags);
        
        return sb.toString();
    }
    
    private void initExampleCards()
    {
        exampleCard_a = CardAPI.getCard(430834);
        exampleCard_b = CardAPI.getCard(439702);
        exampleCard_c = CardAPI.getCard(439801);
        exampleCard_d = CardAPI.getCard(442989);
    }
}
