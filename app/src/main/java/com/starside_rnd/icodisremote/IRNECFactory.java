package com.starside_rnd.icodisremote;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class IRNECFactory {
    public static final int NEC_UNIT  = 560;
    
    public static final int HDR_MARK  = 16*NEC_UNIT;
    public static final int HDR_SPACE = 8*NEC_UNIT;
    
    public static final int FTR_MARK  = NEC_UNIT;
    public static final int FTR_SPACE = 1687;

    public static final int ONE_MARK  = NEC_UNIT;
    public static final int ONE_SPACE = 3*NEC_UNIT;
    
    public static final int ZERO_MARK = NEC_UNIT;
    public static final int ZERO_SPACE= NEC_UNIT;

    public static final int REPEAT_TIME = 110000;
    public static final int REPEAT_SPACE  = 2250;

    public static int _cmd;
    public static int _addr;
    public static int _rep;
    public static String _item;
    
    public static List<Integer> header1;
    public static List<Integer> header2;
    
    public static List<Integer> data1;
    public static List<Integer> data2;
    

    public static IRMessage create(int command, int address, int repeats, String item, int bits) {
        List<Integer> message = new ArrayList<>();

        _item = item;
        _cmd = command;
        _addr = address;
        _rep = repeats;

        message.add(HDR_MARK);
        message.add(HDR_SPACE);

        boolean MSB = true;
        //boolean MSB = false;
        
        if(bits == 8) {
            header1 = decodeInt(address, MSB, bits);
            header2 = decodeInt(~address, MSB, bits);

            data1 = decodeInt(command, MSB, bits);
            data2 = decodeInt(~command, MSB, bits);
            
            message.addAll(header1);
            message.addAll(header2);
            message.addAll(data1);
            message.addAll(data2);
        } else if(bits == 16) {
            header1 = decodeInt(address, MSB, bits);
            data1 = decodeInt(command, MSB, bits);
            message.addAll(header1);
            message.addAll(data1);
        }

        message.add(FTR_MARK);
        //message.add(FTR_SPACE);

        int [] final_Code = new int[message.size()];

        for(int a = 0; a < message.size(); a++) {
            final_Code[a] = message.get(a);
        }
        
        Log.d("iCodis", "Message size: "+message.size());
        
        return new IRMessage(IRMessage.FREQ_38000_HZ, final_Code, command, address, item);
    }
    
    private static List<Integer> decodeInt(int num, boolean msb, int bits) {
        //0xef == 0b_1110_1111
        List<Integer> values = new ArrayList<>();
        if(msb) {
            for(int i = bits-1; i >= 0; i--) {
                values.add( ((num & (1 << i)) == 0) ? ZERO_MARK : ONE_MARK );
                values.add( ((num & (1 << i)) == 0) ? ZERO_SPACE: ONE_SPACE);
            }
        } else {
            for(int i = 0; i < bits; i++) {
                values.add( ((num & (1 << i)) == 0) ? ZERO_MARK : ONE_MARK );
                values.add( ((num & (1 << i)) == 0) ? ZERO_SPACE: ONE_SPACE);
            }
        }
        return values;
    }
}
