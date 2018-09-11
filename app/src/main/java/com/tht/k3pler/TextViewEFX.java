package com.tht.k3pler;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class TextViewEFX {
    private static String randomStr = "あたアカサザジズゼゾシスセソキクケコイウエオジャな";
    private String tmpChr = "";
    private Random random = new Random();
    private static int ms1 = 80, ms2 = 60, itCnt = 5;
    private String rnd;

    public void useFX(final TextView textView, final String text){
        textView.setText("");
        for (int i = 0; i < text.length(); i++){
            final int i1 = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int a = 0; a < itCnt; a++){
                        rnd = String.valueOf(
                                randomStr.toLowerCase().charAt(random.nextInt(randomStr.length()))
                        );
                        final int b = a;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (b == itCnt - 1) {
                                        tmpChr += String.valueOf(text.charAt(i1));
                                    } else {

                                        if (i1 == text.length() - 1) {
                                            textView.setText(text);
                                        }else {
                                            textView.setText(tmpChr + rnd);
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, a * ms2);
                    }
                }
            }, i * ms1);
        }
    }
    public String getRandomStr(int len){
        String rnd = "";
        for (int i = 0; i < len; i++){
            rnd += String.valueOf(
                    randomStr.toLowerCase().charAt(random.nextInt(randomStr.length()))
            );
        }
        return rnd;
    }
}
