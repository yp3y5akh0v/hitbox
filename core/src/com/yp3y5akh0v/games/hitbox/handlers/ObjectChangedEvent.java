package com.yp3y5akh0v.games.hitbox.handlers;

import java.util.EventObject;

public class ObjectChangedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ObjectChangedEvent(Object source) {
        super(source);
    }
}
