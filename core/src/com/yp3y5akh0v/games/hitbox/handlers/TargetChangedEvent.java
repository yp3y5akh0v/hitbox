package com.yp3y5akh0v.games.hitbox.handlers;

import java.util.EventObject;

public class TargetChangedEvent extends EventObject {

    public TargetChangedEvent(Object source) {
        super(source);
    }

    public TargetChangedEvent() {
        this(null);
    }
}
