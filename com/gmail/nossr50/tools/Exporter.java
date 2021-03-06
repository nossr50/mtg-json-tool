package com.gmail.nossr50.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.gmail.nossr50.enums.ExportType;
import com.gmail.nossr50.flags.ExportFlags;
import com.gmail.nossr50.flags.StyleFlags;

import io.magicthegathering.javasdk.resource.Card;
import io.magicthegathering.javasdk.resource.Legality;
import io.magicthegathering.javasdk.resource.Ruling;

public class Exporter {
    
    private static String mainDir                   = "exports";
    private static String simpleExportDir           = "simple_exports";
    private static String customExportDir           = "custom_exports";
    
    private static String header   = "============================================================";
    private static String header2  = "------------------------------------------------------------";
    
    

    public static void exportResults(ArrayList<Card> results, int exportFlags, int styleFlags, ExportType et)
    {
        
        String exportDirPATH = getExportPath(et);
        
        makeDirs(mainDir);
        makeDirs(mainDir+File.separator+simpleExportDir);
        makeDirs(mainDir+File.separator+customExportDir);
        
        makeDirs(exportDirPATH);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat dateFormatFile = new SimpleDateFormat("MM_dd_yyyy(HH_mm_ss)");
        Date date = new Date();
        
        String dateProcessedTimestamp   = dateFormat.format(date);
        String dateProcessedFile        = dateFormatFile.format(date);
        
        String fileName = "mtg-json-tool_" + dateProcessedFile + ".txt";
        
        String exportFilePATH = exportDirPATH + File.separator + fileName;
        
        BufferedWriter bw                   = null;
        FileWriter fw                       = null;
        StringBuilder sb                    = new StringBuilder();
        
        /*
         * Write to the file
         */
        
        System.out.println("Writing file");
        
        File curFile = new File(exportFilePATH); //export destination
        
        try {
            fw = new FileWriter(curFile.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            if (!curFile.exists()) {
                curFile.createNewFile();
            } else {
                //Clear the contents of the file
                PrintWriter pw = new PrintWriter(exportFilePATH);
                pw.close();
            }

            
            buildCardData(sb, results, exportFlags, styleFlags);
            
            sb.append("########################");
            sb.append(System.lineSeparator());
            sb.append("Data Exported on : " + dateProcessedTimestamp);
            sb.append(System.lineSeparator());
            sb.append("Script by : nossr50 (nossr50@gmail.com -- https://github.com/nossr50)");
            sb.append(System.lineSeparator());
            sb.append("########################");
            
            bw.write(sb.toString());
            
            bw.close();
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private static void makeDirs(String dir)
    {
        String PATH = dir + File.separator; //System specific directory shit

        File directory = new File(PATH);
        if (!directory.exists()){
            directory.mkdirs();
        }
    }
    
    private static String getExportPath(ExportType et)
    {
        switch(et)
        {
        case CUSTOM:
            return mainDir + File.separator + customExportDir;
        case SIMPLE:
            return mainDir + File.separator + simpleExportDir;
        case STANDARD:
            return mainDir + File.separator;
        default:
            return mainDir + File.separator;  
        }
    }
    
    private static void buildCardData(StringBuilder sb, ArrayList<Card> results, int exportFlags, int styleFlags)
    {
        
        for(Card c : results)
        {
            if(exportFlags == 0
                    && styleFlags == 0)
            {
                exportAll(sb, c);
            } else {
                System.out.println("Special Flags for export activated!");
                exportFlags(sb, c, exportFlags, styleFlags);
            }
        }
    }
    
    public static void exportFlags(StringBuilder sb, Card c, int exportFlags, int styleFlags)
    {
        boolean prefixBasic = StyleFlags.isEnabled(styleFlags, StyleFlags.PREFIX_BASIC);
        boolean prefixStats = StyleFlags.isEnabled(styleFlags, StyleFlags.PREFIX_STATS);
        boolean prefixExtra = StyleFlags.isEnabled(styleFlags, StyleFlags.PREFIX_EXTRA);
        
        //BEGINNING HEADERS
        if(StyleFlags.isEnabled(styleFlags, StyleFlags.HEADERS))
        {
            sb.append(header+System.lineSeparator());
            sb.append(header2+System.lineSeparator());
        }
        
        /*
         * BASIC
         */
        
        //Simplified Stats
        if(StyleFlags.isEnabled(styleFlags, StyleFlags.SIMPLIFIED_STATS) 
                && ExportFlags.isEnabled(exportFlags, ExportFlags.NAMES)
                    && ExportFlags.isEnabled(exportFlags, ExportFlags.SETNAME)
                    && c.getName() != null && c.getSet() != null)
        {
            appendCardData(sb, "Name: ", c.getName() + " ("+c.getSet()+")", prefixStats);
        } else {
          //Card Name
            if(ExportFlags.isEnabled(exportFlags, ExportFlags.NAMES))
            {
                appendCardData(sb, "Name: ", c.getName(), prefixBasic);
            }
            
            //Set Name
            if(ExportFlags.isEnabled(exportFlags, ExportFlags.SETNAME))
            {
                appendCardData(sb, "Set: ", c.getSetName(), prefixBasic);
            }
        }
        
        
        /*
         * STATS
         */
        
        //Rarity
        if(ExportFlags.isEnabled(exportFlags, ExportFlags.RARITY))
        {
            appendCardData(sb, "Rarity: ", c.getRarity(), prefixStats);
        }
        
        //Colors
        if(ExportFlags.isEnabled(exportFlags, ExportFlags.COLORS))
        {
            appendCardData(sb, "Colors: ", c.getColors(), prefixStats);
        }
        
        //Simplified Stats
        if(StyleFlags.isEnabled(styleFlags, StyleFlags.SIMPLIFIED_STATS) 
                && ExportFlags.isEnabled(exportFlags, ExportFlags.MANACOST)
                    && ExportFlags.isEnabled(exportFlags, ExportFlags.CMC)
                    && c.getManaCost() != null)
        {
            appendCardData(sb, "Mana Cost: ", getSimplifiedManaCost(c.getManaCost()) + " ("+Double.toString(c.getCmc())+" CMC )", prefixStats);
        } else {
          //Mana-Cost
            if(ExportFlags.isEnabled(exportFlags, ExportFlags.MANACOST))
            {
                appendCardData(sb, "Mana Cost: ", getSimplifiedManaCost(c.getManaCost()), prefixStats);
            }
            
            //CMC
            if(ExportFlags.isEnabled(exportFlags, ExportFlags.CMC))
            {
                appendCardData(sb, "CMC: ", Double.toString(c.getCmc()), prefixStats);
            }
        }
        
        /*
        //Simplified Stats
        if(StyleFlags.isEnabled(styleFlags, StyleFlags.SIMPLIFIED_STATS) 
                && ExportFlags.isEnabled(exportFlags, ExportFlags.SUPERTYPES)
                    && ExportFlags.isEnabled(exportFlags, ExportFlags.TYPES)
                        && ExportFlags.isEnabled(exportFlags, ExportFlags.SUBTYPES))
        {
            appendCardData(sb, "Types: ", c.getSupertypes() + " ("+c.getSupertypes().toString()+")", prefixStats);
        } else {
        */
          //Super-Types
            if(ExportFlags.isEnabled(exportFlags, ExportFlags.SUPERTYPES))
            {
                appendCardData(sb, "Super-Types: ", c.getSupertypes(), prefixStats);
            }
            
            //Type
            if(ExportFlags.isEnabled(exportFlags, ExportFlags.TYPES))
            {
                appendCardData(sb, "Types: ", c.getTypes(), prefixStats);
            }
            
            //Sub-Types
            if(ExportFlags.isEnabled(exportFlags, ExportFlags.SUBTYPES))
            {
                appendCardData(sb, "Sub-Types: ", c.getSubtypes(), prefixStats);
            }
       // }
        
          //Simplified Stats
            if(StyleFlags.isEnabled(styleFlags, StyleFlags.SIMPLIFIED_STATS) 
                    && ExportFlags.isEnabled(exportFlags, ExportFlags.POWER)
                        && ExportFlags.isEnabled(exportFlags, ExportFlags.TOUGHNESS)
                        && c.getPower() != null && c.getToughness() != null)
            {
                appendCardData(sb, "Stats: ", c.getPower() + "/" + c.getToughness(), prefixStats);
            } else {
              //Power
                if(ExportFlags.isEnabled(exportFlags, ExportFlags.POWER))
                {
                    appendCardData(sb, "Power: ", c.getPower(), prefixStats);
                }
                
                //Toughness
                if(ExportFlags.isEnabled(exportFlags, ExportFlags.TOUGHNESS))
                {
                    appendCardData(sb, "Toughness: ", c.getToughness(), prefixStats);
                }
            }

        //Artist
        if(ExportFlags.isEnabled(exportFlags, ExportFlags.ARTIST))
        {
            appendCardData(sb, "Artist: ", c.getArtist(), prefixExtra);
        }
        
        //Card Text
        if(ExportFlags.isEnabled(exportFlags, ExportFlags.CARD_TEXT)) {
            if(c.getOriginalText() != null || c.getText() != null)
            {
                if(c.getOriginalText() != null && !c.getOriginalText().isEmpty())
                {
                    if(StyleFlags.isEnabled(styleFlags, StyleFlags.SPACING_BETWEEN_EXTRAS))
                    {
                        sb.append(System.lineSeparator());
                        sb.append(System.lineSeparator());
                    }
                    
                    if(prefixExtra)
                        sb.append("--CARD TEXT [BEG]--"+System.lineSeparator());
                    appendCardDataWrap(sb, c.getOriginalText());
                    if(prefixExtra)
                        sb.append("--CARD TEXT [END]--"+System.lineSeparator());
                }
                else if(c.getText() != null && !c.getText().isEmpty())
                {
                    if(StyleFlags.isEnabled(styleFlags, StyleFlags.SPACING_BETWEEN_EXTRAS))
                    {
                        sb.append(System.lineSeparator());
                        sb.append(System.lineSeparator());
                    }
                    
                    if(prefixExtra)
                        sb.append("--CARD TEXT [BEG]--"+System.lineSeparator());
                    appendCardDataWrap(sb, c.getText());
                    if(prefixExtra)
                        sb.append("--CARD TEXT [END]--"+System.lineSeparator());
                }
            }
        }
        
        //Flavour Text
        if(ExportFlags.isEnabled(exportFlags, ExportFlags.FLAVOUR_TEXT)) {
            if(c.getFlavor() != null && !c.getFlavor().isEmpty())
            {
                if(StyleFlags.isEnabled(styleFlags, StyleFlags.SPACING_BETWEEN_EXTRAS))
                {
                    sb.append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                }
                
                if(prefixExtra)
                    sb.append("--FLAVOUR TXT [BEG]--"+System.lineSeparator());
                
                appendCardDataWrap(sb, c.getFlavor());
                
                if(prefixExtra)
                    sb.append("--FLAVOUR TXT [END]--"+System.lineSeparator());
            }
        }
        
        if(ExportFlags.isEnabled(exportFlags, ExportFlags.RULINGS)) {
            if(c.getRulings() != null && c.getRulings().length > 0)
            {
                if(StyleFlags.isEnabled(styleFlags, StyleFlags.SPACING_BETWEEN_EXTRAS))
                {
                    sb.append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                }
                
                if(prefixExtra)
                    sb.append("--RULINGS [BEG]--"+System.lineSeparator());
                appendCardDataWrap(sb, c.getRulings());
                if(prefixExtra)
                    sb.append("--RULINGS [END]--"+System.lineSeparator());
            }
        }
        
        if(ExportFlags.isEnabled(exportFlags, ExportFlags.LEGALITIES)) {
            if(c.getLegalities() != null && c.getLegalities().length > 0)
            {
                if(StyleFlags.isEnabled(styleFlags, StyleFlags.SPACING_BETWEEN_EXTRAS))
                {
                    sb.append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                }
                
                if(prefixExtra)
                    sb.append("--LEGALITIES [BEG]--"+System.lineSeparator());
                appendCardDataWrap(sb, c.getLegalities());
                if(prefixExtra)
                    sb.append("--LEGALITIES [END]--"+System.lineSeparator());
            }
        }
        
        //END HEADERS
        if(StyleFlags.isEnabled(styleFlags, StyleFlags.HEADERS))
        {
            sb.append(header2+System.lineSeparator());
            sb.append(header+System.lineSeparator()+System.lineSeparator());
        }
        
        
    }
    
    private static String getSimplifiedManaCost(String s)
    {
        String newString = "";
        
        for(char c : s.toCharArray())
        {
            if(c != '{' && c != '}')
            {
                newString+=c;
            }
        }
        
        return newString;
    }
    
    private static void exportAll(StringBuilder sb, Card c)
    {
        exportFlags(sb, c, ExportFlags.getEverythingEnabled(), StyleFlags.getEverythingEnabled());
    }
    
    private static void appendCardData(StringBuilder sb, String prefix, String string, boolean prefixBool)
    {
        if(string != null && !string.isEmpty())
        {
            if(prefixBool)
                sb.append(prefix+string+System.lineSeparator());
            else
                sb.append(string+System.lineSeparator());
        } else {
            //sb.append(System.lineSeparator());
        }
    }
    
    private static void appendCardData(StringBuilder sb, String prefix, String string[], boolean prefixBool)
    {
        if(string != null && string.length > 0)
        {
            String newString = "";
            
            for(int x = 0; x < string.length; x++)
            {
                if(x+1 < string.length)
                {
                    newString+=string[x]+", ";
                } else {
                    newString+=string[x];
                }
            }
            
            if(prefixBool)
                sb.append(prefix+newString+System.lineSeparator());
            else
                sb.append(newString+System.lineSeparator());
            
        } else {
            //sb.append(System.lineSeparator());
        }
    }
    
    private static void appendCardDataWrap(StringBuilder sb, String string)
    {
        if(string != null && !string.isEmpty())
        {
            String newString = "";
            
            for(int x = 0; x < string.length(); x++)
            {
                if(string.toCharArray()[x] == 0x0A) //ASCII New line character
                    newString+=System.lineSeparator();
                else
                    newString+=string.toCharArray()[x];
            }
            sb.append(newString+System.lineSeparator());
        } else {
            //sb.append(System.lineSeparator());
        }
    }
    
    private static void appendCardDataWrap(StringBuilder sb, Ruling[] rulings)
    {
        String newString = "";
            
        for(int a = 0; a < rulings.length; a++)
        {
            String curString = rulings[a].getText();
            
            for(int x = 0; x < curString.length(); x++)
            {
                if(curString.toCharArray()[x] == 0x0A) //ASCII New line character
                    newString+=System.lineSeparator();
                else
                    newString+=curString.toCharArray()[x];
            }
            
            if(a+1 < rulings.length)
                newString+=System.lineSeparator()+System.lineSeparator();
        }
        
        sb.append(newString+System.lineSeparator());

    }
    
    private static void appendCardDataWrap(StringBuilder sb, Legality[] legalities)
    {
        String newString = "";
            
        for(Legality leg : legalities)
        {
            String curString = leg.getFormat().toString() + ": " + leg.getLegality().toString();
            
            newString+=curString+System.lineSeparator();
        }
        
        sb.append(newString);
    }
}
