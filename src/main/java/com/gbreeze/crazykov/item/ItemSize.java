package com.gbreeze.crazykov.item;

public record ItemSize(int width, int height) {
    public ItemSize rotated() {
        return new ItemSize(height, width);
    }

    public int slotCount() {
        return width * height;
    }
}
