import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
        questionPanel.setBackground(new Color(173, 216, 230)); 
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
            answerButtons[i].setBackground(new Color(63, 192, 207)); 
            
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
        questions.add(new Question("?", "https://cdn.discordapp.com/attachments/313985990765182986/1239298257872752761/GNVE0DDXYAAtxRd.png?ex=664269f1&is=66411871&hm=f2fc40619c4bb76ecc42ddfb03e032a22fdc67eba1f122840ac821534d3953de&", new String[]{"!", "3", "f", "?"}, 3));
        questions.add(new Question("OMERO", "https://cdn.discordapp.com/attachments/1027717442442633226/1239386826985181185/Screenshot_1.png?ex=6642bc6e&is=66416aee&hm=6b363e92effce6a1dfdccb0ad205690d625888a68cf3241957b91f445d9d5fb1&", new String[]{"1", "2", "3", "4"}, 2));
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
                        endGame();
                        //displayQuestion();
                    }
                });
            }
        }, 0, 1000);
    }

    private void endGame() {
        if (score >= (questions.size()*.75))
        {
            playSoundFromLocalFile("death2.wav");
            JOptionPane.showMessageDialog(this, "Nice going, you win!" + score);
            startGame();
        }else
        {
            playSoundFromLocalFile("perder.wav");
            JOptionPane.showMessageDialog(this, "Game over! Try harder next time! " + score);
            startGame();
        }
        
    }

    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        Question currentQuestion = questions.get(currentQuestionIndex);
        int correctAnswerIndex = currentQuestion.getCorrectAnswerIndex();
        if (clickedButton.getText().equals(currentQuestion.getAnswers()[correctAnswerIndex])) {
            score++;
            playSoundFromLocalFile("orb.wav");
        }
        else
        {
            playSoundFromLocalFile("death2.wav");
        }
        timer.cancel();
        currentQuestionIndex++;
        displayQuestion();
    }

    private void playSoundFromLocalFile(String fileName) {
        try {
            File file = new File(getClass().getResource("/sounds/" + fileName).toURI());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | URISyntaxException ex) {
            ex.printStackTrace();
        }
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