package com.gbreeze.crazykov.item;

public class ItemLayout {
    private final boolean[][] layout;
    private final ItemSize size;

    public ItemLayout(int width, int height, boolean[][] layout) {
        this.size = new ItemSize(width, height);
        this.layout = layout;
    }

    public boolean canPlaceAt(int x, int y) {
        return layout[y][x];
    }

    public boolean[][] getLayout() {
        return layout;
    }

    public ItemSize getSize() {
        return size;
    }

    public ItemLayout rotated() {
        int newWidth = size.height();
        int newHeight = size.width();
        boolean[][] rotatedLayout = new boolean[newHeight][newWidth];

        for (int y = 0; y < size.height(); y++) {
            for (int x = 0; x < size.width(); x++) {
                rotatedLayout[x][newWidth - y - 1] = layout[y][x];
            }
        }

        return new ItemLayout(newWidth, newHeight, rotatedLayout);
    }
}
