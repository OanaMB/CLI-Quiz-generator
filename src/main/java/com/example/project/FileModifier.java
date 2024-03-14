package com.example.project;

import java.io.*;
import java.util.Scanner;

public class FileModifier {
    public static void createFile(File file, String message) {
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(message);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /* FUNCTIONS FOR USERS */
    /* we read the file and find if the user already exists
       if it does, we return true
       if it doesn't, we return false */
    public static boolean findUserInFile(File file, User user) {

        if (file.exists()) {
            /* we try to read the file */
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    /* find the index of the first comma in order to extract the username and the password "cell" from the file */
                    int commaIndex = line.indexOf(",");
                    String username = line.substring(0, commaIndex);
                    String password = line.substring(commaIndex + 1);

                    /* check if the user with the certain username and password exists*/
                    if (password.equals(user.getPassword())) {
                        if (username.equals(user.getUsername())) {
                            myReader.close();
                            return true;
                        }
                    }
                }
                myReader.close();
                return false;
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return false;
    }

    /* we write the user to the file */
    public static boolean writeUserToFile(File file, User user) {
        if (file.exists()) {
            try (FileWriter fw = new FileWriter(file, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
                out.write(user.getUsername() + "," + user.getPassword());
                out.write("\n");
                out.close();
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return false;
    }

    /* FUNCTIONS FOR QUESTIONS */

    /* function that checks if a question with a certain text is in the file;
     returns the id of the question if it is found, -1 if not */
    public static int findQuestionInFile(File file, String text) {
        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    int commaIndex = line.indexOf(",");
                    if (line.indexOf(text, commaIndex + 1) != -1) {
                        myReader.close();
                        return Integer.parseInt(line.substring(0, commaIndex));
                    }
                }
                myReader.close();
                return -1;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return -1;
    }

    /* function that writes a question to the file, with the help of switchCasesWriteAnswers */
    public static boolean addQuestionToFile(File file, Questions question, int no_answers) {
        if (file.exists()) {
            try (FileWriter fw = new FileWriter(file, true); BufferedWriter out = new BufferedWriter(fw)) {
                int answersid = question.getAnswersId();
                out.write(question.getQuestionId() + "," + question.getQuestion() + "," + question.getType() + ",");
                switchCasesWriteAnswers(question, no_answers, out, answersid);
                out.write("\n");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /* function that writes the answers to a question in the file, depending on how may possible answers there are, between 2 and 5 */
    private static void switchCasesWriteAnswers(Questions question, int no_answers, BufferedWriter out, int answersid) throws IOException {
        switch (no_answers) {
            case 2: {
                out.write(answersid + "," + question.getAnswer1() + "," + question.isTruthValue1() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer2() + "," + question.isTruthValue2());
                Questions.setAnswersId(answersid);
                break;
            }
            case 3: {
                out.write(answersid + "," + question.getAnswer1() + "," + question.isTruthValue1() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer2() + "," + question.isTruthValue2() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer3() + "," + question.isTruthValue3());
                Questions.setAnswersId(answersid);
                break;
            }
            case 4: {
                out.write(answersid + "," + question.getAnswer1() + "," + question.isTruthValue1() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer2() + "," + question.isTruthValue2() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer3() + "," + question.isTruthValue3() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer4() + "," + question.isTruthValue4());
                Questions.setAnswersId(answersid);
                break;
            }
            case 5: {
                out.write(answersid + "," + question.getAnswer1() + "," + question.isTruthValue1() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer2() + "," + question.isTruthValue2() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer3() + "," + question.isTruthValue3() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer4() + "," + question.isTruthValue4() + ",");
                answersid++;
                out.write(answersid + "," + question.getAnswer5() + "," + question.isTruthValue5());
                Questions.setAnswersId(answersid);
                break;
            }
        }
    }

    /* FUNCTIONS FOR QUIZZES AND SUBMITS */
    /* function that reads the quiz file, and checks if there is a quiz with a certain
    name; if there is, it returns the id of it */
    public static int findQuizNameInFile(File file, String text) {
        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    int commaIndex = line.indexOf(",");
                    if (line.indexOf(text, commaIndex + 1) != -1) {
                        myReader.close();
                        return Integer.parseInt(line.substring(0, commaIndex));
                    }
                }
                myReader.close();
                return -1;
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return -1;
    }

    /* function that reads the question file and checks if the question id is in there;
    returns true or false */
    public static boolean findQuestionIdInFile(File file, String id) {

        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    int commaIndex = line.indexOf(",");
                    String currentId = line.substring(0, commaIndex);

                    if (currentId.equals(id)) {
                        myReader.close();
                        return true;
                    }

                }
                myReader.close();
                return false;
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        return false;
    }

    /* function that writes a quiz to a file */
    public static boolean addQuizToFile(File file, Quiz quiz) {
        if (file.exists()) {
            try (FileWriter fw = new FileWriter(file, true); BufferedWriter out = new BufferedWriter(fw)) {

                out.write(quiz.getQuizId() + "," + quiz.getQuizName() + "," + quiz.getUsername() + "," + quiz.getPassword() + ",");
                String[] questions = quiz.getQuestionIDs();
                for (int i = 0; i < questions.length; i++) {
                    if (i == questions.length - 1) {
                        out.write(questions[i]);
                    } else {
                        out.write(questions[i] + ",");
                    }
                }
                out.write("\n");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /* function that deletes a line from a file by writing every line that is not the
    one to be removed in a temporary one and renames it */
    public static void deleteLine(File file, String lineToRemove) {
        File temporaryFile = new File("temporary.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if (trimmedLine.equals(lineToRemove)) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            temporaryFile.renameTo(file);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        temporaryFile.delete();
    }

    /* function that finds a quiz with the help of its id and
    checks if we want to submit answers to our own created quiz;
     returns -1 if it is not found, 0- if it found and it's not created by us, and 1 if it exists and is ours */
    public static int findQuizzIdInFile(File file, String id, String username, String password) {

        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    int commaIndex = line.indexOf(",");
                    String currentId = line.substring(0, commaIndex);

                    if (currentId.equals(id)) {
                        String[] quizInfo = line.split(",");
                        if (quizInfo[2].equals(username) && quizInfo[3].equals(password)) {
                            System.out.println("{ 'status' : 'error', 'message' : 'You cannot answer your own quizz'}");
                            myReader.close();
                            return 1;
                        }
                        myReader.close();
                        return 0;
                    }

                }
                myReader.close();
                return -1;
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        return -1;
    }

    /* function that finds the answer ids in the file with questions */
    public static boolean findAnswerIdInFile(File file, String answerId) {

        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();

                    String[] answers = line.split(",");
                    for (int i = 1; i < answers.length; i++) {
                        if (answers[i].equals(answerId)) {
                            myReader.close();
                            return true;
                        }
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    /* function that calculates the value of an answer a user has given */
    public static double checkAnswerInFile(File file, String[] questions_of_quiz, String answer_of_user) {
        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    int commaIndex = line.indexOf(",");
                    String currentId = line.substring(0, commaIndex);

                    String[] question_info = line.split(",");
                    for (int i = 1; i < question_info.length; i++) {
                        if (question_info[i].equals(answer_of_user)) {
                            Integer x = checkAnswerExistenceInQuiz(questions_of_quiz, myReader, currentId);
                            if (x != null) return x;
                            double intermediateScore = 0;
                            Double intermediateScore1 = getaDouble(myReader, question_info, i, intermediateScore);
                            if (intermediateScore1 != null) return intermediateScore1;
                        }
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            return -1000;
        }
        return -1000;
    }

    /* function that checks  if the answer id is in a question contained in the quiz */
    private static Integer checkAnswerExistenceInQuiz(String[] questions_of_quiz, Scanner myReader, String currentId) {
        int ok = 0;
        for (String id : questions_of_quiz) {
            if (id.equals(currentId)) {
                ok = 1;
                break;
            }
        }
        if (ok == 0) {
            myReader.close();
            System.out.println("{ 'status' : 'error', 'message' : 'You cannot answer a question that is not in the quiz'}");
            return -1000;
        }
        return null;
    }

    /* function that calculates the percent of an answer we have given */
    private static Double getaDouble(Scanner myReader, String[] question_info, int i, double intermediateScore) {
        // we see what type the question is
        if (question_info[2].equals("single")) {
            if (question_info[i + 2].equals("true")) {
                intermediateScore += 1;
            } else if (question_info[i + 2].equals("false")) {
                intermediateScore -= 1;
            }
            myReader.close();
            return intermediateScore;
        }

        if (question_info[2].equals("multiple")) {

            // number the answers with true and false
            int nrOfTrue = 0;
            int nrOfFalse = 0;
            for (int j = 3; j < question_info.length; j++) {
                if (question_info[j].equals("true")) {
                    nrOfTrue++;
                } else if (question_info[j].equals("false")) {
                    nrOfFalse++;
                }
            }

            // check if the user has answered the true answers

            if (question_info[i + 2].equals("true")) {
                intermediateScore += (1.00 * 1 / nrOfTrue);
            } else if (question_info[i + 2].equals("false")) {
                intermediateScore -= (1.00 * 1 / nrOfFalse);
            }
            myReader.close();
            return intermediateScore;
        }
        return null;
    }

    /* function that writes the submit in the file with the score */
    public static boolean writeSubmitToFile(File file, Submits submits, int convertedScore) {
        if (file.exists()) {
            try (FileWriter fw = new FileWriter(file, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
                out.write(submits.getIndex_in_list() + "," + submits.getUsername() + "," + submits.getPassword() + "," + submits.getQuizId() + "," + convertedScore);
                out.write("\n");
                out.close();
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return false;
    }

    /* function that reads the name of a quizId */
    public static String getQuizNameById(File file, String id) {
        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    int commaIndex = line.indexOf(",");
                    String currentId = line.substring(0, commaIndex);
                    String[] quizInfo = line.split(",");

                    if (currentId.equals(id)) {
                        myReader.close();
                        return quizInfo[1];
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return null;
    }

}
