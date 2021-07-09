package edu.wm.cs.cs301.amazebytheochambers.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import edu.wm.cs.cs301.amazebytheochambers.R;
import edu.wm.cs.cs301.amazebytheochambers.generation.Order;
import edu.wm.cs.cs301.amazebytheochambers.generation.Maze;
import edu.wm.cs.cs301.amazebytheochambers.generation.MazeFactory;
import java.util.Random;

/**
 * Updated generating activity:
 *
 * We implement order in order to properly order a maze from a new maze factory instance.
 * We delegate the order given custom methods I wrote below to customize the order.
 * The customized methods set the specific maze configuration attributes given by the first state AMazeActivity (or what the user chose).
 * We set the driver, skill level, and room option. The seed is randomly generated.
 *
 */
public class Generating extends AppCompatActivity implements Order {
    public static Handler ui_handler;
    public Singleton data_holder;
    public static MazePanel mp;
    private Boolean room;
    private ProgressBar progressBar;
    Intent intent;
    MazeFactory mazefactory;
    Builder builder;
    private int seed;
    private int skill_level;
    private TextView progress_bar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // on create we create a new handler which we use to override the deliver method below
        ui_handler = new Handler();
        mazefactory = new MazeFactory(); // mazefactory to order the maze from
        data_holder = new Singleton(); //singleton to reference the data container statically
        Intent receiver = getIntent();
        Bundle bundle_receiver = receiver.getExtras();
        String maze_builder = bundle_receiver.getString("maze_builder");
        int skill_level = bundle_receiver.getInt("skill_level");
        String driver = bundle_receiver.getString("driver");
        String room = bundle_receiver.getString("room");
        intent = new Intent(this, PlayManuallyActivity.class);
        intent.putExtra("maze_builder",maze_builder);
        intent.putExtra("skill_level",skill_level);
        intent.putExtra("driver",driver);
        intent.putExtra("room",room);
        //here we set all of the maze configurations according to what the user selected
        //for testing, please place manually if desired
        setBuilder(maze_builder);
        set_room(room); //for clarity, false (no) for no rooms, true (yes) for rooms
        setSkillLevel(skill_level);
        Random random = new Random();
        setSeed(random.nextInt(100));
        Log.v("seed",String.valueOf(getSeed()));
        intent.putExtra("seed",getSeed());
        mazefactory.order(this); //this will order an order object filled by implementing the order class
        // the set methods will return the appropriate maze build
        //for now we do skill level 1, no rooms, and seed 1 to test
        progress_bar_text = (TextView) findViewById(R.id.progress_bar_text);
    }

    @Override
    public int getSkillLevel() {
        return this.skill_level;
    }

    @Override
    public Builder getBuilder() {
        return this.builder;
    }

    /**
     * Updated isPerfect to check if the current room boolean is true or false.
     * We set room to true, if we don't want a perfect maze.
     * @return
     */
    @Override
    public boolean isPerfect() {
        if (this.room == true) {
            return false;
        }
        else {
            return true;
        }
    }
    @Override
    public int getSeed() {
        return this.seed;
    }

    /**
     * Changed devlier to get the current instance of the singleton, and place the maze configuration into the data container.
     * @param mazeConfig is the maze that is delivered in response to an order
     */
    @Override
    public void deliver(Maze mazeConfig) {
        data_holder.getInstance().set_maze_config(mazeConfig); //get current instance of singleton and set the maze
        startActivity(intent); // start activity
        finish(); //finish current one
    }

    /**
     * Here we set the room option, which isPerfect() will flip.
     * I added this to improve readability. Intuitively, it makes sense to have rooms = yes = true.
     * Then, isPerfect() is called by the order and flips it so the maze will behave correspondingly.
     * @param room
     * @return
     */
    public Boolean set_room(String room) {
        if (room.equals("Yes")) {
            return this.room = true;
        }
        else {
            return this.room = false;
        }
    }

    /**
     * Custom method to set the current seed.
     * Current seed is generated randomly.
     * @param seed
     * @return
     */
    public int setSeed(int seed) {
        return this.seed = seed;
    }

    /**
     * Added method to set builder based on the string received from the previous state.
     * @param builder
     */
    public void setBuilder(String builder) {
        if (builder.equals("Kruskal")) {
            this.builder = Builder.Kruskal;
        }
        else if (builder.equals("Prim")) {
            this.builder = Builder.Prim;
        }
        else if (builder.equals("DFS")) {
            this.builder = Builder.DFS;
        }
    }

    /**
     * We return the current ongoing order, this is used in the background thread updateProgress().
     * @return
     */
    public Order getOrder() {
        return this;
    }

    /**
     * Set the current skill level to the reference desired.
     * @param skill_level
     * @return
     */
    public int setSkillLevel(int skill_level) {
        return this.skill_level = skill_level;
    }


    /**
     * Handler overrides update Progress and run. Calls a new runnable and updates the UI while the background thread generates the maze.
     * As long as the current order is not null, we know that the thread has not terminated and we continue to update until the progress bar reaches 100.
     * This will show the actual progress of the generation being done on the background thread. Once the order is null, we set the progress to 100 and ensure that the order is in fact null.
     * @param percentage of job completion
     */
    @Override
    public void updateProgress(int percentage) {
        ui_handler.post(new Runnable() {
            @Override
            public void run() {
                //we reference the current order
                Order curr_order = getOrder();
                //as long as the current order is not null, we call update percentage, which will continue to update the progressbar until terminated
                if (curr_order != null) {
                    try {
                        update(percentage);
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    //make sure order is null and set the progress to 100
                    curr_order = null;
                    progressBar.setProgress(100);
                }
            }
        });
    }

    /**
     * Helper method that is called in Handler to update UI in background thread.
     * @param percentage
     */
    private void update(int percentage) {
        percentage++;
        progressBar.setProgress(percentage);
        progress_bar_text.setText("Percent complete " + String.valueOf(percentage-1));
    }

    /**
     * Override back pressed / terminate thread if desired.
     */
    @Override
    public void onBackPressed() {
        this.startActivity(new Intent(Generating.this,AMazeActivity.class)); // easier way to override generation
        finish(); // finish current activity
    }
}