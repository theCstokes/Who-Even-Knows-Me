package com.hintdesk.Twitter_oAuth.Encoding;

/**
 * Created by Christopher Stokes on 08/07/14.
 */
public class LetterUtils {
    private static String[] values = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z", " ", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "0", "!", "@", "#", "%", "‡", "&", "»", "§", "«", "-", "_",
            "=", "±", "°", "}", "¡", "<", ">", "~", "/", "¨", "`", "\"",
            "¿", "]", ";", "¤", "¥", "¬", "¶", "."};

    public static String getLetterWithIndexOf(int index){
        if(values.length >= index - 1){
            return values[(index - 1)];
        } else {
            return "-1";
        }
    }

    public static int getIndexOfLetter(String letter){
        int counter = 0;
        for(String s : values){
            counter ++;
            if(s.equals(letter.toLowerCase())){
                return counter;
            }
        }
        return -1;
    }
}
