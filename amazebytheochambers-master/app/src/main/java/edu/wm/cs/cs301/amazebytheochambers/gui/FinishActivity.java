package edu.wm.cs.cs301.amazebytheochambers.gui;

import android.app.ActionBar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import org.w3c.dom.Text;
import edu.wm.cs.cs301.amazebytheochambers.R;
import edu.wm.cs.cs301.amazebytheochambers.databinding.AmazeActivityBinding;

/**
 * This is last activity/screen of our application.
 * We once again, make sure we receive the correct input from the intents.
 * We then check to see what type of driver we receive.
 * If the driver is a robot, we show the path length incurred.
 * Else, we have a statement telling the user to try a different skill level.
 */
public class FinishActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView textView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Intent receiver = getIntent();
        Bundle bundle = receiver.getExtras();
        int path_length= bundle.getInt("path_length");
        TextView text_finish = findViewById(R.id.path_length);
        text_finish.setText("Your path length was " + String.valueOf(path_length));
    }

    /**
     * Ensuring that we go back to the title from here.
     */
    public void go_start(View view) {
        finish();
        Log.d("resetting maze","resetting maze");
        Intent intent = new Intent(this, AMazeActivity.class);
        Toast.makeText(this, "going back to title screen", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
    /**
     * Making sure we go back to the title.
     */
    @Override
    public void onBackPressed() {
        Thread.interrupted();
        finish();
        Log.d("Back press called","back press called");
        Intent intent = new Intent(this,AMazeActivity.class);
        startActivity(intent);
    }
}