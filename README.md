# CLI-Quiz-generator

    Acest program implementează un generator de chestionare, utilizatorii acestuia
având posibilitatea de a adăuga întrebări cu alegere simplă sau multiplă, de a crea
chestionare, a le completa sau de a le șterge. Clasele disponibile în program sunt:
     - Clasa User - prelucrarea de informații despre utilizatori;
     - Clasa Questions - implementarea metodelor pentru crearea întrebărilor sau citirea lor din fișiere;
     - Clasa Quiz - crearea și citirea chestionarelor;
     - Clasa Submits - de încărcarea soluțiilor chestionarelor de către utilizatori;
     - Clasa FileModifier - metode pentru a crea fișierele, a citi sau a scrie în ele;
     - Clasa Tema1 - afișarea meniurilor programului și pentru a prelua datele introduse de utilizator.
    Funcțiile disponibile pentru acest program sunt:
    -Din clasa User
 1. createUser: În această metodă se verifică dacă se oferă un username
    și o parolă, dacă utilizatorul există deja în fișier, iar dacă nu există, se adaugă în baza de date.
    -Din clasa Question
 2. createQuestion: Folosim authentification pentru a verifica dacă utilizatorul
    este logat, apoi folosim extractAnswersInArrays pentru a stoca răspunsurile si descrierile în answers și descriptionsș
    aceste arrays vor fi folosite pentru a analiza mai multe cazuri de rezultate eronate cu funcția checkQuestionsValidity.
    Ulterior, daca avem raspunsurile si descrierile corecte si functia findQuestionInFile nu gaseste,
    se creează întrebarea și se adaugă în baza de date.
 3. getQuestionIdByText: În această metodă se verifică dacă se oferă un text de întrebare
    și dacă acesta se află în baza de date. Dacă se oferă un text de întrebare care nu se află în baza de date,
    se afișează un mesaj de eroare.
 4. getAllQuestions: În această metodă se afișează toate întrebările din baza de date citind file2.
    -Din clasa Quiz
 5. createQuiz: După autentificare, se verifică dacă exista intrebarea ce se vrea adaugata cu checkExistenceofIds
    și dacă acesta nu se află deja în baza de date. Dacă toate condițiile sunt îndeplinite, se creează quiz-ul.
 6. getQuizByName: Se foloseste metoda findQuizNameInFile pentru a gasi id-ul quiz-ului
 7. getAllQuizzes: În această metodă se afișează toate quiz-urile citind file3.
 8. getQuizzDetailsById: citim din file3 si afisam quiz-ul cu id-ul dat si folosim readDetailsInQuestionFile pentru a citi
    detalii despre intrebarile din quiz-ul respectiv.
    -Din clasa Submits
 9. submitQuiz: Folosim metoda noQuizFound sa verificam daca quiz-ul exista, daca au fost oferite răspunsuri, și, cu ajutorul
    findQuizzIdInFile, daca am dat submit deja la acea întrebare, apoi, dacă nu avem erori, folosim metoda submitQuizCorrectly
    pentru a încărca răspunsurile și a afișa scorul, care folosește metoda checkAnswerInFile pentru a obț ine scorul la un raspuns
    de la o întrebare.
 10. deleteQuizzById: Verificam autentificarea si daca avem oferit un quizId si folosim functia deleteQuiz pentru a sterge quiz-ul
    cu id-ul respectiv, în funcție de utilizator.
 11. getMySolutions: Verificam autentificarea si citim din file4 si afisam quiz-urile la care am dat submit.
     -Din main
 12. cleanup-all: În această metodă se șterg toate fișierele din baza de date.
