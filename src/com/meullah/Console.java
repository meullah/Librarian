package com.meullah;

import java.util.Scanner;

public class Console {
    private static Scanner scanner = new Scanner(System.in);

    public static String read(String prompt) {
        System.out.print(prompt+": ");
        return scanner.nextLine();
    }
}
