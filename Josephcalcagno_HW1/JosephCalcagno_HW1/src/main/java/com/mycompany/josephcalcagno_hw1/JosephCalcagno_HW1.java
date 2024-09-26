/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.josephcalcagno_hw1;

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
public class JosephCalcagno_HW1 {

    public static void main(String[] args) throws IOException {
        // AS PER INSTRUCTORS DIRECTIONS 
        // all libraries are used to read in and output images.
        // No external libraries were used to perform any operations on images.
        // Programmed in Apache Netbeans 16

        imageSubtraction(); // Problem 1
        negativeImage();    // Problem 2
        translation();      // Problem 3

    }

    public static void imageSubtraction() {
        try {
            int width = 512;
            int height = 512;

            // Get file paths and read images
            File file1 = new File("WashingtonDC-ver1.tif");
            File file2 = new File("WashingtonDC-ver2.tif");

            BufferedImage image1 = ImageIO.read(file1);
            BufferedImage image2 = ImageIO.read(file2);
            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Array for new image
            int[][] newImageArray = new int[width][height]; // d(x,y)

            // arrays for iamge pixel values
            int[][] image1Data = new int[width][height]; // g(x,y)
            int[][] image2Data = new int[width][height]; // f(x,y)

            // Using raster to get image data
            Raster raster1 = image1.getData();
            Raster raster2 = image2.getData();

            // double loop to perform subtraciton
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    image1Data[i][j] = raster1.getSample(i, j, 0);
                    image2Data[i][j] = raster2.getSample(i, j, 0);
                    newImageArray[i][j] = (image2Data[i][j] - image1Data[i][j]); // d(x,y) = f(x,y) - g(x,y)

                    int checker = Math.max(0, newImageArray[i][j]); // normalization g of m = g - min(g)

                    Color color = new Color(checker, checker, checker); // helping output the right pixel value
                    result.setRGB(i, j, color.getRGB());    // setting new pixel values

                }
            }

            ImageIO.write(result, "tif", new File("WashingtonDC_subtract.tif"));

        } catch (IOException e) {
            System.out.println("Did not work");
        }

    }

    public static void negativeImage() {
        try {
            int width = 512;
            int height = 512;

            File file1 = new File("lena.tif");

            BufferedImage image1 = ImageIO.read(file1);
            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int[][] newImageArray = new int[width][height]; // for new pixel values
            int[][] image1Data = new int[width][height];    // for current pixel values

            Raster raster1 = image1.getData(); // getting correct current pixel values

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    image1Data[i][j] = raster1.getSample(i, j, 0);
                    newImageArray[i][j] = (255 - image1Data[i][j]); // making image negative 

                    Color color = new Color(newImageArray[i][j], newImageArray[i][j], newImageArray[i][j]); // setting correct pixel values
                    result.setRGB(i, j, color.getRGB());

                }
            }
            ImageIO.write(result, "tif", new File("lena_negative.tif"));

        } catch (IOException e) {
            System.out.println("Did not work");
        }
    }

    public static void translation() {
        try {

            int width = 512;
            int height = 512;

            File file1 = new File("lena.tif");

            BufferedImage image1 = ImageIO.read(file1);
            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int[][] newImageArray = new int[width][height];

            int[][] image1Data = new int[width][height];

            Raster raster1 = image1.getData();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    int x = i + 5;
                    int y = j + 5;

                    if ((x >= 0 && x < width) && (y >= 0 && y < height)) { // making sure pixel is in bounds 

                        image1Data[i][j] = raster1.getSample(i, j, 0);
                        newImageArray[x][y] = image1Data[i][j];

                        Color color = new Color(newImageArray[x][y], newImageArray[x][y], newImageArray[x][y]);
                        result.setRGB(x, y, color.getRGB());
                    }
                }
            }

            ImageIO.write(result, "tif", new File("lena_translation.tif"));

        } catch (IOException e) {
            System.out.println("Did not work");

        }

    }

}
