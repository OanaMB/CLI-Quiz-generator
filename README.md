# CLI-Quiz-generator

## Descriere
Acest program implementează un generator de chestionare, utilizatorii acestuia având posibilitatea de a adăuga întrebări cu alegere simplă sau multiplă, de a crea chestionare, a le completa sau de a le șterge.

## Clase disponibile
- **Clasa `User`** – prelucrarea de informații despre utilizatori.
- **Clasa `Questions`** – implementarea metodelor pentru crearea întrebărilor sau citirea lor din fișiere.
- **Clasa `Quiz`** – crearea și citirea chestionarelor.
- **Clasa `Submits`** – încărcarea soluțiilor chestionarelor de către utilizatori.
- **Clasa `FileModifier`** – metode pentru a crea fișierele, a citi sau a scrie în ele.
- **Clasa `Tema1`** – afișarea meniurilor programului și preluarea datelor introduse de utilizator.

## Funcționalități

### Din clasa `User`
1. **createUser** – În această metodă se verifică dacă se oferă un username și o parolă, dacă utilizatorul există deja în fișier, iar dacă nu există, se adaugă în baza de date.

### Din clasa `Questions`
2. **createQuestion** – Folosim autentificarea pentru a verifica dacă utilizatorul este logat, apoi folosim `extractAnswersInArrays` pentru a stoca răspunsurile și descrierile în `answers` și `descriptions`.  
   Aceste arrays vor fi folosite pentru a analiza mai multe cazuri de rezultate eronate cu funcția `checkQuestionsValidity`. Ulterior, dacă avem răspunsurile și descrierile corecte și funcția `findQuestionInFile` nu găsește întrebarea, se creează întrebarea și se adaugă în baza de date.

3. **getQuestionIdByText** – Se verifică dacă se oferă un text de întrebare și dacă acesta se află în baza de date. Dacă textul nu se află în baza de date, se afișează un mesaj de eroare.

4. **getAllQuestions** – Se afișează toate întrebările din baza de date citind `file2`.

### Din clasa `Quiz`
5. **createQuiz** – După autentificare, se verifică dacă există întrebarea ce se vrea adăugată cu `checkExistenceofIds` și dacă aceasta nu se află deja în baza de date. Dacă toate condițiile sunt îndeplinite, se creează quiz-ul.

6. **getQuizByName** – Se folosește metoda `findQuizNameInFile` pentru a găsi ID-ul quiz-ului.

7. **getAllQuizzes** – Se afișează toate quiz-urile citind `file3`.

8. **getQuizzDetailsById** – Se citește din `file3` și se afișează quiz-ul cu ID-ul dat, folosind `readDetailsInQuestionFile` pentru a citi detalii despre întrebările din quiz-ul respectiv.

### Din clasa `Submits`
9. **submitQuiz** – Se folosește metoda `noQuizFound` pentru a verifica dacă quiz-ul există, dacă au fost oferite răspunsuri și, cu ajutorul `findQuizzIdInFile`, dacă s-a făcut deja submit la acea întrebare.  
   Dacă nu există erori, se folosește metoda `submitQuizCorrectly` pentru a încărca răspunsurile și a afișa scorul, utilizând `checkAnswerInFile` pentru a obține scorul la un răspuns.

10. **deleteQuizzById** – Se verifică autentificarea și dacă s-a oferit un `quizId`, apoi se folosește funcția `deleteQuiz` pentru a șterge quiz-ul cu ID-ul respectiv, în funcție de utilizator.

11. **getMySolutions** – Se verifică autentificarea, se citește din `file4` și se afișează quiz-urile la care s-a făcut submit.

### Din `main`
12. **cleanup-all** – Se șterg toate fișierele din baza de date.

