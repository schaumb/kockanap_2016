package mellek;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by qqcs on 26/11/16.
 */
public class Task1 {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        Stack<Character> brackets = new Stack<>();

        boolean wasError = false;
        int index = 0;
        while(!wasError && index < line.length()) {
            switch (line.charAt(index)) {
                case '(':
                case '{':
                case '[':
                    brackets.push(line.charAt(index));
                    break;
                case ')':
                    if(brackets.size() == 0 || brackets.peek() != '(') {
                        wasError = true;
                    } else {
                        brackets.pop();
                    }
                    break;
                case '}':
                    if(brackets.size() == 0 || brackets.peek() != '{') {
                        wasError = true;
                    } else {
                        brackets.pop();
                    }
                    break;
                case ']':
                    if(brackets.size() == 0 || brackets.peek() != '[') {
                        wasError = true;
                    } else {
                        brackets.pop();
                    }
                    break;
            }
            ++index;
        }
        if(wasError || brackets.size() != 0) {
            System.out.println("HELYTELEN");
        } else {
            System.out.println("HELYES");
        }
    }
}
