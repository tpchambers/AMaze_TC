package edu.wm.cs.cs301.amazebytheochambers.gui;

import edu.wm.cs.cs301.amazebytheochambers.generation.Maze;
import edu.wm.cs.cs301.amazebytheochambers.gui.Constants.UserInput;
import edu.wm.cs.cs301.amazebytheochambers.generation.Order.Builder;

/**
 * This is a default implementation of the State interface
 * with methods that do nothing but providing runtime exceptions
 * such that subclasses of this class can selectively override
 * those methods that are truly needed.
 *
 * @author Peter Kemper
 *
 */
public class DefaultState {

    private static final String MSG_UNIMPLEMENTED_METHOD = "DefaultState:using unimplemented method";

    public void start(MazePanel panel) {
        throw new RuntimeException(MSG_UNIMPLEMENTED_METHOD);
    }

    public void setFileName(String filename) {
        throw new RuntimeException(MSG_UNIMPLEMENTED_METHOD);
    }

    public void setSkillLevel(int skillLevel) {
        throw new RuntimeException(MSG_UNIMPLEMENTED_METHOD);
    }

    public void setSeed(int seed) {
        throw new RuntimeException(MSG_UNIMPLEMENTED_METHOD);
    }
    public void setPerfect(boolean isPerfect) {
        throw new RuntimeException(MSG_UNIMPLEMENTED_METHOD);
    }

    public void setMazeConfiguration(Maze config) {
        throw new RuntimeException(MSG_UNIMPLEMENTED_METHOD);
    }

    public void setPathLength(int pathLength) {
        throw new RuntimeException(MSG_UNIMPLEMENTED_METHOD);
    }

    public boolean keyDown(UserInput key, int value) {
        throw new RuntimeException(MSG_UNIMPLEMENTED_METHOD);
    }

    public void setBuilder(Builder dfs) {
        throw new RuntimeException(MSG_UNIMPLEMENTED_METHOD);
    }
}

