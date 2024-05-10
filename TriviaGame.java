import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class TriviaGame extends JFrame implements ActionListener {
    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private Timer timer;
    private JLabel questionLabel;
    private JLabel imageLabel;
    private JButton[] answerButtons;
    private JLabel timerLabel;
    private int score;

    public TriviaGame() {
        setTitle("Trivia Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize questions
        initializeQuestions();

        // Shuffle questions
        Collections.shuffle(questions);

        // Create GUI components
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionLabel = new JLabel();
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        imageLabel = new JLabel();
        questionPanel.add(imageLabel, BorderLayout.CENTER);
        add(questionPanel, BorderLayout.CENTER);

        JPanel answerPanel = new JPanel(new GridLayout(2, 2));
        answerButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JButton();
            answerButtons[i].addActionListener(this);
            answerPanel.add(answerButtons[i]);
        }
        add(answerPanel, BorderLayout.SOUTH);

        timerLabel = new JLabel();
        add(timerLabel, BorderLayout.NORTH);

        // Start game
        startGame();

        setSize(400, 400);
        setVisible(true);
    }

    private void initializeQuestions() {
        // Add your questions here
        questions = new ArrayList<>();
        questions.add(new Question("Question 1", "https://image.freepik.com/vector-gratis/ilustracion-dibujos-animados-perro-feliz-humor_11460-3669.jpg", new String[]{"Answer 1", "Answer 2", "Answer 3", "Answer 4"}, 0));
        questions.add(new Question("Pene?", "https://www.kindpng.com/picc/m/485-4852609_funny-dogs-transparent-png-png-download.png", new String[]{"Sí", "No", "No", "Sí"}, 0));
        // Add more questions similarly
    }

    private void startGame() {
        score = 0;
        currentQuestionIndex = 0;
        displayQuestion();
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestion());

            // Load image
            try {
                URL url = new URL(currentQuestion.getImagePath());
                ImageIcon icon = new ImageIcon(url);
                Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 4; i++) {
                answerButtons[i].setText(currentQuestion.getAnswers()[i]);
            }
            startTimer();
        } else {
            endGame();
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            int secondsLeft = 10;

            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timerLabel.setText("Time left: " + secondsLeft);
                    secondsLeft--;
                    if (secondsLeft < 0) {
                        timer.cancel();
                        displayQuestion();
                    }
                });
            }
        }, 0, 1000);
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this, "Game over! Your score: " + score);
        startGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        Question currentQuestion = questions.get(currentQuestionIndex);
        int correctAnswerIndex = currentQuestion.getCorrectAnswerIndex();
        if (clickedButton.getText().equals(currentQuestion.getAnswers()[correctAnswerIndex])) {
            score++;
        }
        timer.cancel();
        currentQuestionIndex++;
        displayQuestion();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TriviaGame::new);
    }
}

class Question {
    private String question;
    private String imagePath;
    private String[] answers;
    private int correctAnswerIndex;

    public Question(String question, String imagePath, String[] answers, int correctAnswerIndex) {
        this.question = question;
        this.imagePath = imagePath;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}