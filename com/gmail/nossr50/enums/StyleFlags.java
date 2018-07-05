package com.gmail.nossr50.enums;

public class StyleFlags {
    public static final int NOFLAGS                     = 0;
    public static final int HEADERS                     = 1;
    public static final int PREFIX_BASIC                = 2;
    public static final int PREFIX_STATS                = 4;
    public static final int PREFIX_EXTRA                = 8;
    public static final int SPACING_BETWEEN_EXTRAS      = 16;
    public static final int SIMPLIFIED_STATS            = 32;
    
    
    public static boolean isEnabled(int x, int y) { return ((x & y) == y); };
    
    public static int getEverythingEnabled()
    {
        //I know.. it can just be hardcoded
        return 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128 | 256 | 512 | 1024 | 2048 | 4096
                    | 8192 | 16384 | 32768;
    }
}
