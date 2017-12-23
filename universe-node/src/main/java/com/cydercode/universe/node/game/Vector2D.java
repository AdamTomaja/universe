package com.cydercode.universe.node.game;

public class Vector2D {

    private final int x, y;

    public static final Vector2D UP = new Vector2D(0, 1),
            DOWN = new Vector2D(0, -1),
            LEFT = new Vector2D(-1, 0),
            RIGHT = new Vector2D(1, 0);

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2D add(Vector2D vec) {
        return new Vector2D(this.x + vec.x, this.y + vec.y);
    }

    public double distance(Vector2D vec) {
        return Math.sqrt(Math.pow(x - vec.x, 2) + Math.pow(y - vec.y, 2));
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", x, y);
    }
}
