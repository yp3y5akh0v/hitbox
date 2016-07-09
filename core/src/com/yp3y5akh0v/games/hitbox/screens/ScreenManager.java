package com.yp3y5akh0v.games.hitbox.screens;

import com.badlogic.gdx.Screen;

import java.util.Stack;

public class ScreenManager {

    public Stack<Screen> screenStack;

    public ScreenManager() {
        screenStack = new Stack<>();
    }

    public void push(Screen screen) {
        screenStack.push(screen);
    }

    public Screen pop(Screen screen) {
        return screenStack.pop();
    }

    public void setScreen(Screen screen) {
        if (!screenStack.isEmpty())
            screenStack.pop();
        screenStack.push(screen);
    }

    public Screen peek() {
        return screenStack.peek();
    }

    public boolean isEmpty() {
        return screenStack.isEmpty();
    }

    public int size() {
        return screenStack.size();
    }

}
