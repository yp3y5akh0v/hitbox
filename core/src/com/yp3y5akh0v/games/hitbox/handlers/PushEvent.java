package com.yp3y5akh0v.games.hitbox.handlers;

import java.util.EventObject;

public class PushEvent extends EventObject {

    public transient Object pushedObject;

    public PushEvent(Object source, Object pushedObject) {
        super(source);
        this.pushedObject = pushedObject;
    }

    public PushEvent() {
        this(null, null);
    }

    public Object getPushedObject() {
        return pushedObject;
    }
}
