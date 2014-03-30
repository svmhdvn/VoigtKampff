package tk.sivamahadevan.VoigtKampff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void calibrateView(View view){
        startActivity(new Intent(this, CalibrationActivity.class));
    }

    public void testView(View view) {
        Intent intent = getIntent();
        if(intent.hasExtra("textSpeed")) {
            Intent newIntent = new Intent(this, TestActivity.class);
            newIntent.putExtra("textSpeed", intent.getExtras().getInt("textSpeed"));
            finish();
            startActivity(newIntent);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Calibrate First!")
                    .setMessage("Calibrate your reading speed before starting the test.")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }

}
