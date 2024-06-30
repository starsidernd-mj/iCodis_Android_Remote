package com.example.icodisremote;

import android.util.Log;

public class IRMessages {
    // iCodis RD 815
    public IRMessage RD_POWER;
    public IRMessage RD_MENU;
    public IRMessage RD_SOURCE;
    public IRMessage RD_FREEZE_FRAME;
    public IRMessage RD_ROTATE_SCREEN;
    public IRMessage RD_VOL_UP;
    public IRMessage RD_VOL_DOWN;
    public IRMessage RD_MUTE;
    public IRMessage RD_BACK;
    public IRMessage RD_SETTINGS;
    public IRMessage RD_ASPECT;
    public IRMessage RD_ENTER;
    public IRMessage RD_PICTURE;
    public IRMessage RD_COLOR;
    public IRMessage RD_UP;
    public IRMessage RD_LEFT;
    public IRMessage RD_RIGHT;
    public IRMessage RD_DOWN;

    public IRMessages() {
        Log.d("iCodis", "Initializing IRMessage types");

        // iCODIS
        IRMessage RD_POWER          = IRNECFactory.create(0x2f, 0xef, 0, "POWER"    , 8);
        IRMessage RD_MENU           = IRNECFactory.create(0xcf, 0xef, 0, "MENU"     , 8);
        IRMessage RD_SOURCE         = IRNECFactory.create(0x4f, 0xef, 0, "SOURCE"   , 8);
        IRMessage RD_FREEZE_FRAME   = IRNECFactory.create(0x8f, 0xef, 0, "FREEZE"   , 8);
        IRMessage RD_ROTATE_SCREEN  = IRNECFactory.create(0x0f, 0xef, 0, "ROTATE"   , 8);
        IRMessage RD_VOL_UP         = IRNECFactory.create(0x37, 0xef, 0, "VOL +"    , 8);
        IRMessage RD_VOL_DOWN       = IRNECFactory.create(0xb7, 0xef, 0, "VOL -"    , 8);
        IRMessage RD_MUTE           = IRNECFactory.create(0xd7, 0xef, 0, "MUTE"     , 8);
        IRMessage RD_BACK           = IRNECFactory.create(0x57, 0xef, 0, "BACK"     , 8);
        IRMessage RD_SETTINGS       = IRNECFactory.create(0x17, 0xef, 0, "SETTINGS" , 8);
        IRMessage RD_ASPECT         = IRNECFactory.create(0xa7, 0xef, 0, "ASPECT"   , 8);
        IRMessage RD_ENTER          = IRNECFactory.create(0xc7, 0xef, 0, "OK"       , 8);
        IRMessage RD_PICTURE        = IRNECFactory.create(0x87, 0xef, 0, "PIC"      , 8);
        IRMessage RD_COLOR          = IRNECFactory.create(0x0d, 0xef, 0, "COLOR"    , 8);
        IRMessage RD_UP             = IRNECFactory.create(0xf5, 0xef, 0, "UP"       , 8);
        IRMessage RD_LEFT           = IRNECFactory.create(0x75, 0xef, 0, "LEFT"     , 8);
        IRMessage RD_RIGHT          = IRNECFactory.create(0xb5, 0xef, 0, "RIGHT"    , 8);
        IRMessage RD_DOWN           = IRNECFactory.create(0x35, 0xef, 0, "DOWN"     , 8);

        Log.d("iCodis", "Initializing IRMessage done");
    }
    
    public IRMessage get_msg(int id) {
        switch(id) {
            case 0: return RD_POWER;
            case 1: return RD_MENU;
            case 2: return RD_SOURCE;
            case 3: return RD_FREEZE_FRAME;
            case 4: return RD_ROTATE_SCREEN;
            case 5: return RD_VOL_UP;
            case 6: return RD_VOL_DOWN;
            case 7: return RD_MUTE;
            case 8: return RD_BACK;
            case 9: return RD_SETTINGS;
            case 10: return RD_ASPECT;
            case 11: return RD_ENTER;
            case 12: return RD_PICTURE;
            case 13: return RD_COLOR;
            case 14: return RD_UP;
            case 15: return RD_LEFT;
            case 16: return RD_RIGHT;
            case 17: return RD_DOWN;
        }
        return RD_POWER;
    }

    public static int get_command(IRMessage msg) {
        return msg._cmd;
    }

    public static int get_address(IRMessage msg) {
        return msg._addr;
    }
}
