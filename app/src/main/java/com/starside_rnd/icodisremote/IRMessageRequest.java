package com.starside_rnd.icodisremote;

import java.util.ArrayList;
import java.util.Arrays;
import android.util.Log;

public class IRMessageRequest {
    private final ArrayList<IRMessage> _messages = new ArrayList<>();
    public IRMessage _req;

    public IRMessageRequest(IRMessage... args) {
        _messages.addAll(Arrays.asList(args));
    }

    public IRMessageRequest(IRMessage msg) {
        _req = msg;
        _messages.add(msg);

        int cmd = msg._cmd;
        int addr = msg._addr;

        Log.d("iCodis", "New IRMsgRequest: "+cmd+" "+addr);
    }

    public ArrayList<IRMessage> get_messages() {
        return _messages;
    }

    public long getRequestTime() {
        long time = 0;
        for(int a = 0; a < _messages.size(); ++a) {
            time += _messages.get(a).get_messageTime();
        }
        return time;
    }
}