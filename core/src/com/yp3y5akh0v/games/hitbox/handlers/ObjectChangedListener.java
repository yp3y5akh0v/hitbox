package com.yp3y5akh0v.games.hitbox.handlers;

import com.badlogic.gdx.math.Vector2;

public interface ObjectChangedListener {
    void positionChanged(ObjectChangedEvent oce, Vector2 prevPosition);

    void removed(ObjectChangedEvent oce);
}
