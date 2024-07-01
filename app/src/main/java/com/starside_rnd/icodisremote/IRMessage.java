package com.starside_rnd.icodisremote;

import android.util.Log;

public class IRMessage {
    public static int FREQ_38222_HZ = 38222;
    public static int FREQ_56000_HZ = 56000;
    public static int FREQ_40000_HZ = 40000;
    public static int FREQ_38000_HZ = 38000;
    public static int FREQ_36000_HZ = 36000;
    public static int FREQ_33000_HZ = 33000;
    public static int FREQ_30000_HZ = 30000;

    private final int _frequency;
    private int _messageTime;
    private final int [] _message;
    public final String _name;

    public int _cmd;
    public int _addr;

    public IRMessage(int frequency, int [] message, int command, int address, String name) {
        _frequency = frequency;
        _message = message;
        _name = name;

        _cmd = command;
        _addr = address;

        Log.d("iCodis", "Creating new IRMessage: "+_cmd+" "+_addr);

        calculateMessageTime();
    }

    public void calculateMessageTime() {
        int time = 0;
        for (int i : _message) {
            time += i;
        }
        _messageTime = time;
    }

    public int get_frequency() {
        return _frequency;
    }

    public int[] get_message() {
        return _message;
    }

    public int get_messageTime() {
        return _messageTime;
    }
}