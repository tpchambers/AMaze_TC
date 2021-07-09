package edu.wm.cs.cs301.amazebytheochambers.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.wm.cs.cs301.amazebytheochambers.R;
// Arrow shaped buttons taken from - https://looksok.wordpress.com/2013/08/24/android-triangle-arrow-defined-as-an-xml-shape/

/**
 * First State - AMazeActivity
 * Here is the title screen of the application
 * We initiate a maze_builder,seekbar,textview,skill level, driver, and room.
 * When calling create, we instantiate the seekBar to receive a skill input from the user (that one can slide).
 * We then create two different buttons, one to explore the maze, one to revisit a previous maze state.
 * For now, the revisit button will have the same function as the explorer.
 * When the user clicks on explore, we pass the strings and information received from the spinners to the next activity.
 * We then go to the generating state.
 */
public class AMazeActivity extends AppCompatActivity {
    String maze_builder;
    SeekBar seekBar;
    TextView textView;
    int skill_level;
    String driver;
    String room;

    /**
     * We take in the initial state, and set the content view to the title screen.
     * We collect the desired skill level and set the corresponding textview to show the users skill level.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amaze_activity);
        textView = (TextView) findViewById(R.id.MazeSkillLeveltext);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("Skill Level " + String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    /**
     * Here we create a method for the explorer button to use (and for now the revisit button).
     * We use Log.v to ensure we have the right information passed in the method.
     * We then create a new intent and add the users inputs to the intent.
     * The next activity is generating.
     * @param view
     */
    public void explore_click(View view) {
        Spinner mySpinner = (Spinner) findViewById(R.id.builder_spinner);
        Spinner spinner2 = (Spinner) findViewById(R.id.driver_spinner);
        Spinner spinner3 = (Spinner) findViewById(R.id.room_spinner);
        maze_builder = mySpinner.getSelectedItem().toString();
        skill_level = seekBar.getProgress();
        driver = spinner2.getSelectedItem().toString();
        room = spinner3.getSelectedItem().toString();
        Log.v("maze_generator",maze_builder);
        Log.v("skill_level",String.valueOf(skill_level));
        Log.v("driver",driver);
        Log.v("room",room);
        Intent intent = new Intent(AMazeActivity.this,Generating.class);
        intent.putExtra("maze_builder",maze_builder);
        intent.putExtra("skill_level",skill_level);
        intent.putExtra("driver",driver);
        intent.putExtra("room",room);
        startActivity(intent);
    }

    /**
     * This method is used to skip to the end if desired. Currently no button for this operation as I use it later.
     * @param view
     */
    public void go_finish(View view) {
        Intent intent = new Intent(this, FinishActivity.class);
        Toast.makeText(this, "skipped to end", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
