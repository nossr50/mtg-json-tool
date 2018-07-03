package com.gmail.nossr50.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.gmail.nossr50.MainApplicationWindow;
import com.gmail.nossr50.enums.AppState;
import com.gmail.nossr50.enums.ExportType;
import com.gmail.nossr50.enums.Flags;

import io.magicthegathering.javasdk.resource.Card;
import io.magicthegathering.javasdk.resource.Ruling;

public class Exporter {
    
    private static String mainDir                   = "exports";
    private static String simpleExportDir           = "simple_exports";
    private static String customExportDir           = "custom_exports";
    
    private static String header   = "============================================================";
    private static String header2  = "------------------------------------------------------------";
    
    

    public static void exportResults(ArrayList<Card> results, int flags, ExportType et)
    {
        
        String exportDirPATH = getExportPath(et);
        
        makeDirs(mainDir);
        makeDirs(exportDirPATH);
        
        String fileName = "queryExport.txt";
        
        String exportFilePATH = exportDirPATH + File.separator + fileName;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        
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

            
            buildCardData(sb, results, flags);
            
            sb.append("########################");
            sb.append(System.lineSeparator());
            sb.append("Data Exported on : " + dateFormat.format(date));
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
    
    private static void buildCardData(StringBuilder sb, ArrayList<Card> results, int flags)
    {
        
        for(Card c : results)
        {
            sb.append(header+System.lineSeparator());
            sb.append(header2+System.lineSeparator());
            
            System.out.println(flags | Flags.NOFLAGS);
            
            if((flags | Flags.NOFLAGS) == Flags.NOFLAGS)
            {
                exportAll(sb, c);
            } else {
                System.out.println("Special Flags for export activated!");
                exportFlags(sb, c, flags);
            }
        }
    }
    
    private static void exportFlags(StringBuilder sb, Card c, int flags)
    {
        if((flags & Flags.NAMES) == Flags.NAMES)
            appendCardData(sb, "Name: ", c.getName());
    }
    
    private static void exportAll(StringBuilder sb, Card c)
    {
        //Card Name
        appendCardData(sb, "Name: ", c.getName());
        
        //Set Name
        appendCardData(sb, "Set: ", c.getSetName());
        
        //Rarity
        appendCardData(sb, "Rarity: ", c.getRarity());
        
        //Colors
        appendCardData(sb, "Colors: ", c.getColors());
        
        //CMC
        appendCardData(sb, "CMC: ", Double.toString(c.getCmc()));
        
        //Super-Types
        appendCardData(sb, "Super-Types: ", c.getSupertypes());
        
        //Type
        appendCardData(sb, "Types: ", c.getTypes());
        
        //Sub-Types
        appendCardData(sb, "Sub-Types: ", c.getSubtypes());
        
        //Power
        appendCardData(sb, "Power: ", c.getPower());
        
        //Toughness
        appendCardData(sb, "Toughness: ", c.getToughness());
        
        //Artist
        appendCardData(sb, "Artist: ", c.getArtist());
        
        if(c.getOriginalText() != null || c.getText() != null)
        {
            
            //Card Text
            if(c.getOriginalText() != null && !c.getOriginalText().isEmpty())
            {
                sb.append(System.lineSeparator());
                sb.append(System.lineSeparator());
                sb.append("--CARD TEXT [BEG]--"+System.lineSeparator());
                appendCardDataWrap(sb, c.getOriginalText());
                sb.append("--CARD TEXT [END]--"+System.lineSeparator());
            }
            else if(c.getText() != null && !c.getText().isEmpty())
            {
                sb.append(System.lineSeparator());
                sb.append(System.lineSeparator());
                sb.append("--CARD TEXT [BEG]--"+System.lineSeparator());
                appendCardDataWrap(sb, c.getText());
                sb.append("--CARD TEXT [END]--"+System.lineSeparator());
            }
        }
        
        
        //Flavour Text
        if(c.getFlavor() != null && !c.getFlavor().isEmpty())
        {
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
            sb.append("--FLAVOUR TXT [BEG]--"+System.lineSeparator());
            appendCardDataWrap(sb, c.getFlavor());
            sb.append("--FLAVOUR TXT [END]--"+System.lineSeparator());
        }
        
        if(c.getRulings() != null && c.getRulings().length > 0)
        {
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
            sb.append("--LEGALITIES / RULINGS [BEG]--"+System.lineSeparator());
            appendCardDataWrap(sb, c.getRulings());
            sb.append("--LEGALITIES / RULINGS [END]--"+System.lineSeparator());
        }
        
        sb.append(header2+System.lineSeparator());
        sb.append(header+System.lineSeparator()+System.lineSeparator());
    }
    
    private static void appendCardData(StringBuilder sb, String prefix, String string)
    {
        if(string != null && !string.isEmpty())
        {
            sb.append(prefix+string+System.lineSeparator());
        } else {
            //sb.append(System.lineSeparator());
        }
    }
    
    private static void appendCardData(StringBuilder sb, String prefix, String string[])
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
            
            sb.append(prefix+newString+System.lineSeparator());
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
}
