package org.example;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Soundexer {

    public  String encode(String value) {
        String code = encodeString(value);
        code = removeDuplicates(code);
        code = fillOutToFour(code);
        return code;
    }

    public  String fillOutToFour(String code) {

        switch(code.length()) {
            case 1:
                code += "000";
                break;
            case 2:
                code += "00";
                break;
            case 3:
                code += "0";
                break;
        }
        return code;
    }

    public  String encodeString(String value) {
        StringBuilder encoded = new StringBuilder();
        char[] chars = value.toCharArray();
        encoded.append(chars[0]);

        // add first char to head of list
//       int i=0;
//        encoded += (!"aeiou".contains("" + chars[i]) ) ? chars[i]: chars[++i];
//
        for (int i = 1 ; i < chars.length;i++) {
            switch(chars[i]) {
                case 'a':
                case 'e':
                case 'i':
                case 'o':
                case 'u':
                case 'h':
                case 'w':
                case 'y':
                    break;
                case 'b':
                case 'f':
                case 'p':
                case 'v':
                    encoded.append("1");
                    break;
                case 'c':
                case 'g':
                case 'j':
                case 'k':
                case 'q':
                case 's':
                case 'x':
                case 'z':
                    encoded.append("2");
                    break;
                case 'd':
                case 't':
                    encoded.append("3");
                    break;
                case 'l':
                    encoded.append("4");
                    break;
                case 'm':
                case 'n':
                    encoded.append("5");
                    break;
                case 'r':
                    encoded.append("6");
                    break;


            }
        }
        return encoded.toString();
    }

    public  String removeDuplicates(String value) {
        StringBuilder encoded = new StringBuilder();
        char[] chars = value.toCharArray();

        char prev = chars[0];
        encoded.append(prev);
        for(int i = 1 ; i<chars.length; i++) {
            if(prev != chars[i]) {
                prev = chars[i];
                encoded.append(chars[i]);
            }
        }
        return encoded.toString();
    }
}
