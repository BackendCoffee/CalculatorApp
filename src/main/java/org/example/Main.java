package org.example;

import org.example.Matrix.MatrixClass;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Pick your app...");
            System.out.println("""
                    1. Matrix App
                    2. Calculator App
                    Choice - [ 1 or 2 ] - """);
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("1")) {
                MatrixClass.main(args);
            } else if (choice.equalsIgnoreCase("2")) {
                break;
            } else {
                System.out.println("\u001B[m32Invalid choice. Please try again.");
            }
            System.out.println("Exiting program...");
            scanner.close();
        }

        System.out.println("=== Simple Calculator ===");

        while (true) {
            System.out.println("\nChoose operation: + - * / or type 'exit' to quit");
            String operation = scanner.next();

            if (operation.equalsIgnoreCase("exit")) {
                System.out.println("Exiting calculator...");
                break;
            }

            System.out.print("Enter first number: ");
            double num1 = scanner.nextDouble();

            System.out.print("Enter second number: ");
            double num2 = scanner.nextDouble();

            double result;

            switch (operation) {
                case "+":
                    result = calculator.add(num1, num2);
                    break;
                case "-":
                    result = calculator.subtract(num1, num2);
                    break;
                case "*":
                    result = calculator.multiply(num1, num2);
                    break;
                case "/":
                    result = calculator.divide(num1, num2);
                    break;
                default:
                    System.out.println("Invalid operation.");
                    continue;
            }

            System.out.println("Result: " + result);
        }

        scanner.close();
    }
}
