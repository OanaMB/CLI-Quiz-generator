package com.example.project;

import java.io.*;
import java.util.Scanner;

public class Questions {
    private String question;
    private String type;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private boolean truthValue1;
    private boolean truthValue2;
    private boolean truthValue3;
    private boolean truthValue4;
    private boolean truthValue5;
    static final int noOfAnswers = 5;
    static int questionId = 0;
    static int answersId = 0;

    /* we have fields for every possible answer */
    public Questions(int questionId, int answersId, String question, String type, String answer1, String answer2, String answer3, String answer4, String answer5, boolean truthValue1, boolean truthValue2, boolean truthValue3, boolean truthValue4, boolean truthValue5) {
        this.questionId = questionId;
        this.answersId = answersId;
        this.question = question;
        this.type = type;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.truthValue1 = truthValue1;
        this.truthValue2 = truthValue2;
        this.truthValue3 = truthValue3;
        this.truthValue4 = truthValue4;
        this.truthValue5 = truthValue5;
    }

    public String getQuestion() {
        return question;
    }

    public String getType() {
        return type;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public boolean isTruthValue1() {
        return truthValue1;
    }

    public boolean isTruthValue2() {
        return truthValue2;
    }

    public boolean isTruthValue3() {
        return truthValue3;
    }

    public boolean isTruthValue4() {
        return truthValue4;
    }

    public boolean isTruthValue5() {
        return truthValue5;
    }

    public static int getQuestionId() {
        return questionId;
    }

    public static int getAnswersId() {
        return answersId;
    }

    public static void setAnswersId(int answersId) {
        Questions.answersId = answersId;
    }

    public static void createQuestion(String[] args, File file, File file2) {

        if (authentification(args, file)) return;
        /* extract the question text and type strings; they are positioned on the third and fourth place in the args array*/
        String text = args[3].substring(args[3].indexOf("'") + 1, args[3].lastIndexOf("'"));
        String type = args[4].substring(args[4].indexOf("'") + 1, args[4].lastIndexOf("'"));

        /* a copy to handle the extraction of the possible answers and their value of truth; 5 represents the number of arguments
         beside the ones that refer to answers, for example the username, password, type */
        String[] array_of_answers = new String[args.length - 5];
        System.arraycopy(args, 5, array_of_answers, 0, array_of_answers.length);

        /* see the number of answers and the number of true/false descriptions */
        int no_of_descriptions = 0;
        int no_of_answers = 0;

        /* put the answers and descriptions in separate arrays so that the index of, for example, answers 1
         corresponds with the index of value of truth for answer 1 */
        String[] answers = new String[noOfAnswers];
        String[] descriptions = new String[noOfAnswers];

        for (int i = 0; i < array_of_answers.length; i++) {
            if (array_of_answers[i].contains("-is-correct")) {
                ++no_of_descriptions;
                continue;
            }
            ++no_of_answers;
        }

        /* put the answers and the corresponding values of truth in the two arrays, depending on the flag from the command line */
        extractAnswersInArrays(array_of_answers, answers, descriptions);

        /* checks multiple cases that may cause the method to go wrong */
        if (checkQuestionsValidity(no_of_descriptions, no_of_answers, answers, descriptions)) return;
        /* checks if the same answer is put more than one time */
        boolean[] truth_of_answers = sameAnswerMoreOnce(type, answers, descriptions);
        if (truth_of_answers == null) return;
        /* check if the question text already exists in the file of questions;
        if we find the id of the text, the question already exists */
        if (FileModifier.findQuestionInFile(file2, text) != -1) {
            System.out.print("{ 'status' : 'error', 'message' : 'Question already exists'}");
            return;
        }
        /* create the question object and add it to the file */
        addQuestionSuccessfully(file2, text, type, no_of_answers, answers, truth_of_answers);
    }

    /* function that creates the question object and uses the addQuestionToFile method to  add it in the file */
    private static void addQuestionSuccessfully(File file2, String text, String type, int no_of_answers, String[] answers, boolean[] truth_of_answers) {
        /* create the question object */
        questionId++;
        answersId++;
        Questions question = new Questions(questionId, answersId, text, type, answers[0], answers[1], answers[2], answers[3],
                answers[4], truth_of_answers[0], truth_of_answers[1], truth_of_answers[2], truth_of_answers[3], truth_of_answers[4]);

        /* add the question to the file of questions */
        if (FileModifier.addQuestionToFile(file2, question, no_of_answers)) {
            System.out.print("{ 'status' : 'ok', 'message' : 'Question added successfully'}");
        }
    }

    /* checks if there are two answers that are the same */
    private static boolean[] sameAnswerMoreOnce(String type, String[] answers, String[] descriptions) {
        /* convert strings to boolean */
        boolean[] truth_of_answers = checkBooleans(type, descriptions);
        if (truth_of_answers == null) return null;

        /* check if there are two answers with the same text int the answers array */
        for (int i = 0; i < answers.length; i++) {
            for (int j = i + 1; j < answers.length; j++) {
                if (answers[i] != null && answers[j] != null && answers[i].equals(answers[j])) {
                    System.out.print("{ 'status' : 'error', 'message' : 'Same answer provided more than once'}");
                    return null;
                }
            }
        }
        return truth_of_answers;
    }

    /* function for authentication and checking the cases where there is no text or type provided */
    private static boolean authentification(String[] args, File file) {
        if (!Tema1.checkAuthentication(args, file)) {
            return true;
        }

        if (args.length < 4 || !args[3].contains("-text")) {
            System.out.print("{ 'status' : 'error', 'message' : 'No question text provided'}");
            return true;
        }

        if (args.length < 5 || !args[4].contains("-type")) {
            System.out.print("{ 'status' : 'error', 'message' : 'No question type provided'}");
            return true;
        }
        return false;
    }

    /* converts descriptions from strings to booleans and checks if there are multiple true values in a single type question */
    private static boolean[] checkBooleans(String type, String[] descriptions) {
        boolean[] truth_of_answers = new boolean[noOfAnswers];
        for (int i = 0; i < noOfAnswers; i++) {
            if (descriptions[i] == null) {
                continue;
            }
            if (descriptions[i].equals("1")) {
                truth_of_answers[i] = true;
            } else if (descriptions[i].equals("0")) {
                truth_of_answers[i] = false;
            }
        }

        /* check if there are more correct answers in the case the type of the question is single answer */
        int no_of_correct_answers = 0;
        if (type.equals("single")) {
            for (int i = 0; i < truth_of_answers.length; i++) {
                if (truth_of_answers[i]) {
                    ++no_of_correct_answers;
                }
            }

            if (no_of_correct_answers > 1) {
                System.out.print("{ 'status' : 'error', 'message' : 'Single correct answer question has more than one correct answer'}");
                return null;
            }
        }
        return truth_of_answers;
    }

    /* checks in arguments from the terminal if there are any answers provided, if there is only one or more than 5 answers,
    if all the answers have corresponding descriptions */
    private static boolean checkQuestionsValidity(int no_of_descriptions, int no_of_answers, String[] answers, String[] descriptions) {
        /* check if there are any possible answers provided */
        if (no_of_answers == 0) {
            System.out.print("{ 'status' : 'error', 'message' : 'No answer provided'}");
            return true;
        }
        /* variable max_no_of_answers is used in the context of checking if the answers have corresponding values of truth */
        int max_no_of_answers = Math.max(no_of_answers, no_of_descriptions);

        /* check if the number of answers is between 2 and 5 */
        if (max_no_of_answers == 1) {
            System.out.print("{ 'status' : 'error', 'message' : 'Only one answer provided'}");
            return true;
        } else if (max_no_of_answers > 5) {
            System.out.print("{ 'status' : 'error', 'message' : 'More than 5 answers were submitted'}");
            return true;
        }

        /* in the case the number of answers is not equal to the number of descriptions, from the fact that one flag is missing */
        if (no_of_answers != no_of_descriptions) {

            for (int i = 0; i < max_no_of_answers; i++) {
                if (descriptions[i] == null && answers[i] != null) {
                    System.out.print("{ 'status' : 'error', 'message' : 'Answer " + (i + 1) + " has no answer correct flag'}");
                    return true;
                }

                if (descriptions[i] != null && answers[i] == null) {
                    System.out.print("{ 'status' : 'error', 'message' : 'Answer " + (i + 1) + " has no answer description'}");
                    return true;
                }
            }
        }
        return false;
    }

    /* function that puts the answers and the corresponding descriptions in arrays */
    private static void extractAnswersInArrays(String[] array_of_answers, String[] answers, String[] descriptions) {
        for (int i = 0; i < array_of_answers.length; i++) {
            String flag = array_of_answers[i].substring(0, array_of_answers[i].lastIndexOf(" "));
            switch (flag) {
                case "-answer-1":
                    answers[0] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                case "-answer-2":
                    answers[1] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                case "-answer-3":
                    answers[2] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                case "-answer-4":
                    answers[3] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                case "-answer-5":
                    answers[4] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                case "-answer-1-is-correct":
                    descriptions[0] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                case "-answer-2-is-correct":
                    descriptions[1] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                case "-answer-3-is-correct":
                    descriptions[2] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                case "-answer-4-is-correct":
                    descriptions[3] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                case "-answer-5-is-correct":
                    descriptions[4] = array_of_answers[i].substring(array_of_answers[i].indexOf("'") + 1, array_of_answers[i].lastIndexOf("'"));
                    break;
                default:
                    break;
            }
        }
    }

    /* function that gets the question id from the text, by using the function findQuestionInFile*/
    public static void getQuestionIdByText(String[] args, File file, File file2) {
        if (!Tema1.checkAuthentication(args, file)) {
            return;
        }
        /* extract the text string from args then enter the findQuestionInFile and return index_question */
        String text = args[3].substring(args[3].indexOf("'") + 1, args[3].lastIndexOf("'"));
        int index_question = FileModifier.findQuestionInFile(file2, text);
        if (index_question == -1) {
            System.out.print("{ 'status' : 'error', 'message' : 'Question does not exist'}");
        } else {
            System.out.println("{ 'status' : 'ok', 'message' : '" + index_question + "'}");
        }
    }

    /* we extract all the questions stored in the file */
    public static void getAllQuestions(String[] args, File file, File file2) {
        if (!Tema1.checkAuthentication(args, file)) {
            return;
        }
        /* we read everything from the file */
        System.out.print("{ 'status' : 'ok', 'message' :'[");

        /* the questionArray array represents all the elements stored on that line;
         we also avoid the first line in the file, the one that contains the header */
        try {
            Scanner myReader = new Scanner(file2);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] questionArray = data.split(",");
                if (questionArray[0].equals("QuestionID")) {
                    continue;
                }
                System.out.print("{\"question_id\" : \"" + questionArray[0] + "\", " + "\"question_name\" : \"" + questionArray[1] + "\"}");
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
