package com.yp3y5akh0v.games.hitbox.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Queue;
import com.yp3y5akh0v.games.hitbox.actors.Box;
import com.yp3y5akh0v.games.hitbox.actors.Player;
import com.yp3y5akh0v.games.hitbox.handlers.ObjectChangedEvent;
import com.yp3y5akh0v.games.hitbox.handlers.ObjectChangedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.yp3y5akh0v.games.hitbox.constans.Constants.*;

public class FlowField implements ObjectChangedListener {

    public GameScreen gs;
    public boolean targetRemoved;

    public HashMap<Vector2, Node> mapNode;
    public boolean[] usedNode;

    public List<Class> obstacleObjectTypeList;

    public FlowField(GameScreen gs) {
        this.gs = gs;
        this.obstacleObjectTypeList = new ArrayList<>();
    }

    public void addObstacleObjectType(Class c) {
        obstacleObjectTypeList.add(c);
    }

    public void removeObstacleObjectType(Class c) {
        obstacleObjectTypeList.remove(c);
    }

    public void init() {
        mapNode = new HashMap<>();
        usedNode = new boolean[gs.mapHeight * gs.mapWidth];
        HashMap<Vector2, Actor> vectorActor = gs.vectorActor;
        for (int i = 0; i < gs.mapHeight; i++)
            for (int j = 0; j < gs.mapWidth; j++) {
                Vector2 v = new Vector2(gs.tilePixelWidth * j, gs.tilePixelHeight * i);
                boolean isPassable = true;
                if (vectorActor.containsKey(v)) {
                    Actor actor = vectorActor.get(v);
                    if (obstacleObjectTypeList.contains(actor.getClass()))
                        isPassable = false;
                }
                mapNode.put(v, new Node(v, isPassable));
            }
        for (int i = 0; i < gs.mapHeight; i++)
            for (int j = 0; j < gs.mapWidth; j++) {
                Vector2 v = new Vector2(gs.tilePixelWidth * j, gs.tilePixelHeight * i);
                Node node = mapNode.get(v);
                //for North
                if (i + 1 < gs.mapHeight)
                    node.neighbors[0] = mapNode.get(new Vector2(gs.tilePixelWidth * j,
                            gs.tilePixelHeight * (i + 1)));
                //for East
                if (j + 1 < gs.mapWidth)
                    node.neighbors[1] = mapNode.get(new Vector2(gs.tilePixelWidth * (j + 1),
                            gs.tilePixelHeight * i));
                //for South
                if (i - 1 >= 0)
                    node.neighbors[2] = mapNode.get(new Vector2(gs.tilePixelWidth * j,
                            gs.tilePixelHeight * (i - 1)));
                //for West
                if (j - 1 >= 0)
                    node.neighbors[3] = mapNode.get(new Vector2(gs.tilePixelWidth * (j - 1),
                            gs.tilePixelHeight * i));
            }
    }

    public int getId(Vector2 pos) {
        return (int) (pos.y / gs.tilePixelHeight * gs.mapWidth + pos.x / gs.tilePixelWidth);
    }

    public void calculateField(Vector2 targetPosition) {
        resetAll();
        Node targetNode = mapNode.get(targetPosition);
        targetNode.cost = 0;
        Queue<Node> q = new Queue<>();
        q.addFirst(targetNode);
        usedNode[getId(targetPosition)] = true;
        while (q.size != 0) {
            Node curNode = q.removeFirst();
            for (int i = 0; i < 4; i++) {
                Node neighbor = curNode.neighbors[i];
                if (neighbor != null && neighbor.isPassable) {
                    if (usedNode[getId(neighbor.pos)] &&
                            neighbor.cost > curNode.cost + 1) {
                        if (q.indexOf(neighbor, false) != -1)
                            q.removeValue(neighbor, false);
                        neighbor.cost = curNode.cost + 1;
                        neighbor.dir = getDirectionByIndex((i + 2) % 4);
                        q.addFirst(neighbor);
                    } else if (!usedNode[getId(neighbor.pos)]) {
                        neighbor.cost = curNode.cost + 1;
                        neighbor.dir = getDirectionByIndex((i + 2) % 4);
                        q.addFirst(neighbor);
                        usedNode[getId(neighbor.pos)] = true;
                    }
                }
            }
        }
    }

    public int getDirectionByIndex(int number) {
        int dir = UNKNOWN;
        switch (number) {
            case 0:
                dir = UP;
                break;
            case 1:
                dir = RIGHT;
                break;
            case 2:
                dir = DOWN;
                break;
            case 3:
                dir = LEFT;
                break;
        }
        return dir;
    }

    public void resetAll() {
        Arrays.fill(usedNode, false);
        for (Node node : mapNode.values()) {
            node.cost = 0;
            node.dir = UNKNOWN;
        }
    }

    public void showIntegrationField() {
        for (int i = gs.mapHeight - 1; i >= 0; i--) {
            for (int j = 0; j < gs.mapWidth; j++) {
                Node node = mapNode.get(new Vector2(gs.tilePixelWidth * j, gs.tilePixelHeight * i));
                System.out.print((node.isPassable ? node.cost : "x") + "\t");
            }
            System.out.println();
        }
    }

    public void showFlowField() {
        for (int i = gs.mapHeight - 1; i >= 0; i--) {
            for (int j = 0; j < gs.mapWidth; j++) {
                Node node = mapNode.get(new Vector2(gs.tilePixelWidth * j, gs.tilePixelHeight * i));
                System.out.print((node.isPassable ? node.dir : "x") + "\t");
            }
            System.out.println();
        }
    }

    @Override
    public void positionChanged(ObjectChangedEvent oce, Vector2 prevPosition) {
        Object obj = oce.getSource();
        if (obj instanceof Player) {
            calculateField(((Player) obj).getDiscretePosition());
        } else if (obj instanceof Box) {
            if (!targetRemoved) {
                Box box = (Box) obj;
                Node oldObstacle = mapNode.get(prevPosition);
                Node newObstacle = mapNode.get(box.getDiscretePosition());
                oldObstacle.isPassable = true;
                newObstacle.isPassable = false;

                Player player = gs.player;
                Vector2 playerPos = player.getDiscretePosition();
                calculateField(playerPos);
            }
        }
    }

    @Override
    public void removed(ObjectChangedEvent oce) {
        Object obj = oce.getSource();
        if (obj instanceof Player) {
            resetAll();
            targetRemoved = true;
        } else if (obj instanceof Box) {
            if (!targetRemoved) {
                Box box = (Box) obj;
                Node rNode = mapNode.get(box.getPosition());
                rNode.isPassable = true;

                Player player = gs.player;
                Vector2 playerPos = player.getDiscretePosition();
                calculateField(playerPos);
            }
        }
    }
}
