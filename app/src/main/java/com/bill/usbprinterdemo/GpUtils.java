package com.bill.usbprinterdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: Bill
 * created on: 17/11/24 下午4:47
 * description: 打印工具类
 */
public class GpUtils {
    private static Pattern pattern = Pattern.compile("([a-zA-Z0-9!@#$^&*\\(\\)~\\{\\}:\",\\.<>/]+)");
    private static int[] p0 = new int[]{0, 128};
    private static int[] p1 = new int[]{0, 64};
    private static int[] p2 = new int[]{0, 32};
    private static int[] p3 = new int[]{0, 16};
    private static int[] p4 = new int[]{0, 8};
    private static int[] p5 = new int[]{0, 4};
    private static int[] p6 = new int[]{0, 2};
    private static int[][] Floyd16x16 = new int[][]{{0, 128, 32, 160, 8, 136, 40, 168, 2, 130, 34, 162, 10, 138, 42, 170}, {192, 64, 224, 96, 200, 72, 232, 104, 194, 66, 226, 98, 202, 74, 234, 106}, {48, 176, 16, 144, 56, 184, 24, 152, 50, 178, 18, 146, 58, 186, 26, 154}, {240, 112, 208, 80, 248, 120, 216, 88, 242, 114, 210, 82, 250, 122, 218, 90}, {12, 140, 44, 172, 4, 132, 36, 164, 14, 142, 46, 174, 6, 134, 38, 166}, {204, 76, 236, 108, 196, 68, 228, 100, 206, 78, 238, 110, 198, 70, 230, 102}, {60, 188, 28, 156, 52, 180, 20, 148, 62, 190, 30, 158, 54, 182, 22, 150}, {252, 124, 220, 92, 244, 116, 212, 84, 254, 126, 222, 94, 246, 118, 214, 86}, {3, 131, 35, 163, 11, 139, 43, 171, 1, 129, 33, 161, 9, 137, 41, 169}, {195, 67, 227, 99, 203, 75, 235, 107, 193, 65, 225, 97, 201, 73, 233, 105}, {51, 179, 19, 147, 59, 187, 27, 155, 49, 177, 17, 145, 57, 185, 25, 153}, {243, 115, 211, 83, 251, 123, 219, 91, 241, 113, 209, 81, 249, 121, 217, 89}, {15, 143, 47, 175, 7, 135, 39, 167, 13, 141, 45, 173, 5, 133, 37, 165}, {207, 79, 239, 111, 199, 71, 231, 103, 205, 77, 237, 109, 197, 69, 229, 101}, {63, 191, 31, 159, 55, 183, 23, 151, 61, 189, 29, 157, 53, 181, 21, 149}, {254, 127, 223, 95, 247, 119, 215, 87, 253, 125, 221, 93, 245, 117, 213, 85}};
    private static int[][] Floyd8x8 = new int[][]{{0, 32, 8, 40, 2, 34, 10, 42}, {48, 16, 56, 24, 50, 18, 58, 26}, {12, 44, 4, 36, 14, 46, 6, 38}, {60, 28, 52, 20, 62, 30, 54, 22}, {3, 35, 11, 43, 1, 33, 9, 41}, {51, 19, 59, 27, 49, 17, 57, 25}, {15, 47, 7, 39, 13, 45, 5, 37}, {63, 31, 55, 23, 61, 29, 53, 21}};
    public static final int PAPER_58_WIDTH = 32;
    public static final int PAPER_80_WIDTH = 48;
    private static int sPaperWidth = 48;
    private static Integer[] theSet0 = new Integer[]{Integer.valueOf(1569), Integer.valueOf(1570), Integer.valueOf(1571), Integer.valueOf(1572), Integer.valueOf(1573), Integer.valueOf(1574), Integer.valueOf(1575), Integer.valueOf(1576), Integer.valueOf(1577), Integer.valueOf(1578), Integer.valueOf(1579), Integer.valueOf(1580), Integer.valueOf(1581), Integer.valueOf(1582), Integer.valueOf(1583), Integer.valueOf(1584), Integer.valueOf(1585), Integer.valueOf(1586), Integer.valueOf(1587), Integer.valueOf(1588), Integer.valueOf(1589), Integer.valueOf(1590), Integer.valueOf(1591), Integer.valueOf(1592), Integer.valueOf(1593), Integer.valueOf(1594), Integer.valueOf(1601), Integer.valueOf(1602), Integer.valueOf(1603), Integer.valueOf(1604), Integer.valueOf(1605), Integer.valueOf(1606), Integer.valueOf(1607), Integer.valueOf(1608), Integer.valueOf(1609), Integer.valueOf(1610), Integer.valueOf(17442), Integer.valueOf(17443), Integer.valueOf(17445), Integer.valueOf(17447)};
    private static Integer[][] FormatTable = new Integer[][]{{Integer.valueOf('ﺀ'), Integer.valueOf('ﺀ'), Integer.valueOf('ﺀ'), Integer.valueOf('ﺀ')}, {Integer.valueOf('ﺁ'), Integer.valueOf('ﺂ'), Integer.valueOf('ﺁ'), Integer.valueOf('ﺂ')}, {Integer.valueOf('ﺃ'), Integer.valueOf('ﺄ'), Integer.valueOf('ﺃ'), Integer.valueOf('ﺄ')}, {Integer.valueOf('ﺅ'), Integer.valueOf('ﺅ'), Integer.valueOf('ﺅ'), Integer.valueOf('ﺅ')}, {Integer.valueOf('ﹽ'), Integer.valueOf('ﹽ'), Integer.valueOf('ﹽ'), Integer.valueOf('ﹽ')}, {Integer.valueOf('ﺋ'), Integer.valueOf('ﺋ'), Integer.valueOf('ﺋ'), Integer.valueOf('ﺋ')}, {Integer.valueOf('ﺍ'), Integer.valueOf('ﺎ'), Integer.valueOf('ﺍ'), Integer.valueOf('ﺎ')}, {Integer.valueOf('ﺏ'), Integer.valueOf('ﺏ'), Integer.valueOf('ﺑ'), Integer.valueOf('ﺑ')}, {Integer.valueOf('ﺓ'), Integer.valueOf('ﺓ'), Integer.valueOf('ﺓ'), Integer.valueOf('ﺓ')}, {Integer.valueOf('ﺕ'), Integer.valueOf('ﺕ'), Integer.valueOf('ﺗ'), Integer.valueOf('ﺗ')}, {Integer.valueOf('ﺙ'), Integer.valueOf('ﺙ'), Integer.valueOf('ﺛ'), Integer.valueOf('ﺛ')}, {Integer.valueOf('ﺝ'), Integer.valueOf('ﺝ'), Integer.valueOf('ﺟ'), Integer.valueOf('ﺟ')}, {Integer.valueOf('ﺡ'), Integer.valueOf('ﺡ'), Integer.valueOf('ﺣ'), Integer.valueOf('ﺣ')}, {Integer.valueOf('ﺥ'), Integer.valueOf('ﺥ'), Integer.valueOf('ﺧ'), Integer.valueOf('ﺧ')}, {Integer.valueOf('ﺩ'), Integer.valueOf('ﺩ'), Integer.valueOf('ﺩ'), Integer.valueOf('ﺩ')}, {Integer.valueOf('ﺫ'), Integer.valueOf('ﺫ'), Integer.valueOf('ﺫ'), Integer.valueOf('ﺫ')}, {Integer.valueOf('ﺭ'), Integer.valueOf('ﺭ'), Integer.valueOf('ﺭ'), Integer.valueOf('ﺭ')}, {Integer.valueOf('ﺯ'), Integer.valueOf('ﺯ'), Integer.valueOf('ﺯ'), Integer.valueOf('ﺯ')}, {Integer.valueOf('ﺱ'), Integer.valueOf('ﺱ'), Integer.valueOf('ﺳ'), Integer.valueOf('ﺳ')}, {Integer.valueOf('ﺵ'), Integer.valueOf('ﺵ'), Integer.valueOf('ﺷ'), Integer.valueOf('ﺷ')}, {Integer.valueOf('ﺹ'), Integer.valueOf('ﺹ'), Integer.valueOf('ﺻ'), Integer.valueOf('ﺻ')}, {Integer.valueOf('ﺽ'), Integer.valueOf('ﺽ'), Integer.valueOf('ﺿ'), Integer.valueOf('ﺿ')}, {Integer.valueOf('ﻁ'), Integer.valueOf('ﻁ'), Integer.valueOf('ﻁ'), Integer.valueOf('ﻁ')}, {Integer.valueOf('ﻅ'), Integer.valueOf('ﻅ'), Integer.valueOf('ﻅ'), Integer.valueOf('ﻅ')}, {Integer.valueOf('ﻉ'), Integer.valueOf('ﻊ'), Integer.valueOf('ﻋ'), Integer.valueOf('ﻌ')}, {Integer.valueOf('ﻍ'), Integer.valueOf('ﻎ'), Integer.valueOf('ﻏ'), Integer.valueOf('ﻐ')}, {Integer.valueOf('ﻑ'), Integer.valueOf('ﻑ'), Integer.valueOf('ﻓ'), Integer.valueOf('ﻓ')}, {Integer.valueOf('ﻕ'), Integer.valueOf('ﻕ'), Integer.valueOf('ﻗ'), Integer.valueOf('ﻗ')}, {Integer.valueOf('ﻙ'), Integer.valueOf('ﻙ'), Integer.valueOf('ﻛ'), Integer.valueOf('ﻛ')}, {Integer.valueOf('ﻝ'), Integer.valueOf('ﻝ'), Integer.valueOf('ﻟ'), Integer.valueOf('ﻟ')}, {Integer.valueOf('ﻡ'), Integer.valueOf('ﻡ'), Integer.valueOf('ﻣ'), Integer.valueOf('ﻣ')}, {Integer.valueOf('ﻥ'), Integer.valueOf('ﻥ'), Integer.valueOf('ﻧ'), Integer.valueOf('ﻧ')}, {Integer.valueOf('ﻩ'), Integer.valueOf('ﻩ'), Integer.valueOf('ﻫ'), Integer.valueOf('ﻫ')}, {Integer.valueOf('ﻭ'), Integer.valueOf('ﻭ'), Integer.valueOf('ﻭ'), Integer.valueOf('ﻭ')}, {Integer.valueOf('ﻯ'), Integer.valueOf('ﻰ'), Integer.valueOf('ﻯ'), Integer.valueOf('ﻰ')}, {Integer.valueOf('ﻱ'), Integer.valueOf('ﻲ'), Integer.valueOf('ﻳ'), Integer.valueOf('ﻳ')}, {Integer.valueOf('ﻵ'), Integer.valueOf('ﻶ'), Integer.valueOf('ﻵ'), Integer.valueOf('ﻶ')}, {Integer.valueOf('ﻷ'), Integer.valueOf('ﻸ'), Integer.valueOf('ﻷ'), Integer.valueOf('ﻸ')}, {Integer.valueOf('ﻹ'), Integer.valueOf('ﻺ'), Integer.valueOf('ﻹ'), Integer.valueOf('ﻺ')}, {Integer.valueOf('ﻻ'), Integer.valueOf('ﻼ'), Integer.valueOf('ﻻ'), Integer.valueOf('ﻼ')}};
    static Integer[] theSet1 = new Integer[]{Integer.valueOf(1574), Integer.valueOf(1576), Integer.valueOf(1578), Integer.valueOf(1579), Integer.valueOf(1580), Integer.valueOf(1581), Integer.valueOf(1582), Integer.valueOf(1587), Integer.valueOf(1588), Integer.valueOf(1589), Integer.valueOf(1590), Integer.valueOf(1591), Integer.valueOf(1592), Integer.valueOf(1593), Integer.valueOf(1594), Integer.valueOf(1600), Integer.valueOf(1601), Integer.valueOf(1602), Integer.valueOf(1603), Integer.valueOf(1604), Integer.valueOf(1605), Integer.valueOf(1606), Integer.valueOf(1607), Integer.valueOf(1610)};
    static Integer[] theSet2 = new Integer[]{Integer.valueOf(1570), Integer.valueOf(1571), Integer.valueOf(1572), Integer.valueOf(1573), Integer.valueOf(1574), Integer.valueOf(1575), Integer.valueOf(1576), Integer.valueOf(1577), Integer.valueOf(1578), Integer.valueOf(1579), Integer.valueOf(1580), Integer.valueOf(1581), Integer.valueOf(1582), Integer.valueOf(1583), Integer.valueOf(1584), Integer.valueOf(1585), Integer.valueOf(1586), Integer.valueOf(1587), Integer.valueOf(1588), Integer.valueOf(1589), Integer.valueOf(1590), Integer.valueOf(1591), Integer.valueOf(1592), Integer.valueOf(1593), Integer.valueOf(1594), Integer.valueOf(1600), Integer.valueOf(1601), Integer.valueOf(1602), Integer.valueOf(1603), Integer.valueOf(1604), Integer.valueOf(1605), Integer.valueOf(1606), Integer.valueOf(1607), Integer.valueOf(1608), Integer.valueOf(1609), Integer.valueOf(1610)};
    public static final int ALGORITHM_DITHER_16x16 = 16;
    public static final int ALGORITHM_DITHER_8x8 = 8;
    public static final int ALGORITHM_TEXTMODE = 2;
    public static final int ALGORITHM_GRAYTEXTMODE = 1;
    public static final int[][] COLOR_PALETTE = new int[][]{new int[3], {255, 255, 255}};
    public static final int FLOYD_STEINBERG_DITHER = 1;
    private static int method = 1;
    public static final int ATKINSON_DITHER = 2;

    public GpUtils() {
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = (float) w / (float) width;
        float scaleHeight = (float) h / (float) height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    public static void saveMyBitmap(Bitmap mBitmap) {
        File f = new File(Environment.getExternalStorageDirectory().getPath(), "Btatotest.jpeg");

        try {
            f.createNewFile();
        } catch (IOException var6) {
            ;
        }

        FileOutputStream fOut = null;

        try {
            fOut = new FileOutputStream(f);
            mBitmap.compress(CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException var4) {
            ;
        } catch (IOException var5) {
            ;
        }

    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int height = bmpOriginal.getHeight();
        int width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0F);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0.0F, 0.0F, paint);
        return bmpGrayscale;
    }

    static byte[] pixToEscRastBitImageCmd(byte[] src, int nWidth, int nMode) {
        int nHeight = src.length / nWidth;
        byte[] data = new byte[8 + src.length / 8];
        data[0] = 29;
        data[1] = 118;
        data[2] = 48;
        data[3] = (byte) (nMode & 1);
        data[4] = (byte) (nWidth / 8 % 256);
        data[5] = (byte) (nWidth / 8 / 256);
        data[6] = (byte) (nHeight % 256);
        data[7] = (byte) (nHeight / 256);
        int i = 8;

        for (int k = 0; i < data.length; ++i) {
            data[i] = (byte) (p0[src[k]] + p1[src[k + 1]] + p2[src[k + 2]] + p3[src[k + 3]] + p4[src[k + 4]] + p5[src[k + 5]] + p6[src[k + 6]] + src[k + 7]);
            k += 8;
        }

        return data;
    }

    public static byte[] pixToEscRastBitImageCmd(byte[] src) {
        byte[] data = new byte[src.length / 8];
        int i = 0;

        for (int k = 0; i < data.length; ++i) {
            data[i] = (byte) (p0[src[k]] + p1[src[k + 1]] + p2[src[k + 2]] + p3[src[k + 3]] + p4[src[k + 4]] + p5[src[k + 5]] + p6[src[k + 6]] + src[k + 7]);
            k += 8;
        }

        return data;
    }

    static byte[] pixToEscNvBitImageCmd(byte[] src, int width, int height) {
        byte[] data = new byte[src.length / 8 + 4];
        data[0] = (byte) (width / 8 % 256);
        data[1] = (byte) (width / 8 / 256);
        data[2] = (byte) (height / 8 % 256);
        data[3] = (byte) (height / 8 / 256);

        for (int i = 0; i < width; ++i) {
            int k = 0;

            for (int j = 0; j < height / 8; ++j) {
                data[4 + j + i * height / 8] = (byte) (p0[src[i + k]] + p1[src[i + k + 1 * width]] + p2[src[i + k + 2 * width]] + p3[src[i + k + 3 * width]] + p4[src[i + k + 4 * width]] + p5[src[i + k + 5 * width]] + p6[src[i + k + 6 * width]] + src[i + k + 7 * width]);
                k += 8 * width;
            }
        }

        return data;
    }

    public static byte[] pixToLabelCmd(byte[] src) {
        byte[] data = new byte[src.length / 8];
        int k = 0;

        for (int j = 0; k < data.length; ++k) {
            byte temp = (byte) (p0[src[j]] + p1[src[j + 1]] + p2[src[j + 2]] + p3[src[j + 3]] + p4[src[j + 4]] + p5[src[j + 5]] + p6[src[j + 6]] + src[j + 7]);
            data[k] = (byte) (~temp);
            j += 8;
        }

        return data;
    }

    public static byte[] pixToTscCmd(int x, int y, int mode, byte[] src, int nWidth) {
        int height = src.length / nWidth;
        int width = nWidth / 8;
        String str = "BITMAP " + x + "," + y + "," + width + "," + height + "," + mode + ",";
        byte[] bitmap = null;

        try {
            bitmap = str.getBytes("GB2312");
        } catch (UnsupportedEncodingException var13) {
            var13.printStackTrace();
        }

        byte[] arrayOfByte = new byte[src.length / 8];
        int k = 0;

        for (int j = 0; k < arrayOfByte.length; ++k) {
            byte temp = (byte) (p0[src[j]] + p1[src[j + 1]] + p2[src[j + 2]] + p3[src[j + 3]] + p4[src[j + 4]] + p5[src[j + 5]] + p6[src[j + 6]] + src[j + 7]);
            arrayOfByte[k] = (byte) (~temp);
            j += 8;
        }

        byte[] data = new byte[bitmap.length + arrayOfByte.length];
        System.arraycopy(bitmap, 0, data, 0, bitmap.length);
        System.arraycopy(arrayOfByte, 0, data, bitmap.length, arrayOfByte.length);
        return data;
    }

    private static void format_K_dither16x16(int[] orgpixels, int xsize, int ysize, byte[] despixels) {
        int k = 0;

        for (int y = 0; y < ysize; ++y) {
            for (int x = 0; x < xsize; ++x) {
                if ((orgpixels[k] & 255) > Floyd16x16[x & 15][y & 15]) {
                    despixels[k] = 0;
                } else {
                    despixels[k] = 1;
                }

                ++k;
            }
        }

    }

    public static byte[] bitmapToBWPix(Bitmap mBitmap) {
        int[] pixels = new int[mBitmap.getWidth() * mBitmap.getHeight()];
        byte[] data = new byte[mBitmap.getWidth() * mBitmap.getHeight()];
        Bitmap grayBitmap = toGrayscale(mBitmap);
        grayBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        format_K_dither16x16(pixels, grayBitmap.getWidth(), grayBitmap.getHeight(), data);
        return data;
    }

    private static void format_K_dither16x16_int(int[] orgpixels, int xsize, int ysize, int[] despixels) {
        int k = 0;

        for (int y = 0; y < ysize; ++y) {
            for (int x = 0; x < xsize; ++x) {
                if ((orgpixels[k] & 255) > Floyd16x16[x & 15][y & 15]) {
                    despixels[k] = -1;
                } else {
                    despixels[k] = -16777216;
                }

                ++k;
            }
        }

    }

    private static void format_K_dither8x8_int(int[] orgpixels, int xsize, int ysize, int[] despixels) {
        int k = 0;

        for (int y = 0; y < ysize; ++y) {
            for (int x = 0; x < xsize; ++x) {
                if ((orgpixels[k] & 255) >> 2 > Floyd8x8[x & 7][y & 7]) {
                    despixels[k] = -1;
                } else {
                    despixels[k] = -16777216;
                }

                ++k;
            }
        }

    }

    public static int[] bitmapToBWPix_int(Bitmap mBitmap, int algorithm) {
        int[] pixels = new int[0];
        Bitmap grayBitmap;
        switch (algorithm) {
            case 2:
                break;
            case 8:
                grayBitmap = toGrayscale(mBitmap);
                pixels = new int[grayBitmap.getWidth() * grayBitmap.getHeight()];
                grayBitmap.getPixels(pixels, 0, grayBitmap.getWidth(), 0, 0, grayBitmap.getWidth(), grayBitmap.getHeight());
                format_K_dither8x8_int(pixels, grayBitmap.getWidth(), grayBitmap.getHeight(), pixels);
                break;
            case 16:
            default:
                grayBitmap = toGrayscale(mBitmap);
                pixels = new int[grayBitmap.getWidth() * grayBitmap.getHeight()];
                grayBitmap.getPixels(pixels, 0, grayBitmap.getWidth(), 0, 0, grayBitmap.getWidth(), grayBitmap.getHeight());
                format_K_dither16x16_int(pixels, grayBitmap.getWidth(), grayBitmap.getHeight(), pixels);
        }

        return pixels;
    }

    public static Bitmap toBinaryImage(Bitmap mBitmap, int nWidth, int algorithm) {
        int width = (nWidth + 7) / 8 * 8;
        int height = mBitmap.getHeight() * width / mBitmap.getWidth();
        Bitmap rszBitmap = resizeImage(mBitmap, width, height);
        int[] pixels = bitmapToBWPix_int(rszBitmap, algorithm);
        rszBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return rszBitmap;
    }

    private static int getCloseColor(int tr, int tg, int tb) {
        int minDistanceSquared = 195076;
        int bestIndex = 0;

        for (int i = 0; i < COLOR_PALETTE.length; ++i) {
            int rdiff = tr - COLOR_PALETTE[i][0];
            int gdiff = tg - COLOR_PALETTE[i][1];
            int bdiff = tb - COLOR_PALETTE[i][2];
            int distanceSquared = rdiff * rdiff + gdiff * gdiff + bdiff * bdiff;
            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared;
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    private static void setPixel(int[] input, int width, int height, int col, int row, int[] p) {
        if (col < 0 || col >= width) {
            col = 0;
        }

        if (row < 0 || row >= height) {
            row = 0;
        }

        int index = row * width + col;
        input[index] = -16777216 | clamp(p[0]) << 16 | clamp(p[1]) << 8 | clamp(p[2]);
    }

    private static int[] getPixel(int[] input, int width, int height, int col, int row, float error, int[] ergb) {
        if (col < 0 || col >= width) {
            col = 0;
        }

        if (row < 0 || row >= height) {
            row = 0;
        }

        int index = row * width + col;
        int tr = input[index] >> 16 & 255;
        int tg = input[index] >> 8 & 255;
        int tb = input[index] & 255;
        tr = (int) ((float) tr + error * (float) ergb[0]);
        tg = (int) ((float) tg + error * (float) ergb[1]);
        tb = (int) ((float) tb + error * (float) ergb[2]);
        return new int[]{tr, tg, tb};
    }

    public static int clamp(int value) {
        return value > 255 ? 255 : (value < 0 ? 0 : value);
    }

    public static Bitmap filter(Bitmap nbm, int width, int height) {
        int[] inPixels = new int[width * height];
        nbm.getPixels(inPixels, 0, width, 0, 0, width, height);
        int[] outPixels = new int[inPixels.length];

        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                int index = row * width + col;
                int r1 = inPixels[index] >> 16 & 255;
                int g1 = inPixels[index] >> 8 & 255;
                int b1 = inPixels[index] & 255;
                int cIndex = getCloseColor(r1, g1, b1);
                outPixels[index] = -16777216 | COLOR_PALETTE[cIndex][0] << 16 | COLOR_PALETTE[cIndex][1] << 8 | COLOR_PALETTE[cIndex][2];
                int[] ergb = new int[]{r1 - COLOR_PALETTE[cIndex][0], g1 - COLOR_PALETTE[cIndex][1], b1 - COLOR_PALETTE[cIndex][2]};
                float e1;
                int[] rgb4;
                int[] rgb5;
                int[] rgb6;
                if (method == 1) {
                    e1 = 0.4375F;
                    float e2 = 0.3125F;
                    float e3 = 0.1875F;
                    float e4 = 0.0625F;
                    rgb4 = getPixel(inPixels, width, height, col + 1, row, e1, ergb);
                    rgb5 = getPixel(inPixels, width, height, col, row + 1, e2, ergb);
                    rgb6 = getPixel(inPixels, width, height, col - 1, row + 1, e3, ergb);
                    int[] rgb41 = getPixel(inPixels, width, height, col + 1, row + 1, e4, ergb);
                    setPixel(inPixels, width, height, col + 1, row, rgb4);
                    setPixel(inPixels, width, height, col, row + 1, rgb5);
                    setPixel(inPixels, width, height, col - 1, row + 1, rgb6);
                    setPixel(inPixels, width, height, col + 1, row + 1, rgb41);
                } else {
                    if (method != 2) {
                        throw new IllegalArgumentException("Not Supported Dither Mothed!!");
                    }

                    e1 = 0.125F;
                    int[] rgb1 = getPixel(inPixels, width, height, col + 1, row, e1, ergb);
                    int[] rgb2 = getPixel(inPixels, width, height, col + 2, row, e1, ergb);
                    int[] rgb3 = getPixel(inPixels, width, height, col - 1, row + 1, e1, ergb);
                    rgb4 = getPixel(inPixels, width, height, col, row + 1, e1, ergb);
                    rgb5 = getPixel(inPixels, width, height, col + 1, row + 1, e1, ergb);
                    rgb6 = getPixel(inPixels, width, height, col, row + 2, e1, ergb);
                    setPixel(inPixels, width, height, col + 1, row, rgb1);
                    setPixel(inPixels, width, height, col + 2, row, rgb2);
                    setPixel(inPixels, width, height, col - 1, row + 1, rgb3);
                    setPixel(inPixels, width, height, col, row + 1, rgb4);
                    setPixel(inPixels, width, height, col + 1, row + 1, rgb5);
                    setPixel(inPixels, width, height, col, row + 2, rgb6);
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(outPixels, 0, width, width, height, Config.RGB_565);
        return bitmap;
    }

    static String splitArabic(String input) {
        StringBuilder sb = new StringBuilder(256);
        String[] arabics = input.split("\\n");
        int i;
        int lastArabic;
        if (arabics.length == 1 && arabics[0].length() > sPaperWidth) {
            i = arabics[0].length() / sPaperWidth;
            lastArabic = 1;

            for (int j = 0; lastArabic <= i; ++lastArabic) {
                sb.append(arabics[0].substring(j, sPaperWidth * lastArabic));
                j += sPaperWidth;
            }

            if (sb.length() >= 0) {
                sb.append('\n');
            }

            lastArabic = arabics[0].length() % sPaperWidth;
            sb.append(arabics[0].substring(arabics[0].length() - lastArabic, arabics[0].length()));
            return splitArabic(sb.toString());
        } else {
            for (i = 0; i < arabics.length; ++i) {
                lastArabic = arabics[i].length();
                if (lastArabic > sPaperWidth) {
                    sb.append(splitArabic(arabics[i]));
                } else {
                    sb.append(addSpaceAfterArabicString(arabics[i], sPaperWidth - lastArabic));
                }
            }

            return sb.toString();
        }
    }

    static String addSpaceAfterArabicString(String arabic, int number) {
        StringBuilder sb = new StringBuilder(65);
        sb.append(arabic);

        for (int i = 0; i < number; ++i) {
            sb.append(' ');
        }

        sb.append('\n');
        return sb.toString();
    }

    static String reverseLetterAndNumber(String input) {
        StringBuilder sb = new StringBuilder(input);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String matcherString = matcher.group();
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();
            sb.replace(matcherStart, matcherEnd, (new StringBuilder(matcherString)).reverse().toString());
        }

        return sb.toString();
    }

    static byte[] string2Cp864(String arabicString) {
        Integer[] originUnicode = new Integer[arabicString.length()];
        Integer[] outputUnicode = new Integer[arabicString.length()];
        Integer[] outputChars = new Integer[originUnicode.length];
        copy(arabicString.toCharArray(), originUnicode, arabicString.length());
        List<Integer> list = new ArrayList(Arrays.asList(originUnicode));
        List<Integer> list1 = Hyphen(list);
        list = Deformation(list1);
        Collections.reverse(list);
        list.toArray(outputUnicode);
        char[] chs = integer2Character(outputUnicode);
        byte[] cp864bytes = new byte[0];

        try {
            cp864bytes = (new String(chs)).getBytes("cp864");
        } catch (UnsupportedEncodingException var8) {
            var8.printStackTrace();
        }

        return cp864bytes;
    }

    static char[] integer2Character(Integer[] integers) {
        char[] chs = new char[integers.length];

        for (int i = 0; i < integers.length; ++i) {
            if (integers[i] != null) {
                chs[i] = (char) integers[i].intValue();
            } else {
                chs[i] = 32;
            }
        }

        return chs;
    }

    static void copy(char[] array, Integer[] originUnicode, int length) {
        for (int i = 0; i < length; ++i) {
            originUnicode[i] = Integer.valueOf(array[i]);
        }

    }

    static List<Integer> Hyphen(List<Integer> list) {
        for (int i = 0; i < list.size(); ++i) {
            if (((Integer) list.get(i)).intValue() == 1604) {
                switch (((Integer) list.get(i + 1)).intValue()) {
                    case 1570:
                        list.set(i, Integer.valueOf(17442));
                        list.remove(i + 1);
                        break;
                    case 1571:
                        list.set(i, Integer.valueOf(17443));
                        list.remove(i + 1);
                    case 1572:
                    case 1574:
                    default:
                        break;
                    case 1573:
                        list.set(i, Integer.valueOf(17445));
                        list.remove(i + 1);
                        break;
                    case 1575:
                        list.set(i, Integer.valueOf(17447));
                        list.remove(i + 1);
                }
            }
        }

        return list;
    }

    static List<Integer> Deformation(List<Integer> inputlist) {
        List<Integer> outputlist = new ArrayList();
        Map<Integer, Integer[]> formHashTable = new HashMap(40);

        int i;
        for (i = 0; i < 40; ++i) {
            formHashTable.put(theSet0[i], FormatTable[i]);
        }

        for (i = 0; i < inputlist.size(); ++i) {
            if (compare((Integer) inputlist.get(i), 0)) {
                boolean inSet1;
                boolean inSet2;
                int flag;
                if (i == 0) {
                    inSet1 = false;
                    inSet2 = compare((Integer) inputlist.get(i + 1), 2);
                    flag = Flag(inSet1, inSet2);
                } else if (i == inputlist.size() - 1) {
                    inSet1 = compare((Integer) inputlist.get(i - 1), 1);
                    inSet2 = false;
                    flag = Flag(inSet1, inSet2);
                } else {
                    inSet1 = compare((Integer) inputlist.get(i - 1), 1);
                    inSet2 = compare((Integer) inputlist.get(i + 1), 2);
                    flag = Flag(inSet1, inSet2);
                }

                Integer[] a = (Integer[]) formHashTable.get(inputlist.get(i));
                outputlist.add(a[flag]);
            } else {
                outputlist.add((Integer) inputlist.get(i));
            }
        }

        return outputlist;
    }

    static boolean compare(Integer input, int i) {
        List<Integer[]> list = new ArrayList(3);
        list.add(theSet0);
        list.add(theSet1);
        list.add(theSet2);
        return findInArray((Integer[]) list.get(i), input.intValue());
    }

    static boolean findInArray(Integer[] integer, int input) {
        for (int j = 0; j < integer.length; ++j) {
            if (integer[j].intValue() == input) {
                return true;
            }
        }

        return false;
    }

    static int Flag(boolean set1, boolean set2) {
        return set1 && set2 ? 3 : (!set1 && set2 ? 2 : (set1 && !set2 ? 1 : 0));
    }

    public static void setPaperWidth(int paperWidth) {
        sPaperWidth = paperWidth;
    }

    public static byte[] ByteTo_byte(Vector<Byte> vector) {
        int len = vector.size();
        byte[] data = new byte[len];

        for (int i = 0; i < len; ++i) {
            data[i] = ((Byte) vector.get(i)).byteValue();
        }

        return data;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        method = method;
    }
}
