/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.josephcalcagno_hw5;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author josephcalcagno
 */
public class JosephCalcagno_HW5 {

    public static void main(String[] args) {
        lincoln_boundary(); // Question 1
        opening(); // Question 2
        closing(); // Question 3

    }

    public static int[][] erosion(int width, int height, Raster raster1, int[][] imageArray, int[][] newImageArray) {

        int[][] structuringElement = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        // Converting 255 to 1
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imageArray[i][j] = raster1.getSample(i, j, 0);

                if (imageArray[i][j] == 255) {
                    imageArray[i][j] = 1;
                }

            }
        }

        // Performing erosion operatioon
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int min = 1;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        if (structuringElement[m + 1][n + 1] == 1) {
                            min = Math.min(min, imageArray[i + m][j + n]);
                        }
                    }
                }
                int checker = Math.max(0, min);
                checker = checker * 255;

                newImageArray[i][j] = checker;

            }
        }

        return newImageArray;
    }

    public static void lincoln_boundary() {

        try {

            // Setting up image input and output
            int width = 221;
            int height = 269;
            File file1 = new File("lincoln.tif");
            BufferedImage image1 = ImageIO.read(file1);
            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int[][] imageArray = new int[width][height];
            int[][] newImageArray = new int[width][height];
            Raster raster1 = image1.getData();

            newImageArray = erosion(width, height, raster1, imageArray, newImageArray);
            // Now for subtraction
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    newImageArray[i][j] = (imageArray[i][j] * 255) - newImageArray[i][j];
                    int checker2 = Math.max(0, newImageArray[i][j]);
                    Color color = new Color(checker2, checker2, checker2);
                    result.setRGB(i, j, color.getRGB());
                }
            }

            ImageIO.write(result, "tif", new File("lincoln_boundary.tif"));

        } catch (IOException e) {
            System.out.println("Did not work");
        }
    }

    public static void opening() {
        try {

            // Setting up image input and output
            int width = 315;
            int height = 238;
            File file1 = new File("fingerprint.tif");
            BufferedImage image1 = ImageIO.read(file1);
            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int[][] imageArray = new int[width][height];
            int[][] newImageArray = new int[width][height];
            Raster raster1 = image1.getData();

            newImageArray = erosion(width, height, raster1, imageArray, newImageArray);

            int[][] structuringElement = {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
            };

            // Now for Dilation
            for (int i = 1; i < width - 1; i++) {
                for (int j = 1; j < height - 1; j++) {
                    int max = 0;
                    for (int m = -1; m <= 1; m++) {
                        for (int n = -1; n <= 1; n++) {
                            if (structuringElement[m + 1][n + 1] == 1) {
                                max = Math.max(max, newImageArray[i + m][j + n]);
                            }
                        }
                    }
                    int checker = Math.min(1, max);
                    checker = checker * 255;

                    result.setRGB(i, j, new Color(checker, checker, checker).getRGB());
                }
            }

            ImageIO.write(result, "tif", new File("fingerprint_opening.tif"));

        } catch (IOException e) {
            System.out.println("Did not work");
        }
    }

    public static void closing() {
    try {
        // Setting up image input and output
        int width = 315;
        int height = 238;
        File file1 = new File("fingerprint.tif");
        BufferedImage image1 = ImageIO.read(file1);
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[][] imageArray = new int[width][height];
        int[][] newImageArray = new int[width][height];
        Raster raster1 = image1.getData();
        
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++) {
                imageArray[i][j] = raster1.getSample(i, j, 0);
            }
        }

        // Define structuring element for dilation
        int[][] structuringElement = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };

        // Perform dilation first
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int max = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        if (structuringElement[m + 1][n + 1] == 1) {
                            max = Math.max(max, imageArray[i + m][j + n]);
                        }
                    }
                }
                int checker = Math.min(1, max);
                checker = checker * 255;

                newImageArray[i][j] = checker;
            }
        }

        // Perform erosion
        newImageArray = erosion(width, height, raster1, imageArray, newImageArray);

        // Set result image
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
               
                result.setRGB(i, j, new Color(newImageArray[i][j], newImageArray[i][j], newImageArray[i][j]).getRGB());
            }
        }

        ImageIO.write(result, "tif", new File("fingerprint_closing.tif"));

    } catch (IOException e) {
        System.out.println("Did not work");
    }
}

}
