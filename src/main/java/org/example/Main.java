package org.example;

import org.apache.commons.codec.language.Soundex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static HashMap<String , String> onePLusTwoSoundexes   = new HashMap<>();
    public static HashMap<String , String> twoPlusThreeSoundexes = new HashMap<>();
    public static  HashMap<String, String> allThreeSoundexes     = new HashMap<>();

    public static  HashMap<String, String> playerNumberSoundexes = new HashMap<>();
    public static  HashMap<String, String> statNameSoundexes     = new HashMap<>();
    public static HashMap<String , String> successSoundexes      = new HashMap<>();

    public static List<String> first          = new ArrayList<>();
    public static List<String> second         = new ArrayList<>();
    public static List<String> third          = new ArrayList<>();

    public static List<String> firsttwowords  = new ArrayList<>();
    public static List<String> secondtwowords = new ArrayList<>();
    public static List<String> allthreewords  = new ArrayList<>();

    final static int APACHE_ENCODER = 1;
    final static int OWN_ENCODER = 2;


    public static void main(String[] args) {
        System.out.println("Hello world!");
        Soundex dex = new Soundex();
        Soundexer dexer = new Soundexer();
        loadTestWords();

        int encoder = OWN_ENCODER;

        loadSoundexArrays(dex,dexer, encoder);
        int totalCount = totalCount(first, second, third, firsttwowords);
        int found = checkTestWords(dex,dexer,encoder);

        float result = (float) found/totalCount * 100;
        System.out.println("Found: "+found+ " Total: " + totalCount + " Percent: " + result + " %");
    }

    private static int checkTestWords(Soundex dex, Soundexer dexer, int encoder) {
         int f = 0;
         switch(encoder) {
             case APACHE_ENCODER:
                 f = countSuccess(dex, first, playerNumberSoundexes);
                 f += countSuccess(dex, second, statNameSoundexes);
                 f += countSuccess(dex, third, successSoundexes);
                 f += countSuccess(dex, firsttwowords, successSoundexes);
                 f += countSuccess(dex, third, onePLusTwoSoundexes);
                 break;
             case OWN_ENCODER:
                 f = countSuccess(dexer, first, playerNumberSoundexes);
                 f += countSuccess(dexer, second, statNameSoundexes);
                 f += countSuccess(dexer, third, successSoundexes);
                 f += countSuccess(dexer, firsttwowords, successSoundexes);
                 f += countSuccess(dexer, third, onePLusTwoSoundexes);
                 break;
         }
        return f;
    }

    private static int totalCount(List<String> first, List<String> second, List<String> third, List<String> firsttwowords) {
        return  first.size() + second.size() + third.size() + firsttwowords.size();
    }

    private static int countSuccess(Soundex dex, List<String> list, HashMap<String, String> soundexes) {
        int f = 0;
        for(String word : list) {
            String dexword = dex.encode(word);
            System.out.println(word + " -> " + soundexes.get(dexword));
            if(soundexes.get(dexword)!=null) f++;
        };
        return f;
    }

    private static int countSuccess(Soundexer dex, List<String> list, HashMap<String, String> soundexes) {
        int f = 0;
        for(String word : list) {
            String dexword = dex.encode(word);
            System.out.println(word + " -> " + soundexes.get(dexword));
            if(soundexes.get(dexword)!=null) f++;
        };
        return f;
    }


    private static void loadTestWords() {
        first  = Arrays.asList("one", "do", "to", "you", "three", "very", "four", "or", "for");
        second = Arrays.asList("catch","cats","thats", "black", "block", "goal", "go");
        third  = Arrays.asList("true","trill", "fart", "moles", "Holes", "thirty", "valves", "true", "through");

        firsttwowords  = Arrays.asList("uncatch","reebok", "oracle", "fargo", "golfer", "onecatch","oncatch");
        secondtwowords = Arrays.asList("castro", "blackpool");

    }

    public static void loadSoundexArrays(Soundex dex, Soundexer dexer, int encoder) {

        // player numbers
        List<String> playerNumbers = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine","ten",
                "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen","twenty",
                "twentyone", "twentytwo", "twentythree","twentyfour", "twentyfive", "twentysix", "twentyseven", "twentyeight", "twentynine", "thirty");

        // stat names
        List<String>  statNames = Arrays.asList("Block","Catch","FreePass","FreeScore","Hook","Handpass","PassBlocked","PassLong",
                "PuckOut","PuckOutOpp","PassShort","Ruck","SaveBody","SaveCatch","ScoreGoal","ScorePoint",
                "ScoreSideLine","SaveHurl","ScoreMiss","Solo","SoloPass","SoloScore","Standup","SubstituteOff","SubstituteOn");

        // success
        List<String>  success = Arrays.asList("true","false","win","loss","one","zero");

        List<String> onePlusTwo = new ArrayList<>();
        List<String> twoPlusThree = new ArrayList<>();
        List<String> allThree = new ArrayList<>();

        // combining first and second words
        for(int i = 0; i < playerNumbers.size();i++) {
            for (int j = 0; j < statNames.size(); j++) {
                onePlusTwo.add(playerNumbers.get(i)+statNames.get(j));
            }
        }
        // combining 2nd two
        for(int i = 0; i < statNames.size();i++) {
            for (int j = 0; j < success.size(); j++) {
                twoPlusThree.add(statNames.get(i)+success.get(j));
            }
        }
        // combining all three
        for(int i = 0; i < playerNumbers.size();i++) {
            for (int j = 0; j < statNames.size(); j++) {
                for (int k = 0; k < success.size(); k++) {
                    allThree.add(playerNumbers.get(i)+statNames.get(j)+success.get(k));
                }
            }
        }

        switch(encoder) {
            case APACHE_ENCODER: // apache encoder
                playerNumbers.forEach( player ->  playerNumberSoundexes.put( dex.encode( player ), player ) );
                statNames.forEach( stat       ->  statNameSoundexes.put( dex.encode( stat ), stat) );
                success.forEach( outcome      ->  successSoundexes.put( dex.encode( outcome ), outcome ) );

                onePlusTwo.forEach( word   -> onePLusTwoSoundexes.put( dex.encode(word), word ));
                twoPlusThree.forEach( word -> twoPlusThreeSoundexes.put( dex.encode(word), word ));
                allThree.forEach( word     -> allThreeSoundexes.put( dex.encode(word), word ));
                break;
            case OWN_ENCODER: // home made encoder
                playerNumbers.forEach( player ->  playerNumberSoundexes.put( dexer.encode( player ), player ) );
                statNames.forEach( stat       ->  statNameSoundexes.put( dexer.encode( stat ), stat) );
                success.forEach( outcome      ->  successSoundexes.put( dexer.encode( outcome ), outcome ) );

                onePlusTwo.forEach( word   -> onePLusTwoSoundexes.put( dexer.encode(word), word ));
                twoPlusThree.forEach( word -> twoPlusThreeSoundexes.put( dexer.encode(word), word ));
                allThree.forEach( word     -> allThreeSoundexes.put( dexer.encode(word), word ));
                break;
        }
    }
}