package com.hintdesk.Twitter_oAuth.Encoding;

/**
 * Created by Christopher Stokes on 08/07/14.
 */
public class CipherUtils {

    public static String encode(String tweet){
        if(judge(tweet)){
            String compressed = compress(tweet);
            System.out.println("Compress : " + compressed);
            String encrypted = CipherUtils.rot(compressed);
            System.out.println("Encrypted : " + encrypted);
            return encrypted;
        } else {
            System.out.println("Bad Input");
            return "-1";
        }
    }

    public static String decode(String tweet){
            String decrypted = CipherUtils.rot(tweet);
            System.out.println("Decrypted : " + decrypted);
            String expaned = expand(decrypted);
            System.out.println("Expand : " + expaned);
            return expaned;
    }

    public static String rot(String tweet){
        char[] data = tweet.toCharArray();
        String out = "";

        for(Character c : data){
            if(!"-1".equals(LetterUtils.getIndexOfLetter(c.toString()))){
                out += LetterUtils.getLetterWithIndexOf(((LetterUtils.getIndexOfLetter(c.toString()) + 34) % 68));
            } else {
                out += c.toString();
            }
        }
        return out;
    }

    private static String compress(String tweet){
        String out;
        out = tweet.replaceAll("(?i)en", "!");
        out = out.replaceAll("(?i)re", "@");
        out = out.replaceAll("(?i)er", "#");
        out = out.replaceAll("(?i)nt", "%");
        out = out.replaceAll("(?i)th", "‡");
        out = out.replaceAll("(?i)on", "&");
        out = out.replaceAll("(?i)in", "»");
        out = out.replaceAll("(?i)te", "§");
        out = out.replaceAll("(?i)an", "«");
        out = out.replaceAll("(?i)or", "-");
        out = out.replaceAll("(?i)st", "_");
        out = out.replaceAll("(?i)ed", "=");
        out = out.replaceAll("(?i)ne", "±");
        out = out.replaceAll("(?i)ve", "°");
        out = out.replaceAll("(?i)es", "}");
        out = out.replaceAll("(?i)nd", "¡");
        out = out.replaceAll("(?i)to", "<");
        out = out.replaceAll("(?i)se", ">");
        out = out.replaceAll("(?i)at", "~");
        out = out.replaceAll("(?i)ti", "/");
        out = out.replaceAll("(?i)ar", "¨");
        out = out.replaceAll("(?i)ee", "`");
        out = out.replaceAll("(?i)rt", "\"");
        out = out.replaceAll("(?i)as", "¿");
        out = out.replaceAll("(?i)co", "]");
        out = out.replaceAll("(?i)io", ";");
        out = out.replaceAll("(?i)ty", "¤");
        out = out.replaceAll("(?i)fo", "¥");
        out = out.replaceAll("(?i)fi", "¬");
        out = out.replaceAll("(?i)ra", "¶");
        out.toUpperCase();
        return out;
    }

    private static String expand(String tweet){
        String out;
        out = tweet.replaceAll("!", "en");
        out = out.replaceAll("@", "re");
        out = out.replaceAll("#", "er");
        out = out.replaceAll("%", "nt");
        out = out.replaceAll("‡", "th");
        out = out.replaceAll("&", "on");
        out = out.replaceAll("»", "in");
        out = out.replaceAll("§", "te");
        out = out.replaceAll("«", "an");
        out = out.replaceAll("-", "or");
        out = out.replaceAll("_", "st");
        out = out.replaceAll("=", "ed");
        out = out.replaceAll("±", "ne");
        out = out.replaceAll("°", "ve");
        out = out.replaceAll("\\}", "es");
        out = out.replaceAll("¡", "nd");
        out = out.replaceAll("<", "to");
        out = out.replaceAll(">", "se");
        out = out.replaceAll("~", "at");
        out = out.replaceAll("/", "ti");
        out = out.replaceAll("¨", "ar");
        out = out.replaceAll("`", "ee");
        out = out.replaceAll("\"", "rt");
        out = out.replaceAll("¿", "as");
        out = out.replaceAll("]", "co");
        out = out.replaceAll(";", "io");
        out = out.replaceAll("¤", "ty");
        out = out.replaceAll("¥", "fo");
        out = out.replaceAll("¬", "fi");
        out = out.replaceAll("¶", "ra");
        out.toUpperCase();
        return out;
    }

    private static Boolean judge(String tweet){
        if(tweet.contains("!") || tweet.contains("@") || tweet.contains("#") || tweet.contains("%") ||
                tweet.contains("‡") || tweet.contains("&") || tweet.contains("»") || tweet.contains("§") ||
                tweet.contains("«") || tweet.contains("-") || tweet.contains("_") || tweet.contains("=") ||
                tweet.contains("±") || tweet.contains("°") || tweet.contains("}") || tweet.contains("¡") ||
                tweet.contains("<") || tweet.contains(">") || tweet.contains("~") || tweet.contains("/") ||
                tweet.contains("¨") || tweet.contains("`") || tweet.contains("\"") || tweet.contains("¿") ||
                tweet.contains("]") || tweet.contains(";") || tweet.contains("¤") || tweet.contains("¥") ||
                tweet.contains("¬") || tweet.contains("¶")){
            return false;
        } else {
            return true;
        }
    }
}
