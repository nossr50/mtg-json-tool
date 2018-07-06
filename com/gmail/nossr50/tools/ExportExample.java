package com.gmail.nossr50.tools;

import io.magicthegathering.javasdk.resource.Card;
import io.magicthegathering.javasdk.resource.Legality;
import io.magicthegathering.javasdk.resource.Ruling;

public class ExportExample {
    
    public static Card a;
    public static Card b;
    
    public ExportExample()
    {
        initExampleCards();
    }
    
    public String getExample(int exportFlags, int styleFlags)
    {
        //ArrayList<String> newExample = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        
        //Hard Coded examples
        initExampleCards();
       
        Exporter.exportFlags(sb, a, exportFlags, styleFlags);
        Exporter.exportFlags(sb, b, exportFlags, styleFlags);
        
        return sb.toString();
    }
    
    private void initExampleCards()
    {
        a = new Card();
        b = new Card();
        
        a.setName("Polyraptor");
        a.setSetName("Rivals of Ixalan");
        a.setSet("RIX");
        a.setColors(new String[] {"Green"});
        a.setManaCost("{6}{G}{G}");
        a.setRarity("Mythic Rare");
        a.setPower("5");
        a.setToughness("5");
        a.setType("Creature");
        a.setSubtypes(new String[] {"Dinosaur"});
        a.setCmc(8.0);
        a.setArtist("Mark Behm");
        a.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=439801&type=card");
        a.setText("Enrage — Whenever Polyraptor is dealt damage, create a token that's a copy of Polyraptor.");
        
        Ruling r_1 = new Ruling();
        Ruling r_2 = new Ruling();
        Ruling r_3 = new Ruling();
        Ruling r_4 = new Ruling();
        Ruling r_5 = new Ruling();
        
        r_1.setText("The token will have Polyraptor's ability. It will also be able to create copies of itself.");
        r_2.setText("The token won't copy counters or damage marked on Polyraptor, nor will it copy other effects that have changed Polyraptor's power, toughness, types, color, or so on. Normally, this means the token will simply be a Polyraptor. But if any copy effects have affected that Polyraptor, they're taken into account.");
        r_3.setText("If Polyraptor leaves the battlefield before its triggered ability resolves, most likely because it was dealt lethal damage, the token will still enter the battlefield as a copy of Polyraptor, using Polyraptor's copiable values from when it was last on the battlefield.");
        r_4.setText("If multiple sources deal damage to a creature with an enrage ability at the same time, most likely because multiple creatures blocked that creature, the enrage ability triggers only once.");
        r_5.setText("If lethal damage is dealt to a creature with an enrage ability, that ability triggers. The creature with that enrage ability leaves the battlefield before that ability resolves, so it won't be affected by the resolving ability.");
        
        a.setRulings(new Ruling[] {r_1, r_2, r_3, r_4, r_5});
        a.setLegalities(genLegality());
        
        /*
         * 
         */
        
        b.setName("The Scarab God");
        b.setSetName("Hour of Devastation");
        b.setSet("HOD");
        b.setColors(new String[] {"Blue", "Black"});
        b.setManaCost("{3}{U}{B}");
        b.setRarity("Mythic Rare");
        b.setPower("5");
        b.setToughness("5");
        b.setSupertypes(new String[] {"Legendary"});
        b.setType("Creature");
        b.setSubtypes(new String[] {"God"});
        b.setCmc(5.0);
        b.setArtist("Lius Lasahido");
        b.setImageUrl("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=430834&type=card");
        b.setText("At the beginning of your upkeep, each opponent loses X life and you scry X, where X is the number of Zombies you control.\r\n" + 
                    "{2}{U}{B}: Exile target creature card from a graveyard. Create a token that's a copy of it, except it's a 4/4 black Zombie.\r\n" + 
                    "When The Scarab God dies, return it to its owner's hand at the beginning of the next end step.");
        
        Ruling br_1 = new Ruling();
        Ruling br_2 = new Ruling();
        Ruling br_3 = new Ruling();
        Ruling br_4 = new Ruling();
        Ruling br_5 = new Ruling();
        Ruling br_6 = new Ruling();
        Ruling br_7 = new Ruling();
        Ruling br_8 = new Ruling();
        
        
        br_1.setText("The number of Zombies you control is counted as The Scarab God's first ability resolves. Players can try to change that number in response to the ability (perhaps by activating its second ability).");
        br_2.setText("The token copies exactly what was printed on the original card and nothing else, except the characteristics it specifically modifies. It doesn't copy any information about the object the card was before it was put into its owner's graveyard.");
        br_3.setText("The token is a Zombie instead of its other types (unlike Zombies created by an eternalize ability) and is black instead of its other colors. Its power and toughness are 4/4. These are copiable values of the token that other effects may copy.");
        br_4.setText("Unlike the tokens created by an eternalize ability, this token has the mana cost and thus converted mana cost of the card it's copying.");
        br_5.setText("If the card copied by the token had any \"when [this permanent] enters the battlefield\" abilities, then the token also has those abilities and will trigger them when it's created. Similarly, any \"as [this permanent] enters the battlefield\" or \"[this permanent] enters the battlefield with\" abilities that the token has copied will also work.");
        br_6.setText("In a Two-Headed Giant game, The Scarab God's first ability causes the opposing team to lose life equal to twice the number of Zombies you control, although you scry only equal to the number of Zombies you control.");
        br_7.setText("If this creature dies but leaves your graveyard before the next end step, it will remain in its new zone.");
        br_8.setText("The \"next end step\" refers to the next end step that occurs, not the end step of the next turn. If this creature dies before a turn's end step (for example, during combat), it will be returned to its owner's hand at the beginning of that turn's end step.");
        
        
        b.setRulings(new Ruling[] {br_1, br_2, br_3, br_4, br_5, br_6, br_7, br_8});
        b.setLegalities(genLegality());
    }
    
    private Legality[] genLegality()
    {
        Legality l_1 = new Legality();
        Legality l_2 = new Legality();
        Legality l_3 = new Legality();
        Legality l_4 = new Legality();
        Legality l_5 = new Legality();
        Legality l_6 = new Legality();
        
        l_1.setLegality("Legal");
        l_1.setFormat("Standard");
        
        l_2.setLegality("Legal");
        l_2.setFormat("Modern");
        
        l_3.setLegality("Legal");
        l_3.setFormat("Commander");
        
        l_4.setLegality("Legal");
        l_4.setFormat("Legacy");
        
        l_5.setLegality("Legal");
        l_5.setFormat("Vintage");
        
        l_6.setLegality("Legal");
        l_6.setFormat("Block");
        
        return new Legality[] {l_1, l_2, l_3, l_4, l_5, l_6};
    }
}
