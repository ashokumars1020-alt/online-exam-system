import express from "express";
import bodyParser from "body-parser";
import cors from "cors";

const app = express();
app.use(cors());
app.use(bodyParser.json());

// Mock database
const exams = {
  1: {
    id: 1,
    title: "Java Basics Test",
    questions: [
      {
        id: 101,
        text: "Which keyword is used to inherit a class in Java?",
        options: ["this", "super", "extends", "implements"],
        answer: "extends"
      },
      {
        id: 102,
        text: "Which of the following is not a Java primitive type?",
        options: ["int", "String", "boolean", "float"],
        answer: "String"
      }
    ]
  }
};

// Fetch exam questions
app.get("/api/exams/:examId/questions", (req, res) => {
  const exam = exams[req.params.examId];
  if (!exam) return res.status(404).json({ message: "Exam not found" });
  res.json(exam.questions);
});

// Submit answers and calculate result
app.post("/api/exams/:examId/submit", (req, res) => {
  const { answers } = req.body;
  const exam = exams[req.params.examId];
  if (!exam) return res.status(404).json({ message: "Exam not found" });

  let correct = 0;
  exam.questions.forEach(q => {
    if (answers[q.id] === q.answer) correct++;
  });

  const total = exam.questions.length;
  const wrong = total - correct;
  const score = correct * 10; // each question = 10 marks

  res.json({ score, total, correct, wrong });
});

// Start server
app.listen(5000, () => console.log("Exam system backend running on port 5000"));
