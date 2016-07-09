package com.yp3y5akh0v.games.hitbox.handlers;

public interface TargetChangedListener {
    void targetPositionChanged(TargetChangedEvent tce);

    void targetRemoved(TargetChangedEvent tce);
}
