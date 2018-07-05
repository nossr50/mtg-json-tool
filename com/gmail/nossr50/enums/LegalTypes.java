package com.gmail.nossr50.enums;

import com.gmail.nossr50.flags.*;

public enum LegalTypes {
    MODERN("Modern", LegalFlags.MODERN, LegalFlags.MISS_MODERN),
    STANDARD("Standard", LegalFlags.STANDARD, LegalFlags.MISS_STANDARD),
    COMMANDER("Commander", LegalFlags.COMMANDER, LegalFlags.MISS_COMMANDER),
    LEGACY("Legacy", LegalFlags.LEGACY, LegalFlags.MISS_LEGACY),
    VINTAGE("Vintage", LegalFlags.VINTAGE, LegalFlags.MISS_VINTAGE),
    BLOCK("Block", LegalFlags.BLOCK, LegalFlags.MISS_BLOCK);
    
    private String name;
    private int legalFlag;
    private int missingFlag;
    
    private LegalTypes (String s, int flagValue, int mf) { name = s; legalFlag = flagValue; missingFlag = mf; }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    public int getLegalityFlag() { return legalFlag; }
    
    public int getMissingFlag() { return missingFlag; }
}
