package edu.wm.cs.cs301.amazebytheochambers.gui;
import edu.wm.cs.cs301.amazebytheochambers.generation.Maze;

import android.content.Context;
import android.graphics.Bitmap;
// as recommended from class using https://stackoverflow.com/questions/4878159/whats-the-best-way-to-share-data-between-activities

public class Singleton {
    private static Singleton instance;
    private Maze maze;
    private Context context;
    private MazePanel mp;
    private StatePlaying state_playing;
    private static final Singleton holder = new Singleton();
    public static Singleton getInstance() {
        return holder;
    }
    public void set_maze_config(Maze maze) {
        this.maze = maze;
    }
    public void set_context(Context context) {this.context=context;}
    public Maze get_maze_config() {
        return maze;
    }
    public Context get_context() {return context;}
    public void set_maze_panel(MazePanel mp) {this.mp=mp;}
    public MazePanel get_maze_panel() {return this.mp;}
}
