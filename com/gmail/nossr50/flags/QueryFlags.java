package com.gmail.nossr50.flags;

public class QueryFlags {
    public static final int NOFLAGS                     = 0;
    public static final int CLEAR_SEARCH                = 1;
    public static final int ADDITIVE_SEARCH             = 2;
    
    
    public static boolean isEnabled(int x, int y) { return ((x & y) == y); };
    
    public static int getEverythingEnabled()
    {
        //I know.. it can just be hardcoded
        return 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128 | 256 | 512 | 1024 | 2048 | 4096
                    | 8192 | 16384 | 32768;
    }
}
