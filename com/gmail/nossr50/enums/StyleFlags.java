package com.gmail.nossr50.enums;

public class StyleFlags {
    public static final int NOFLAGS                     = 0;
    public static final int HEADERS                     = 1;
    public static final int PREFIX_BASIC                = 2;
    public static final int PREFIX_STATS                = 4;
    public static final int PREFIX_EXTRA                = 8;
    public static final int SPACING_BETWEEN_EXTRAS      = 16;
    
    
    public static boolean isEnabled(int x, int y) { return ((x & y) == y); };
}
