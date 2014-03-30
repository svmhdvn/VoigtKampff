package tk.sivamahadevan.VoigtKampff;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.*;
import java.util.Random;

/**
 * Created by siva on 29/03/14.
 */
public class CalibrationActivity extends Activity {
    private int textSpeed = 50;
    private int index = 0;
    private char[] quote;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calibration);

        try {
            quote = initQuote();
            animateText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SeekBar speedSlider = (SeekBar) findViewById(R.id.step1TextSpeed);
        speedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser){
                textSpeed = 100 - progress;
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar){}
        });

    }

    private void animateText() {
        TextView animatedText = (TextView) findViewById(R.id.step1AnimatedText);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(index == 0){
                    animatedText.setText("");

                    try {
                        quote = initQuote();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (index != quote.length) {
                    animatedText.append(String.valueOf(quote[index++]));
                    handler.postDelayed(this, textSpeed);
                } else {
                    index = 0;
                    handler.postDelayed(this, 2000);
                }
            }
        }, textSpeed);
    }

    public void menuView(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("textSpeed", textSpeed);
        finish();
        startActivity(intent);
    }

    private char[] initQuote() throws IOException {
        AssetManager assetManager = getAssets();
        BufferedReader input = new BufferedReader(new InputStreamReader(assetManager.open(String.format("calibration_samples/%d.txt", new Random().nextInt(5) + 1))));
        return input.readLine().toCharArray();
    }

}