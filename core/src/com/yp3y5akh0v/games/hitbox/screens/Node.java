package com.yp3y5akh0v.games.hitbox.screens;

import com.badlogic.gdx.math.Vector2;

import static com.yp3y5akh0v.games.hitbox.constans.Constants.UNKNOWN;

public class Node {

    public Vector2 pos;
    public int dir;
    public int cost;
    public boolean isPassable;
    public Node[] neighbors;

    public Node(Vector2 pos, boolean isPassable) {
        this.pos = pos;
        this.isPassable = isPassable;
        this.dir = UNKNOWN;
        this.neighbors = new Node[4];
        this.cost = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return pos.equals(node.pos);
    }

    @Override
    public int hashCode() {
        return pos.hashCode();
    }
}
