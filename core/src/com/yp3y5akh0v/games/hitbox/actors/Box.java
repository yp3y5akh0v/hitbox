package com.yp3y5akh0v.games.hitbox.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.yp3y5akh0v.games.hitbox.handlers.BoxChangedEvent;
import com.yp3y5akh0v.games.hitbox.handlers.BoxChangedListener;
import com.yp3y5akh0v.games.hitbox.handlers.ContactEvent;
import com.yp3y5akh0v.games.hitbox.handlers.ContactListener;
import com.yp3y5akh0v.games.hitbox.screens.GameScreen;

import java.util.HashMap;

import static com.yp3y5akh0v.games.hitbox.constans.Constants.*;

public class Box extends Actor {

    public Texture texture;
    public Rectangle rect;

    public ContactEvent contactEvent;
    public ContactListener contactListener;

    public BoxChangedListener boxChangedListener;
    public BoxChangedEvent boxChangedEvent;

    public float curDT;
    public int curStatus;
    public int curDirect;

    public float x0Left, x1Left, x0Right, x1Right;
    public float y0Down, y1Down, y0Up, y1Up;

    public Vector2 startPosition;

    public Box(Texture texture, Rectangle rect,
               ContactListener contactListener,
               BoxChangedListener boxChangedListener) {
        this.texture = texture;
        this.rect = rect;
        this.contactListener = contactListener;
        this.boxChangedListener = boxChangedListener;
        curDirect = UNKNOWN;
        curStatus = STOP;
        curDT = 0;
        setBounds(rect.x, rect.y, rect.getWidth(), rect.getHeight());
    }

    public Vector2 getDiscretePosition() {
        if (curStatus != MOVE)
            return getPosition();
        else {
            Vector2 res = null;
            switch (curDirect) {
                case UP:
                    res = new Vector2(getX(), y1Up);
                    break;
                case RIGHT:
                    res = new Vector2(x1Right, getY());
                    break;
                case DOWN:
                    res = new Vector2(getX(), y1Down);
                    break;
                case LEFT:
                    res = new Vector2(x1Left, getY());
                    break;
            }
            return res;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (curStatus == MOVE) {
            GameScreen gs = getGameScreen();
            HashMap<Vector2, Actor> vectorActor = gs.vectorActor;
            curDT += delta * BOX_SPEED * TRANSITION_TIME;
            switch (curDirect) {
                case LEFT:
                    if (curDT < 1) {
                        float curX = x0Left + (x1Left - x0Left) * curDT;
                        setPosition(curX, getY());
                    } else {
                        setPosition(x1Left, getY());
                        curDT = 0;
                        x0Left = x1Left;
                        x1Left = x0Left - gs.tilePixelWidth;
                        Vector2 vnLeft = new Vector2(x1Left, getY());
                        Actor nLeft = vectorActor.get(vnLeft);
                        if (nLeft != null) {
                            fireContactEvent(nLeft);
                            if (nLeft instanceof Player) {
                                ((Player) nLeft).explosion();
                                vectorActor.remove(getPosition());
                                vectorActor.put(vnLeft, this);
                            } else if (nLeft instanceof Bot) {
                                ((Bot) nLeft).explosion();
                                vectorActor.remove(getPosition());
                                vectorActor.put(vnLeft, this);
                            } else {
                                curDirect = UNKNOWN;
                                curStatus = STOP;
                                fireBoxChangedPositionEvent(startPosition);
                            }
                        } else {
                            vectorActor.remove(getPosition());
                            vectorActor.put(vnLeft, this);
                        }
                    }
                    break;
                case RIGHT:
                    if (curDT < 1) {
                        float curX = x0Right + (x1Right - x0Right) * curDT;
                        setPosition(curX, getY());
                    } else {
                        setPosition(x1Right, getY());
                        curDT = 0;
                        x0Right = x1Right;
                        x1Right = x0Right + gs.tilePixelWidth;
                        Vector2 vnRight = new Vector2(x1Right, getY());
                        Actor nRight = vectorActor.get(vnRight);
                        if (nRight != null) {
                            fireContactEvent(nRight);
                            if (nRight instanceof Player) {
                                ((Player) nRight).explosion();
                                vectorActor.remove(getPosition());
                                vectorActor.put(vnRight, this);
                            } else if (nRight instanceof Bot) {
                                ((Bot) nRight).explosion();
                                vectorActor.remove(getPosition());
                                vectorActor.put(vnRight, this);
                            } else {
                                curDirect = UNKNOWN;
                                curStatus = STOP;
                                fireBoxChangedPositionEvent(startPosition);
                            }
                        } else {
                            vectorActor.remove(getPosition());
                            vectorActor.put(vnRight, this);
                        }
                    }
                    break;
                case UP:
                    if (curDT < 1) {
                        float curY = y0Up + (y1Up - y0Up) * curDT;
                        setPosition(getX(), curY);
                    } else {
                        setPosition(getX(), y1Up);
                        curDT = 0;
                        y0Up = y1Up;
                        y1Up = y0Up + gs.tilePixelHeight;
                        Vector2 vnUp = new Vector2(getX(), y1Up);
                        Actor nUp = vectorActor.get(vnUp);
                        if (nUp != null) {
                            fireContactEvent(nUp);
                            if (nUp instanceof Player) {
                                ((Player) nUp).explosion();
                                vectorActor.remove(getPosition());
                                vectorActor.put(vnUp, this);
                            } else if (nUp instanceof Bot) {
                                ((Bot) nUp).explosion();
                                vectorActor.remove(getPosition());
                                vectorActor.put(vnUp, this);
                            } else {
                                curDirect = UNKNOWN;
                                curStatus = STOP;
                                fireBoxChangedPositionEvent(startPosition);
                            }
                        } else {
                            vectorActor.remove(getPosition());
                            vectorActor.put(vnUp, this);
                        }
                    }
                    break;
                case DOWN:
                    if (curDT < 1) {
                        float curY = y0Down + (y1Down - y0Down) * curDT;
                        setPosition(getX(), curY);
                    } else {
                        setPosition(getX(), y1Down);
                        curDT = 0;
                        y0Down = y1Down;
                        y1Down = y0Down - gs.tilePixelHeight;
                        Vector2 vnDown = new Vector2(getX(), y1Down);
                        Actor nDown = vectorActor.get(vnDown);
                        if (nDown != null) {
                            fireContactEvent(nDown);
                            if (nDown instanceof Player) {
                                ((Player) nDown).explosion();
                                vectorActor.remove(getPosition());
                                vectorActor.put(vnDown, this);
                            } else if (nDown instanceof Bot) {
                                ((Bot) nDown).explosion();
                                vectorActor.remove(getPosition());
                                vectorActor.put(vnDown, this);
                            } else {
                                curDirect = UNKNOWN;
                                curStatus = STOP;
                                fireBoxChangedPositionEvent(startPosition);
                            }
                        } else {
                            vectorActor.remove(getPosition());
                            vectorActor.put(vnDown, this);
                        }
                    }
                    break;
            }
        }
    }

    public void startMoveRight() {
        GameScreen gs = getGameScreen();
        x0Right = getX();
        startPosition = new Vector2(x0Right, getY());
        x1Right = x0Right + gs.tilePixelWidth;
        HashMap<Vector2, Actor> vectorActor = gs.vectorActor;
        Vector2 vnRight = new Vector2(x1Right, getY());
        Actor nRight = vectorActor.get(vnRight);
        if (nRight != null) {
            fireContactEvent(nRight);
            if (nRight instanceof Player) {
                ((Player) nRight).explosion();
                vectorActor.remove(getPosition());
                vectorActor.put(vnRight, this);
                curDirect = RIGHT;
                curStatus = MOVE;
            } else if (nRight instanceof Bot) {
                ((Bot) nRight).explosion();
                vectorActor.remove(getPosition());
                vectorActor.put(vnRight, this);
                curDirect = RIGHT;
                curStatus = MOVE;
            } else {
                explosion();
            }
        } else {
            vectorActor.remove(getPosition());
            vectorActor.put(vnRight, this);
            curDirect = RIGHT;
            curStatus = MOVE;
        }
    }

    public void startMoveLeft() {
        GameScreen gs = getGameScreen();
        x0Left = getX();
        startPosition = new Vector2(x0Left, getY());
        x1Left = x0Left - gs.tilePixelWidth;
        HashMap<Vector2, Actor> vectorActor = gs.vectorActor;
        Vector2 vnLeft = new Vector2(x1Left, getY());
        Actor nLeft = vectorActor.get(vnLeft);
        if (nLeft != null) {
            fireContactEvent(nLeft);
            if (nLeft instanceof Player) {
                Player player = (Player) nLeft;
                player.explosion();
                curDirect = LEFT;
                curStatus = MOVE;
                vectorActor.remove(getPosition());
                vectorActor.put(vnLeft, this);
            } else if (nLeft instanceof Bot) {
                ((Bot) nLeft).explosion();
                vectorActor.remove(getPosition());
                vectorActor.put(vnLeft, this);
                curDirect = LEFT;
                curStatus = MOVE;
            } else {
                explosion();
            }
        } else {
            vectorActor.remove(getPosition());
            vectorActor.put(vnLeft, this);
            curDirect = LEFT;
            curStatus = MOVE;
        }
    }

    public void startMoveUp() {
        GameScreen gs = getGameScreen();
        y0Up = getY();
        startPosition = new Vector2(getX(), y0Up);
        y1Up = y0Up + gs.tilePixelHeight;
        HashMap<Vector2, Actor> vectorActor = gs.vectorActor;
        Vector2 vnUp = new Vector2(getX(), y1Up);
        Actor nUp = vectorActor.get(vnUp);
        if (nUp != null) {
            fireContactEvent(nUp);
            if (nUp instanceof Player) {
                Player player = (Player) nUp;
                player.explosion();
                curDirect = UP;
                curStatus = MOVE;
                vectorActor.remove(getPosition());
                vectorActor.put(vnUp, this);
            } else if (nUp instanceof Bot) {
                ((Bot) nUp).explosion();
                vectorActor.remove(getPosition());
                vectorActor.put(vnUp, this);
                curDirect = UP;
                curStatus = MOVE;
            } else {
                explosion();
            }
        } else {
            vectorActor.remove(getPosition());
            vectorActor.put(vnUp, this);
            curDirect = UP;
            curStatus = MOVE;
        }
    }

    public void startMoveDown() {
        GameScreen gs = getGameScreen();
        y0Down = getY();
        startPosition = new Vector2(getX(), y0Down);
        y1Down = y0Down - gs.tilePixelHeight;
        HashMap<Vector2, Actor> vectorActor = gs.vectorActor;
        Vector2 vnDown = new Vector2(getX(), y1Down);
        Actor nDown = vectorActor.get(vnDown);
        if (nDown != null) {
            fireContactEvent(nDown);
            if (nDown instanceof Player) {
                Player player = (Player) nDown;
                player.explosion();
                curDirect = DOWN;
                curStatus = MOVE;
                vectorActor.remove(getPosition());
                vectorActor.put(vnDown, this);
            } else if (nDown instanceof Bot) {
                ((Bot) nDown).explosion();
                vectorActor.remove(getPosition());
                vectorActor.put(vnDown, this);
                curDirect = DOWN;
                curStatus = MOVE;
            } else {
                explosion();
            }
        } else {
            vectorActor.remove(getPosition());
            vectorActor.put(vnDown, this);
            curDirect = DOWN;
            curStatus = MOVE;
        }
    }

    public void explosion() {
        curStatus = DEATH;
        GameScreen gs = getGameScreen();
        gs.boxes.removeValue(this, true);
        gs.countBoxLabel.setText("Box: " + gs.boxes.size);
        gs.vectorActor.remove(getDiscretePosition());
        gs.gameStage.getRoot().removeActor(this);
        fireBoxRemovedEvent();
    }

    public GameScreen getGameScreen() {
        return ((GameScreen) contactListener);
    }

    public void fireBoxChangedPositionEvent(Vector2 prevPosition) {
        boxChangedEvent = new BoxChangedEvent(this);
        boxChangedListener.boxPositionChanged(boxChangedEvent, prevPosition);
    }

    public void fireBoxRemovedEvent() {
        boxChangedEvent = new BoxChangedEvent(this);
        boxChangedListener.boxRemoved(boxChangedEvent);
    }

    public void fireContactEvent(Actor actor) {
        contactEvent = new ContactEvent(this, actor);
        contactListener.beginContact(contactEvent);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }
}
