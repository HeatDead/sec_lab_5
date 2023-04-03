package com.example.sec_lab_5.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SteganographyUtil {
    private static final File DIR_RESULTS = new File("");

    static {
        if (!DIR_RESULTS.exists())
            DIR_RESULTS.mkdir();
    }

    @SneakyThrows
    public static BufferedImage reader(File file) {
        return ImageIO.read(file);
    }


    public static File integrateMessage(File file, byte[] msg) {
        return integrateMessage(file, msg, createEncryptedFileResult(file));
    }

    @SneakyThrows
    public static File integrateMessage(File file, byte[] msg, File out) {
        msg = Arrays.copyOf(msg, msg.length + 1);
        msg[msg.length - 1] = 10;
        BufferedImage image = reader(file);
        int x = 0, y = 0;
        for (int i = 0; i < msg.length; i++) {
            RGBColor color = RGBColor.unpack(image.getRGB(x, y));
            RGBColor rgbColor = integrateMessageUnit(color, msg[i]);
            image.setRGB(x, y, rgbColor.pack());
            x++;
            if (x == image.getWidth()) {
                x = 0;
                y++;
                if (y == image.getHeight())
                    throw new IllegalStateException("End of image");
            }
        }
        ImageIO.write(image, "PNG", out);
        return out;
    }

    @SneakyThrows
    public static byte[] disintegrateMessage(File file) {
        List<Byte> bytes = new LinkedList<>();
        BufferedImage image = reader(file);
        int x = 0, y = 0;
        while (true) {
            RGBColor color = RGBColor.unpack(image.getRGB(x, y));
            byte b = disintegrateMessageUnit(color);
            if (b == 10)
                return ArrayUtils.toPrimitive(bytes.toArray(new Byte[0]));
            bytes.add(b);
            x++;
            if (x == image.getWidth()) {
                x = 0;
                y++;
                if (y == image.getHeight())
                    throw new IllegalStateException("End of image");
            }
        }
    }

    @SneakyThrows
    private static File createEncryptedFileResult(File sourceFile) {
        String s = FilenameUtils.getBaseName(sourceFile.getName());
        return new File(DIR_RESULTS.getAbsolutePath()
                + '/'
                + s
                + "-ENCRPYPTED."
                + FilenameUtils.getExtension(sourceFile.getName()));
    }

    private static RGBColor integrateMessageUnit(RGBColor pixel, byte msgUnit) {
        //System.out.println(msgUnit);
        int blue = (pixel.getBlue() >> 2 << 2) | (msgUnit & 3);
        int green = (pixel.getGreen() >> 3 << 3) | ((msgUnit >> 2) & 7);
        int red = (pixel.getRed() >> 3 << 3) | ((msgUnit >> 5) & 7);
        return new RGBColor(red, green, blue);
    }

    private static byte disintegrateMessageUnit(RGBColor pixel) {
        int i = ((((pixel.getRed() & 7) << 3) | (pixel.getGreen() & 7)) << 2) | (pixel.getBlue() & 3);
        byte b = (byte) i;
        return b;
    }
}
