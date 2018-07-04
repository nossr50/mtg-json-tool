package com.gmail.nossr50.enums;

public class ExportFlags {
    public static final int NOFLAGS      = 0;    // In binary: 00000
    public static final int NAMES        = 1;    // In binary: 00001
    public static final int SETNAME      = 2;    // In binary: 00010
    public static final int SUPERTYPES   = 4;    // In binary: 00100
    public static final int TYPES        = 8;
    public static final int SUBTYPES     = 16;
    public static final int COLORS       = 32;
    public static final int CMC          = 64;
    public static final int POWER        = 128;
    public static final int TOUGHNESS    = 256;
    public static final int RARITY       = 512;
    public static final int CARD_TEXT    = 1024;
    public static final int FLAVOUR_TEXT = 2048;
    public static final int ARTIST       = 4096;
    public static final int LEGALITIES   = 8192;
    //public static final int HEADERS      = 16384;
    //public static final int LABELS       = 32768;
    
    public static boolean isEnabled(int x, int y) { return ((x & y) == y); }
}