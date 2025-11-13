Frontend (index.html): 
import React, { useState, useEffect } from "react";
import axios from "axios";

export default function OnlineExam({ examId }) {
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState({});
  const [submitted, setSubmitted] = useState(false);
  const [result, setResult] = useState(null);

  // Fetch questions from backend
  useEffect(() => {
    axios.get(`/api/exams/${examId}/questions`)
      .then(res => setQuestions(res.data))
      .catch(() => alert("Failed to load questions."));
  }, [examId]);

  // Handle option selection
  const handleAnswer = (questionId, option) => {
    setAnswers({ ...answers, [questionId]: option });
  };

  // Submit exam
  const submitExam = () => {
    if (Object.keys(answers).length !== questions.length)
      return alert("Please answer all questions before submitting.");

    axios.post(`/api/exams/${examId}/submit`, { answers })
      .then(res => {
        setSubmitted(true);
        setResult(res.data);
      })
      .catch(() => alert("Submission failed. Please try again."));
  };

  return (
    <div>
      <h2>Online Examination Portal</h2>

      {!submitted ? (
        <div>
          {questions.map(q => (
            <div key={q.id}>
              <h4>{q.text}</h4>
              {q.options.map(opt => (
                <label key={opt}>
                  <input
                    type="radio"
                    name={`q${q.id}`}
                    value={opt}
                    onChange={() => handleAnswer(q.id, opt)}
                  />
                  {opt}
                </label>
              ))}
            </div>
          ))}
          <button onClick={submitExam}>Submit Exam</button>
        </div>
      ) : (
        <div>
          <h3>Exam Submitted Successfully!</h3>
          <p>Your Score: {result.score} / {result.total}</p>
          <h4>Performance Analysis:</h4>
          <ul>
            <li>Correct Answers: {result.correct}</li>
            <li>Wrong Answers: {result.wrong}</li>
            <li>Accuracy: {((result.correct / result.total) * 100).toFixed(2)}%</li>
          </ul>
        </div>
      )}
    </div>
  );
}
