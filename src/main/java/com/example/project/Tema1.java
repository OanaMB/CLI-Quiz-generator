package com.example.project;

import java.io.*;

public class Tema1 {
    public static void main(final String[] args) {
        if (args == null) {
            System.out.print("Hello world!");
            return;
        }
        /* initialize the file objects */
        File file1 = new File("users.csv");
        File file2 = new File("questions.csv");
        File file3 = new File("quizzes.csv");
        File file4 = new File("results.csv");

        /* check the command flag and execute the described action */
        switch (args[0]) {
            case "-create-user": {
                if (!(file1.exists())) {
                    FileModifier.createFile(file1, "Username,Password");
                }
                User.createUser(args, file1);
                break;
            }
            case "-create-question": {
                if (!file2.exists()) {
                    FileModifier.createFile(file2, "QuestionID,Text,Type,A1,TextA1,Truth_value,A2,TextA2,Truth_value," +
                            "A3,TextA3,Truth_value,A4,TextA4,Truth_value,A5,TextA5,Truth_value");
                }
                Questions.createQuestion(args, file1, file2);
                break;
            }
            case "-get-question-id-by-text": {
                Questions.getQuestionIdByText(args, file1, file2);
                break;
            }
            case "-get-all-questions": {
                Questions.getAllQuestions(args, file1, file2);
                break;
            }
            case "-create-quizz": {
                if (!(file3.exists())) {
                    FileModifier.createFile(file3, "QuizID,QuizzName,Username,Password,Question1ID,Question2ID,Question3ID," +
                            "Question4ID,Question5ID,Question6ID,Question7ID,Question8ID,Question9ID,Question10ID");
                }
                Quiz.createQuiz(args, file1, file2, file3);
                break;
            }
            case "-get-quizz-by-name": {
                Quiz.getQuizByName(args, file1, file2, file3);
                break;
            }
            case "-get-all-quizzes": {
                Quiz.getAllQuizzes(args, file1, file2, file3);
                break;
            }
            case "-get-quizz-details-by-id": {
                Quiz.getQuizzDetailsById(args, file1, file2, file3);
                break;
            }
            case "-submit-quizz": {
                if (!(file4.exists())) {
                    FileModifier.createFile(file4, "Index_in_List,Username,Password,QuizID,Points");
                }
                Submits.submitQuiz(args, file1, file2, file3, file4);
                break;
            }
            case "-delete-quizz-by-id": {
                Submits.deleteQuizzById(args, file1, file2, file3);
                break;
            }
            case "-get-my-solutions": {
                Submits.getMySolutions(args, file1, file2, file3, file4);
                break;
            }
            case "-cleanup-all": {
                /* reset the indexes of the IDs*/
                Questions.questionId = 0;
                Questions.answersId = 0;
                Quiz.quizId = 0;
                Submits.index_in_list = 0;
                //System.out.print("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully'}");

                /* delete the files */
                if (file1.exists()) {
                    file1.delete();
                }
                if (file2.exists()) {
                    file2.delete();
                }
                if (file3.exists()) {
                    file3.delete();
                }
                if (file4.exists()) {
                    file4.delete();
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid command");
            }
        }
    }

    /* used for multiple logins for different tasks: from args, we extract the flags and the arguments
    if there is no -u or -p flag contained, the user isn't authenticated, and if we do not find the user:
    username+password, the login fails */
    public static boolean checkAuthentication(String[] args, File file) {
        /* either the username or password or both are not provided */
        if (args.length < 2 || !args[1].contains("-u") || args.length < 3 || !args[2].contains("-p")) {
            System.out.print("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return false;
        }

        String username = args[1].substring(args[1].indexOf("'") + 1, args[1].lastIndexOf("'"));
        String password = args[2].substring(args[2].indexOf("'") + 1, args[2].lastIndexOf("'"));

        User user = new User(username, password);
        /* the user does not exist in the database */
        if (!FileModifier.findUserInFile(file, user)) {
            System.out.print("{ 'status' : 'error', 'message' : 'Login failed'}");
            return false;
        }

        return true;
    }
}
