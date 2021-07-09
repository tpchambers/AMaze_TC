package edu.wm.cs.cs301.amazebytheochambers.gui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import edu.wm.cs.cs301.amazebytheochambers.R;
import edu.wm.cs.cs301.amazebytheochambers.generation.Maze;


public class PlayManuallyActivity extends AppCompatActivity {
    Singleton data_holder;
    MazePanel panel;
    StatePlaying playing;
    int px, py ; // current position on maze grid (x,y)
    int path_length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmanually);
        panel = findViewById(R.id.panel);
        playing = new StatePlaying();
        playing.start(panel); //start by passing in current mazepanel object);
        path_length = 0;
        Log.v("thread update","finished generation");
        Intent receiver = getIntent();
        data_holder = new Singleton();
        data_holder.getInstance().set_context(PlayManuallyActivity.this);
        Bundle bundle_receiver = receiver.getExtras();
        String maze_builder = bundle_receiver.getString("maze_builder");
        int skill_level = bundle_receiver.getInt("skill_level");
        int seed = bundle_receiver.getInt("seed");
        String driver = bundle_receiver.getString("driver");
        String room = bundle_receiver.getString("room");
        Log.v("maze_generator",maze_builder);
        Log.v("skill_level",String.valueOf(skill_level));
        Log.v("driver",driver);
        Log.v("room",room);
    }

    /**
     * the following three methods are the toggle map, walls, and solution buttons
     * @param view
     */
    public void show_walls(View view) {
        playing.keyDown(Constants.UserInput.TOGGLELOCALMAP, 1);
        Log.v("walls ", "walls");
    }

    public void show_maze(View view) {
        playing.keyDown(Constants.UserInput.TOGGLEFULLMAP, 1);
        Log.v("full maze", "full maze");
    }

    public void show_path(View view) {
        playing.keyDown(Constants.UserInput.TOGGLESOLUTION, 1);
        Log.v("showing solution", "solution");
    }

    /**
     * Changed operation move forward to include the case where the user finds the maze. This essentially does what StatePlaying used to do.
     * @param view
     */
    public void move_forward(View view) {
        playing.keyDown(Constants.UserInput.UP, 1);
        path_length++;
        Log.v("cells traversed",String.valueOf(path_length));
        Log.v("forward ", "forward");
        if (playing.isOutside(playing.px,playing.py)) {
            //we check current playing state if the position is outside
            //if so, we create a new intent to go to the FinishActivity class - the player has one
            Intent intent = new Intent(PlayManuallyActivity.this,FinishActivity.class);
            intent.putExtra("path_length",path_length);
            startActivity(intent);
            finish();
        }
    }

    public void move_backward(View view) {
        playing.keyDown(Constants.UserInput.DOWN, 1);
        Log.v("backwards", "backwards");
    }

    public void move_right(View view) {
        playing.keyDown(Constants.UserInput.RIGHT, 1);
        Log.v("right", "right");
    }

    public void move_left(View view) {
        playing.keyDown(Constants.UserInput.LEFT, 1);
        Log.v("left", "left");
    }

    @Override
    public void onBackPressed() {
        Intent receiver = getIntent();
        Bundle bundle_receiver = receiver.getExtras();
        String maze_builder = bundle_receiver.getString("maze_builder");
        int skill_level = bundle_receiver.getInt("skill_level");
        int seed = bundle_receiver.getInt("seed");
        String driver = bundle_receiver.getString("driver");
        String room = bundle_receiver.getString("room");
       Intent intent = new Intent(PlayManuallyActivity.this,AMazeActivity.class);
       intent.putExtra("seed",seed);
       intent.putExtra("maze_builder",maze_builder);
       intent.putExtra("skill_level",skill_level);
       intent.putExtra("driver",driver);
       intent.putExtra("room",room);
       startActivity(intent);
       finish();
    }
}