package mellek;

import java.util.Scanner;

/**
 * Created by qqcs on 26/11/16.
 */
public class Task2 {
    public static void main(String[] args) {
        String[] code = new Scanner(System.in).nextLine().split(" ");
        int rotate = 'T' - code[0].charAt(0) + 26;

        String[] digits = code[code.length - 1].split("-");

        for(String digit : digits) {
            char[] chars = new char[digit.length()];

            for(int i = 0; i < digit.length(); ++i) {
                chars[i] = (char) (((digit.charAt(i) - 'a') + rotate) % 26 + 'a');
            }
            switch (new String(chars)) {
                case "zero":
                    System.out.print("0");
                    break;
                case "one":
                    System.out.print("1");
                    break;
                case "two":
                    System.out.print("2");
                    break;
                case "three":
                    System.out.print("3");
                    break;
                case "four":
                    System.out.print("4");
                    break;
                case "five":
                    System.out.print("5");
                    break;
                case "six":
                    System.out.print("6");
                    break;
                case "seven":
                    System.out.print("7");
                    break;
                case "eight":
                    System.out.print("8");
                    break;
                case "nine":
                    System.out.print("9");
                    break;
                default:
                    System.out.println("BAD CODE: " + new String(chars));
            }
        }
        System.out.println();
    }
}
