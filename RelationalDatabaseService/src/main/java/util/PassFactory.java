package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by liweiqi on 2014/10/24.
 */
public class PassFactory {
    private static char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int MIN_LENGTH = 6;
    private static final int MIN_NUMLENGTH = 3;
    private static final int MIN_LETTERLEN = 3;

    public static String createRandomPass() {
        return createRandomPass(MIN_LENGTH);
    }
    public static String createRandomPass(int length) {
        Random random = new Random();
        if (length < MIN_LENGTH) length = MIN_LENGTH;
        int numLength = random.nextInt(length);
        numLength = numLength > length - MIN_LETTERLEN ? length - MIN_LETTERLEN : numLength;
        numLength = numLength < MIN_NUMLENGTH ? MIN_NUMLENGTH : numLength;
        List<Character> li = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            if (i < numLength) {
                li.add(numbers[random.nextInt(numbers.length)]);
            } else {
                char letter = alphabet[random.nextInt(alphabet.length)];
                if (i % 2 == 0) {
                    letter = (letter + "").toUpperCase().charAt(0);
                }
                li.add(letter);
            }
        }
        Collections.shuffle(li);
        StringBuilder sb = new StringBuilder();
        for (Character ch : li) sb.append(ch);
        return sb.toString();
    }
}
