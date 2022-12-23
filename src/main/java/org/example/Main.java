package org.example;

import jdk.jfr.Description;
import jdk.jfr.Name;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.RefinedSoundex;
import org.apache.commons.codec.language.Soundex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

//    LoadTestWords words = new LoadTestWords();
    public static Soundex dex = new Soundex();
    public static Soundexer dexer = new Soundexer();
    public static RefinedSoundex rDex = new RefinedSoundex();

    public static List<String>  success = new ArrayList<>();
    public static List<String> playerNumbers = new ArrayList<>();
    public static List<String>  statNames = new ArrayList<>();

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
    final static int REFINED_ENCODER = 3;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Soundex dex = new Soundex();

        Soundexer dexer = new Soundexer();
        RefinedSoundex rDex = new RefinedSoundex();
        loadTestWords();
        int encoder = OWN_ENCODER;

        loadSoundexArrays(dex ,dexer, rDex, encoder);

        singleWordDiffChecks();

//        int totalCount = totalCount(first, second, third, firsttwowords);
//        int found = checkTestWords(dex,dexer,rDex,encoder);
//
//        float result = (float) found/totalCount * 100;
//        System.out.println("Found: "+found+ " Total: " + totalCount + " Percent: " + result + " %");
    }

    private static void singleWordDiffChecks() {
        // using 'first' words and playerNumberSoundexes

        for(String word : first) {
            String wordDex = dex.encode(word);
            String wordDexer = dexer.encode(word);
            String wordRdex = rDex.encode(word);

            for(String wordToMatch : playerNumbers) {
                String wordToMatchDex = dex.encode(wordToMatch);
                String wordToMatchDexer = dexer.encode(wordToMatch);
                String wordToMatchRdex = rDex.encode(wordToMatch);
                int rating = difference(wordDexer,wordToMatchDexer );

                try {
                    String[] array = new String[]{
                            word,
                            "Dex "+wordToMatch+"->"+wordDex+"->"+wordToMatchDex,
                            "" + dex.difference(wordDex, wordToMatchDex),
                            "Dexer "+wordToMatch+"->"+wordDexer+"->"+wordToMatchDexer,
                            "" + dex.difference(wordDexer, wordToMatchDexer),
                            "rDex "+wordToMatch+"->"+wordRdex+"->"+wordToMatchRdex,
                            "" + dex.difference(wordRdex, wordToMatchRdex),
                            "diff "+wordToMatch+"->"+wordDexer+"->"+wordToMatchDexer,
                            "" + difference(wordDexer, wordToMatchDexer),
                    };
                    System.out.println(String.join(" - ", array));
                } catch (EncoderException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }


    private static int difference(String word1, String word2) {
        char[] wordChars1 = word1.toCharArray();
        char[] wordChars2 = word2.toCharArray();
        int rating = 4;

        // take in soundex strings of testwords and dictionary words
        // starting with a rating of 4, count down where index values are different - cap at zero.


        for(int i = 0; i<wordChars1.length;i++) {
            if (wordChars1[i] != wordChars2[i]) {
                rating--;
            }
        }

        return Math.max(rating, 0);
    }



    private static void checkFirst(String testWord, List<String> first, Soundex dex) {

        HashMap<String, Integer> sounds = new HashMap<>();


        first.forEach(word -> {
//            sounds.put(word, dex.soundex(testWord));
            System.out.println(word + " - " + dex.soundex(word) + " - " + dex.encode(word));
        });

//        first.forEach(word -> System.out.println(word + " - " + sounds.get(word)));


    }

    private static int checkTestWords(Soundex dex, Soundexer dexer, RefinedSoundex rDex, int encoder) {
         int f = 0;
         switch(encoder) {
             case APACHE_ENCODER:
                 f  = countSuccess(dex, first, playerNumberSoundexes);
                 f += countSuccess(dex, second, statNameSoundexes);
                 f += countSuccess(dex, third, successSoundexes);
                 f += countSuccess(dex, firsttwowords, successSoundexes);
                 f += countSuccess(dex, third, onePLusTwoSoundexes);
                 break;
             case OWN_ENCODER:
                 f  = countSuccess(dexer, first, playerNumberSoundexes);
                 f += countSuccess(dexer, second, statNameSoundexes);
                 f += countSuccess(dexer, third, successSoundexes);
                 f += countSuccess(dexer, firsttwowords, successSoundexes);
                 f += countSuccess(dexer, third, onePLusTwoSoundexes);
                 break;
             case REFINED_ENCODER:
                 f  = countSuccess(rDex, first, playerNumberSoundexes);
                 f += countSuccess(rDex, second, statNameSoundexes);
                 f += countSuccess(rDex, third, successSoundexes);
                 f += countSuccess(rDex, firsttwowords, successSoundexes);
                 f += countSuccess(rDex, third, onePLusTwoSoundexes);
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
        System.out.println();
        return f;
    }
    private static int countSuccess(Soundexer dex, List<String> list, HashMap<String, String> soundexes) {
        int f = 0;
        for(String word : list) {
            String dexword = dex.encode(word);
            System.out.println(word + " -> " + soundexes.get(dexword));
            if(soundexes.get(dexword)!=null) f++;
        };
        System.out.println();
        return f;
    }
    private static int countSuccess(RefinedSoundex dex, List<String> list, HashMap<String, String> soundexes) {
        int f = 0;
        for(String word : list) {
            String dexword = dex.encode(word);
            System.out.println(word + " -> " + soundexes.get(dexword));
            if(soundexes.get(dexword)!=null) f++;
        };
        System.out.println();
        return f;
    }

    private static void loadTestWords() {
        first  = Arrays.asList("one", "do", "to", "you", "three", "very", "four", "or", "for");
        second = Arrays.asList("catch","cats","thats", "black", "block", "goal", "go");
        third  = Arrays.asList("true","trill", "fart", "moles", "Holes", "thirty", "valves", "true", "through");

        firsttwowords  = Arrays.asList("uncatch","reebok", "oracle", "fargo", "golfer", "onecatch","oncatch");
        secondtwowords = Arrays.asList("castro", "blackpool");

    }


    public static void loadSoundexArrays(Soundex dex, Soundexer dexer, RefinedSoundex rDex, int encoder ) {

        // player numbers
        playerNumbers = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine","ten",
                "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen","twenty",
                "twentyone", "twentytwo", "twentythree","twentyfour", "twentyfive", "twentysix", "twentyseven", "twentyeight", "twentynine", "thirty");

        // stat names
        statNames = Arrays.asList("Block","Catch","FreePass","FreeScore","Hook","Handpass","PassBlocked","PassLong",
                "PuckOut","PuckOutOpp","PassShort","Ruck","SaveBody","SaveCatch","ScoreGoal","ScorePoint",
                "ScoreSideLine","SaveHurl","ScoreMiss","Solo","SoloPass","SoloScore","Standup","SubstituteOff","SubstituteOn");

        // success
        success = Arrays.asList("true","false","win","loss","one","zero");

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
            case OWN_ENCODER: // homemade encoder
                playerNumbers.forEach( player ->  playerNumberSoundexes.put( dexer.encode( player ), player ) );
                statNames.forEach( stat       ->  statNameSoundexes.put( dexer.encode( stat ), stat) );
                success.forEach( outcome      ->  successSoundexes.put( dexer.encode( outcome ), outcome ) );

                onePlusTwo.forEach( word   -> onePLusTwoSoundexes.put( dexer.encode(word), word ));
                twoPlusThree.forEach( word -> twoPlusThreeSoundexes.put( dexer.encode(word), word ));
                allThree.forEach( word     -> allThreeSoundexes.put( dexer.encode(word), word ));
                break;
            case REFINED_ENCODER: // homemade encoder
                playerNumbers.forEach( player ->  playerNumberSoundexes.put( rDex.encode( player ), player ) );
                statNames.forEach( stat       ->  statNameSoundexes.put( rDex.encode( stat ), stat) );
                success.forEach( outcome      ->  successSoundexes.put( rDex.encode( outcome ), outcome ) );

                onePlusTwo.forEach( word   -> onePLusTwoSoundexes.put( rDex.encode(word), word ));
                twoPlusThree.forEach( word -> twoPlusThreeSoundexes.put( rDex.encode(word), word ));
                allThree.forEach( word     -> allThreeSoundexes.put( rDex.encode(word), word ));
                break;
        }
    }
}