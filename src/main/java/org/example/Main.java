package org.example;

import org.apache.commons.codec.language.RefinedSoundex;
import org.apache.commons.codec.language.Soundex;

import java.util.*;

public class Main {

//    LoadTestWords words = new LoadTestWords();
    public static Soundex dex = new Soundex();
    public static Soundexer dexer = new Soundexer();
    public static RefinedSoundex rDex = new RefinedSoundex();

    public static List<String>  success = new ArrayList<>();
    public static List<String> playerNumbers = new ArrayList<>();
    public static List<String>  statNames = new ArrayList<>();

    public static List<String> onePlusTwo = new ArrayList<>();
    public static List<String> twoPlusThree = new ArrayList<>();
    public static List<String> allThree = new ArrayList<>();

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


    final static int APACHE_ENCODER = 1;
    final static int OWN_ENCODER = 2;
    final static int REFINED_ENCODER = 3;
    final static int DIFFERENCES = 4;


    public static void main(String[] args) {
        System.out.println("Hello world!");
        Soundex dex = new Soundex();

        Soundexer dexer = new Soundexer();
        RefinedSoundex rDex = new RefinedSoundex();
        loadTestWords();
        int encoder = DIFFERENCES;

        loadDictionaries();
        loadSoundexArrays(dex ,dexer, rDex, encoder);

        int totalCount = totalCount( first, second, third, firsttwowords );
        int found = checkTestWords(dex,dexer,rDex,encoder);
        System.out.println("Found: "+found+ " Total: " + totalCount + " Percent: " + (float) found/totalCount * 100 + " %");
    }


    private static int checkTestWords(Soundex dex, Soundexer dexer, RefinedSoundex rDex, int encoder) {
         int f = 0;
         switch(encoder) {
             case APACHE_ENCODER:
                 f  = countSuccess(dex, first, playerNumberSoundexes);
                 f += countSuccess(dex, second, statNameSoundexes);
                 f += countSuccess(dex, third, successSoundexes);
                 f += countSuccess(dex, firsttwowords, onePLusTwoSoundexes);
                 f += countSuccess(dex, secondtwowords, twoPlusThreeSoundexes);
                 break;
             case OWN_ENCODER:
                 f  = countSuccess(dexer, first, playerNumberSoundexes);
                 f += countSuccess(dexer, second, statNameSoundexes);
                 f += countSuccess(dexer, third, successSoundexes);
                 f += countSuccess(dexer, firsttwowords, onePLusTwoSoundexes);
                 f += countSuccess(dexer, secondtwowords, twoPlusThreeSoundexes);
                 break;
             case REFINED_ENCODER:
                 f  = countSuccess(rDex, first, playerNumberSoundexes);
                 f += countSuccess(rDex, second, statNameSoundexes);
                 f += countSuccess(rDex, third, successSoundexes);
                 f += countSuccess(rDex, firsttwowords, onePLusTwoSoundexes);
                 f += countSuccess(rDex, secondtwowords, twoPlusThreeSoundexes);
                 break;
             case DIFFERENCES :
                 f  = countSuccess(dexer, first, playerNumbers, DIFFERENCES);
                 f += countSuccess(dexer, second, statNames, DIFFERENCES);
                 f += countSuccess(dexer, third, success, DIFFERENCES);
                 f += countSuccess(dexer, firsttwowords, onePlusTwo, DIFFERENCES);
                 f += countSuccess(dexer, secondtwowords, twoPlusThree, DIFFERENCES);
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
        }
        System.out.println();
        return f;
    }

    private static int countSuccess(Soundexer dex, List<String> list, HashMap<String, String> soundexes) {
        int f = 0;
        for(String word : list) {
            String dexword = dex.encode(word);
            System.out.println(word + " -> " + soundexes.get(dexword));
            if(soundexes.get(dexword)!=null) f++;
        }
        System.out.println();
        return f;
    }

    private static int countSuccess(Soundexer dex, List<String> testWords, List<String> dictionary, int type) {
        int f = 0;

        for(String word : testWords ) {

            HashMap<Integer, List<String>> wordRatings = dexer.getWordRatings(word,dictionary);
            int max = Collections.max(wordRatings.keySet());
            List<String> words = wordRatings.get(max);
            System.out.println(word + " -> " + words);
            if(words.size()<3) f++;
        }

        return f;
    }

    private static int countSuccess(RefinedSoundex dex, List<String> list, HashMap<String, String> soundexes) {
        int f = 0;
        for(String word : list) {
            String dexword = dex.encode(word);
            System.out.println(word + " -> " + soundexes.get(dexword));
            if(soundexes.get(dexword)!=null) f++;
        }
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

    public static void loadDictionaries() {
        // player numbers
        playerNumbers = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine","ten",
                "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen","twenty",
                "twentyone", "twentytwo", "twentythree","twentyfour", "twentyfive", "twentysix", "twentyseven", "twentyeight", "twentynine", "thirty");

        // stat names
        statNames = Arrays.asList("Block","Catch","FreePass","FreeScore","Hook","Handpass","PassBlocked","PassLong",
                "PuckOut","PuckOutOpp","PassShort","Ruck","SaveBody","SaveCatch","ScoreGoal","ScorePoint",
                "ScoreSideLine","SaveHurl","ScoreMiss","Solo","SoloPass","SoloScore","Standup","SubstituteOff","SubstituteOn");

        // success
        success = Arrays.asList("true","false","win","loss","one","zero","On", "Off", "Won", "Lost");

        // combining first and second words
        for (String number : playerNumbers)
            for (String statName : statNames)
                onePlusTwo.add(number + statName);


        // combining 2nd two
        for (String statName : statNames)
            for (String s : success)
                twoPlusThree.add(statName + s);


        // combining all three
        for ( String number :  playerNumbers )
            for ( String statName : statNames )
                for ( String success : success )
                    allThree.add(number + statName + success );


    }

    public static void loadSoundexArrays(Soundex dex, Soundexer dexer, RefinedSoundex rDex, int encoder ) {

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