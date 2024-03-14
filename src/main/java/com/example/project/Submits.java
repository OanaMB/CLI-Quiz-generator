package com.example.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Submits {
    private String username;
    private String password;
    private String quizId;
    private double score;
    static int index_in_list = 0;

    public Submits(String username, String password, String quizId, int score) {
        this.username = username;
        this.password = password;
        this.quizId = quizId;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getQuizId() {
        return quizId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public static int getIndex_in_list() {
        return index_in_list;
    }

    public static void submitQuiz(String[] args, File file1, File file2, File file3, File file4) {
        if (!Tema1.checkAuthentication(args, file1)) {
            return;
        }

        if (args.length < 4 || !args[3].contains("-quiz-id")) {
            System.out.print("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
            return;
        }

        String username = args[1].substring(args[1].indexOf("'") + 1, args[1].lastIndexOf("'"));
        String password = args[2].substring(args[2].indexOf("'") + 1, args[2].lastIndexOf("'"));
        String quizId = args[3].substring(args[3].indexOf("'") + 1, args[3].lastIndexOf("'"));

        /* function that deals with multiple errors, regarding the existence of the quiz and if it was submitted before */
        if (noQuizFound(args, file3, file4, username, password, quizId)) return;
        /* function that checks if the answer id exists in the file with questions */
        String[] answers_of_user = getStrings(args, file2);
        if (answers_of_user == null) return;
        // we have to check if the answers are correct
        String[] quiz_info = getStringsQuestionsId(file3, quizId);

        submitQuizCorrectly(file2, file4, username, password, quizId, answers_of_user, quiz_info);

    }

    /* function that submits the total score per quiz; the points for the quiz are added depending on the value of an answer
     in the question */
    private static void submitQuizCorrectly(File file2, File file4, String username, String password, String quizId, String[] answers_of_user, String[] quiz_info) {
        String[] questions_of_quiz = new String[quiz_info.length - 4];
        System.arraycopy(quiz_info, 4, questions_of_quiz, 0, questions_of_quiz.length);

        Submits submission = new Submits(username, password, quizId, 0);
        double weight_of_question = 1.00 * 100 / questions_of_quiz.length;

        /* -1000 represents a value of false, a value we ignore */
        for (int i = 0; i < answers_of_user.length; i++) {
            double score_for_one_answer = FileModifier.checkAnswerInFile(file2, questions_of_quiz, answers_of_user[i]);
            if (score_for_one_answer != -1000) {
                submission.setScore(submission.getScore() + weight_of_question * score_for_one_answer);
            }
        }
        /* we round the results */
        if (submission.getScore() <= 0) {
            submission.setScore(0);
        } else {
            submission.setScore(Math.round(submission.getScore()));
        }

        // conversion
        int converted_score = (int) submission.getScore();
        index_in_list++;
        if (FileModifier.writeSubmitToFile(file4, submission, converted_score)) {
            System.out.printf("{ 'status' : 'ok', 'message' : '%s points'}", converted_score);
        }
    }

    /* function that gets the quizId */
    private static String[] getStringsQuestionsId(File file3, String quizId) {
        // extract the questions id from the quiz
        String[] quiz_info = new String[15];
        if (file3.exists()) {
            try {
                Scanner myReader = new Scanner(file3);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    int commaIndex = line.indexOf(",");
                    String currentId = line.substring(0, commaIndex);

                    if (currentId.equals(quizId)) {
                        quiz_info = line.split(",");
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return quiz_info;
    }

    /* function that checks if the answer id exists in the file with questions */
    private static String[] getStrings(String[] args, File file2) {
        // if we do not find the answer id in the file
        // extract the answers id from the questions
        String[] answers_of_user = new String[args.length - 4];
        for (int i = 0; i < answers_of_user.length; i++) {
            answers_of_user[i] = args[i + 4].substring(args[i + 4].indexOf("'") + 1, args[i + 4].lastIndexOf("'"));
        }

        for (int i = 4; i < answers_of_user.length; i++) {
            if (!FileModifier.findAnswerIdInFile(file2, answers_of_user[i])) {
                System.out.print("{ 'status' : 'error', 'message' : 'Answer ID for answer " + (i + 1) + " does not exist'}");
                return null;
            }
        }
        return answers_of_user;
    }

    /* function that checks if the quiz exists in the database of quizzes, if we have given any answer, and if we have already
    submitted that quiz */
    private static boolean noQuizFound(String[] args, File file3, File file4, String username, String password, String quizId) {
        // you cannot answer your own quiz case resolved in filemodifier class
        if (FileModifier.findQuizzIdInFile(file3, quizId, username, password) == -1) {
            System.out.print("{ 'status' : 'error', 'message' : 'No quiz was found'}");
            return true;
        } else if (FileModifier.findQuizzIdInFile(file3, quizId, username, password) == 1) {
            return true;
        }

        if ((args.length < 5) || !args[4].contains("-answer-id-1")) {
            System.out.print("{ 'status' : 'error', 'message' : 'No answers were provided'}");
            return true;
        }

        // we already submitted the quiz uses the same function as the one above, but checks in a different file
        if (FileModifier.findQuizzIdInFile(file4, quizId, username, password) == 1) {
            System.out.print("{ 'status' : 'error', 'message' : 'You already submitted this quiz'}");
            return true;
        }
        return false;
    }

    /* function that deletes the quiz from file3, the file which stores the quizzes */
    public static void deleteQuizzById(String[] args, File file1, File file2, File file3) {
        if (!Tema1.checkAuthentication(args, file1)) {
            return;
        }

        if (args.length < 4 || !args[3].contains("-id")) {
            System.out.print("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
            return;
        }

        // delete the quiz
        String username = args[1].substring(args[1].indexOf("'") + 1, args[1].lastIndexOf("'"));
        String password = args[2].substring(args[2].indexOf("'") + 1, args[2].lastIndexOf("'"));
        String quizId = args[3].substring(args[3].indexOf("'") + 1, args[3].lastIndexOf("'"));

        if (file3.exists()) {
            deleteQuizz(file3, username, password, quizId);
        }
    }

    /* function that checks if the user that deletes the quiz is the one that created it and deletes the quiz with
    deleteLine */
    private static void deleteQuizz(File file3, String username, String password, String quizId) {
        try {
            Scanner myReader = new Scanner(file3);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                int commaIndex = line.indexOf(",");
                String currentId = line.substring(0, commaIndex);

                if (currentId.equals(quizId)) {
                    //check if the user is the owner of the quiz
                    String[] linesplit = line.split(",");
                    if (linesplit[2].equals(username) && linesplit[3].equals(password)) {
                        //delete the quiz
                        myReader.close();
                        FileModifier.deleteLine(file3, line);
                        System.out.print("{ 'status' : 'ok', 'message' : 'Quizz deleted successfully'}");
                        return;
                    } else {
                        myReader.close();
                        System.out.print("{ 'status' : 'error', 'message' : 'You are not the owner of this quiz'}");
                        return;
                    }
                }
            }
            System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /* function that checks/reads what are my solutions to the quizzes and then shows them on the command line */
    public static void getMySolutions(String[] args, File file1, File file2, File file3, File file4) {
        if (!Tema1.checkAuthentication(args, file1)) {
            return;
        }

        String username = args[1].substring(args[1].indexOf("'") + 1, args[1].lastIndexOf("'"));
        String password = args[2].substring(args[2].indexOf("'") + 1, args[2].lastIndexOf("'"));

        System.out.print("{ 'status' : 'ok', 'message' :'[");

        try {
            Scanner myReader = new Scanner(file4);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] submitArray = data.split(",");
                if (submitArray[0].equals("Index_in_List")) {
                    continue;
                }

                if (submitArray[1].equals(username) && submitArray[2].equals(password)) {
                    String nameQuizz = FileModifier.getQuizNameById(file3, submitArray[3]);
                    System.out.print("{\"quiz-id\" : \"" + submitArray[3] + "\", " + "\"quiz-name\" : \"" + nameQuizz + "\", " + "\"score\" : \"" + submitArray[4] + "\", " + "\"index_in_list\" : \"" + submitArray[0] + "\"}");
                }

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
}
