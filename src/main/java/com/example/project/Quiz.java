package com.example.project;

import java.io.*;
import java.util.Scanner;

public class Quiz {
    private String username;
    private String password;
    private String quizName;
    private String[] questionIDs;
    private int no_questions;
    static int quizId = 0;

    public Quiz(String quizName, String[] questionIDs, int no_questions, String username, String password) {
        this.quizName = quizName;
        this.questionIDs = questionIDs;
        this.no_questions = no_questions;
        this.username = username;
        this.password = password;
    }

    public String getQuizName() {
        return quizName;
    }

    public String[] getQuestionIDs() {
        return questionIDs;
    }

    public static int getQuizId() {
        return quizId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static void createQuiz(String[] args, File file1, File file2, File file3) {
        if (!Tema1.checkAuthentication(args, file1)) {
            return;
        }
        /* we treat the case in which there are more than 10 questions;
        4 represents the number of flags that do not refer to the question ids */
        int no_of_questions = args.length - 4;
        if (no_of_questions > 10) {
            System.out.println("{ 'status' : 'error', 'message' : 'Quizz has more than 10 questions'}");
            return;
        }

        String quizName = args[3].substring(args[3].indexOf("'") + 1, args[3].lastIndexOf("'"));
        String username = args[1].substring(args[1].indexOf("'") + 1, args[1].lastIndexOf("'"));
        String password = args[2].substring(args[2].indexOf("'") + 1, args[2].lastIndexOf("'"));

        /* function that checks if the ids of the questions are in the file*/
        String[] questionIds = checkExistenceofIds(args, file2, no_of_questions);
        if (questionIds == null) return;

        /* we check to see if the quiz name already exists */
        if (FileModifier.findQuizNameInFile(file3, quizName) != -1) {
            System.out.print("{ 'status' : 'error', 'message' : 'Quizz name already exists'}");
            return;
        }

        /* we create the quiz and add it to the file */
        Quiz quiz = new Quiz(quizName, questionIds, no_of_questions, username, password);
        quizId++;
        if (FileModifier.addQuizToFile(file3, quiz)) {
            System.out.println("{ 'status' : 'ok', 'message' : 'Quizz added succesfully'}");
        }
    }

    /* we check to see if the question ids exist with the function findQuestionIdInFile */
    private static String[] checkExistenceofIds(String[] args, File file2, int no_of_questions) {
        String[] questionIds = new String[no_of_questions];
        for (int i = 0; i < questionIds.length; i++) {
            questionIds[i] = args[i + 4].substring(args[i + 4].indexOf("'") + 1, args[i + 4].lastIndexOf("'"));
            if (!FileModifier.findQuestionIdInFile(file2, questionIds[i])) {
                System.out.println("{ 'status' : 'error', 'message' : 'Question ID for question " + (i + 1) + " does not exist'}");
                return null;
            }
        }
        return questionIds;
    }

    /* function that shows the id of a quiz depending on its name */
    public static void getQuizByName(String[] args, File file1, File file2, File file3) {
        if (!Tema1.checkAuthentication(args, file1)) {
            return;
        }
        /* extract the text string from args then enter the findQuizNameInFile and return quizId */
        String quizName = args[3].substring(args[3].indexOf("'") + 1, args[3].lastIndexOf("'"));
        int quizId = FileModifier.findQuizNameInFile(file3, quizName);
        if (quizId == -1) {
            System.out.println("{ 'status' : 'error', 'message' : 'Quizz does not exist'}");
        } else {
            System.out.println("{ 'status' : 'ok', 'message' : '" + quizId + "'}");
        }
    }

    /* we extract all the questions stored in the file */
    public static void getAllQuizzes(String[] args, File file1, File file2, File file3) {
        if (!Tema1.checkAuthentication(args, file1)) {
            return;
        }

        /* we read everything from the file */
        System.out.print("{ 'status' : 'ok', 'message' :'[");

        /* the quizArray array represents all the elements stored on that line;
         we also avoid the first line in the file, the one that contains the header */
        try {
            Scanner myReader = new Scanner(file3);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] quizArray = data.split(",");
                if (quizArray[0].equals("QuizID")) {
                    continue;
                }
                System.out.print("{\"quizz_id\" : \"" + quizArray[0] + "\", " + "\"quizz_name\" : \"" + quizArray[1] + "\", \"is_completed\" : \"False\"}");
                if (myReader.hasNextLine()) {
                    System.out.print(", ");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.print("]'}");
    }

    /* function that returns details about the quiz such as quiz name and the infos about its containing questions */
    public static void getQuizzDetailsById(String[] args, File file1, File file2, File file3) {
        if (!Tema1.checkAuthentication(args, file1)) {
            return;
        }

        /* we read the quiz id and check to see if it exists in the file */
        String quizId = args[3].substring(args[3].indexOf("'") + 1, args[3].lastIndexOf("'"));
        if (file3.exists()) {
            try {
                Scanner myReader = new Scanner(file3);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    int commaIndex = line.indexOf(",");
                    String currentId = line.substring(0, commaIndex);

                    if (currentId.equals(quizId)) {
                        /* we found the quiz, now we read the questions */
                        String[] linesplit = line.split(",");
                        String[] questionIds = new String[linesplit.length - 4];
                        for (int i = 0; i < questionIds.length; i++) {
                            questionIds[i] = linesplit[i + 4];
                        }
                        System.out.print("{ 'status' : 'ok', 'message':'[");

                        /* depending on the question ids, we enter the question file and read details about the question
                        that is on the quiz, such as question_index, question_name, question_type and the possible answer */
                        for (int i = 0; i < questionIds.length; i++) {
                            readDetailsInQuestionFile(file2, questionIds, i);
                        }
                        System.out.println("]'}");
                        myReader.close();
                        return;
                    }
                }
                myReader.close();

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    /* reads from the question file the names of the questions and the possible answers on that question */
    private static void readDetailsInQuestionFile(File file2, String[] questionIds, int i) {
        try {
            Scanner myReader2 = new Scanner(file2);
            while (myReader2.hasNextLine()) {
                String data = myReader2.nextLine();
                String[] questionArray = data.split(",");
                if (questionArray[0].equals("QuestionID")) {
                    continue;
                }
                if (questionArray[0].equals(questionIds[i])) {
                    System.out.print("{\"question-name\":\"" + questionArray[1] + "\", \"question_index\":\"" + questionArray[0] + "\", " + "\"question_type\":\"" + questionArray[2] + "\", \"answers\":\"[");
                    for (int j = 3; j < questionArray.length; j += 3) {
                        System.out.print("{\"answer_name\":\"" + questionArray[j + 1] + "\", \"answer_id\":\"" + questionArray[j] + "\"}");
                        if (j != questionArray.length - 3) {
                            System.out.print(", ");
                        } else {
                            System.out.print("]\"}");
                        }
                    }
                    if (i != questionIds.length - 1) {
                        System.out.print(", ");
                    }
                }
            }
            myReader2.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
