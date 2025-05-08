package org.example.Matrix;

import lombok.*;

import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

import static org.example.Main.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Verifier {
    Scanner sc = new Scanner(System.in);
    private String email;
    private String username;
    private String password;

    public String validateInput(String prompt, String pattern, String errMessage) {
        while (true) {
            String input = printStr(prompt);
            try {
                if (input.matches(pattern)) {
                    return input;
                } else {
                    System.out.println(errMessage);
                }
            } catch (PatternSyntaxException e) {
                System.out.println("Pattern error: " + e.getMessage() + "");
            }
        }
    }

    public void validateApplicant() {
        boolean isValid;
        do {
            isValid = true;

            email = validateInput("Enter your email: ", "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}",
                    "\u001B[31mEmail must contain an '@' and must be valid\u001B[0m");

            username = validateInput("Enter your username: ", "[a-zA-Z0-9]{3,15}",
                    "\u001B[31mUsername must be between 5 and 15 characters\u001B[0m");

            password = validateInput("Enter your password: ", "^(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,16}",
                    "\u001B[31mPassword must include upper case letter and number and must be between 8 and 15 characters\u001B[0m");

            if (email != null && username != null && password != null) {
                System.out.println("\u001B32mApplicant is valid!");
                isValid = false;
            }
        } while (isValid);
    }

    public String printStr(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

}

