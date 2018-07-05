package com.gmail.nossr50.flags;

public class LegalFlags {
    public static final int NOFLAGS                     = 0;
    public static final int MODERN                      = 1;
    public static final int STANDARD                    = 2;
    public static final int BLOCK                       = 4;
    public static final int COMMANDER                   = 8;
    public static final int VINTAGE                     = 16;
    public static final int LEGACY                      = 32;
    
    /*
     * Sigh... MTG-JSON is missing a lot of legalities
     */
    
    public static final int MISS_MODERN                 = 64;
    public static final int MISS_STANDARD               = 128;
    public static final int MISS_BLOCK                  = 256;
    public static final int MISS_COMMANDER              = 512;
    public static final int MISS_VINTAGE                = 1024;
    public static final int MISS_LEGACY                 = 2048;


    public static boolean isLegal(int x, int y) { return ((x & y) == y); };
}
