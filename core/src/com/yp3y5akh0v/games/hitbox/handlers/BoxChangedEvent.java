package com.yp3y5akh0v.games.hitbox.handlers;

import java.util.EventObject;

public class BoxChangedEvent extends EventObject {

    public BoxChangedEvent(Object source) {
        super(source);
    }

    public BoxChangedEvent() {
        this(null);
    }
}
