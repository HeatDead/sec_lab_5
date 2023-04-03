package com.example.sec_lab_5.utils;

public class RGBColor {
    private final int red;
    private final int green;
    private final int blue;

    public RGBColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public static RGBColor unpack(int color) {
        int blue = color & 255;
        int green = (color >> 8) & 255;
        int red = (color >> 16) & 255;
        return new RGBColor(red, green, blue);
    }

    public int pack() {
        return (((red << 8)|green) << 8)|blue;
    }
}
