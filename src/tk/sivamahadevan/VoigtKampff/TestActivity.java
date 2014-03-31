package tk.sivamahadevan.VoigtKampff;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.*;

/**
 * Created by siva on 29/03/14.
 */
public class TestActivity extends Activity {
    private int questionNumber = 0;
    private int questionIndex = 0;
    private int textSpeed;
    private long reactionTime;

    private char[] question;
    private String[] answers = new String[4];
    private int[] answerScores = new int[4];

    private RadioGroup answerGroup;
    private TextView animatedText;
    private TextView title;

    private Intent resultsIntent;
    private int[] finalResults = new int[8];

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        textSpeed = getIntent().getIntExtra("textSpeed", 50);
        answerGroup = (RadioGroup) findViewById(R.id.testAnswers);
        animatedText = (TextView) findViewById(R.id.testQuestion);
        title = ((TextView) findViewById(R.id.testNumber));

        resultsIntent = new Intent(this, ResultsActivity.class);

        try {
            resetView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextQuestion(View view) {
        reactionTime = (System.nanoTime() - reactionTime) / 1000000000;
        int currScore = answerScores[answerGroup.indexOfChild(findViewById(answerGroup.getCheckedRadioButtonId()))];

        if(reactionTime < 3) {
            currScore += 10;
        } else if(reactionTime < 6) {
            currScore += 5;
        } else if(reactionTime < 9) {
            currScore -= 5;
        } else {
            currScore -= 20;
        }

        finalResults[questionNumber - 1] = currScore;

        try {
            resetView();
        } catch (IOException e) {
            finishTest();
        }
    }

    private void resetView() throws IOException {
        AssetManager assetManager = getAssets();
        BufferedReader input = new BufferedReader(new InputStreamReader(assetManager.open(String.format("questions/%d.txt", ++questionNumber))));
        question = input.readLine().toCharArray();

        input = new BufferedReader(new InputStreamReader(assetManager.open(String.format("answers/%d.txt", questionNumber))));
        for(int i = 0; i < 4; i++) {
            String[] line = input.readLine().split("XXX");
            answers[i] = line[0];
            answerScores[i] = Integer.parseInt(line[1]);

            ((RadioButton) answerGroup.getChildAt(i)).setText(answers[i]);
        }

        title.setText("Question #" + String.valueOf(questionNumber));
        animatedText.setText("");
        answerGroup.setVisibility(RadioGroup.INVISIBLE);
        ((Button) findViewById(R.id.testNext)).setVisibility(Button.INVISIBLE);
        answerGroup.clearCheck();
        animateText();
    }

    private void animateText() {
        questionIndex = 0;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(questionIndex == 0) {
                    animatedText.setText("");
                }

                if (questionIndex != question.length) {
                    animatedText.append(String.valueOf(question[questionIndex++]));
                    handler.postDelayed(this, textSpeed);
                } else {
                    for(int i = 0; i < 4; i++) {
                        answerGroup.setVisibility(RadioGroup.VISIBLE);
                        ((Button) findViewById(R.id.testNext)).setVisibility(Button.VISIBLE);
                    }

                    reactionTime = System.nanoTime();
                }
            }
        }, textSpeed);
    }

    private void finishTest() {
        resultsIntent.putExtra("finalResults", finalResults);
        finish();
        startActivity(resultsIntent);
    }
}