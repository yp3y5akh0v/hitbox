package com.yp3y5akh0v.games.hitbox.handlers;

import java.util.EventObject;

public class ContactEvent extends EventObject {

    private transient Object contactSource;

    public ContactEvent(Object source, Object contactSource) {
        super(source);
        this.contactSource = contactSource;
    }

    public ContactEvent() {
        super(null);
        this.contactSource = null;
    }

    public Object getcSource() {
        return contactSource;
    }

}
