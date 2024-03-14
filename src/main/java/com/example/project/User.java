package com.example.project;

import java.io.*;

public class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static void createUser(String[] args, File file) {

        /* in case we have no username or password
        we use the contains method to check if the array contains the string -u or -p
        in the second and third element */
        if (args.length < 2 || !args[1].contains("-u")) {
            System.out.print("{ 'status' : 'error', 'message' : 'Please provide username'}");
            return;
        }
        if (args.length < 3 || !args[2].contains("-p")) {
            System.out.print("{ 'status' : 'error', 'message' : 'Please provide password'}");
            return;
        }

        /* we passed this part of the code, so we can create the user object
         we use the substring method to get the strings username and password contained in two  '  */
        String username = args[1].substring(args[1].indexOf("'") + 1, args[1].lastIndexOf("'"));
        String password = args[2].substring(args[2].indexOf("'") + 1, args[2].lastIndexOf("'"));
        User user = new User(username, password);

        /* we use the writeUserToFile method to write the user to the file
        if the user already exists(it is found in the file with the help of findUserInFile method),
        we return an error message */
        if (FileModifier.findUserInFile(file, user)) {
            System.out.print("{ 'status' : 'error', 'message' : 'User already exists'}");
            return;
        }

        /* the user is written succesfully */
        if (FileModifier.writeUserToFile(file, user)) {
            System.out.print("{ 'status' : 'ok', 'message' : 'User created successfully'}");
        }

    }
}
