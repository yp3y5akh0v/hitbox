package com.yp3y5akh0v.games.hitbox.handlers;

import com.badlogic.gdx.math.Vector2;
import com.yp3y5akh0v.games.hitbox.actors.Player;

public interface BoxChangedListener {
    void boxPositionChanged(BoxChangedEvent bce, Vector2 prevPosition);

    void boxRemoved(BoxChangedEvent bce);
}
