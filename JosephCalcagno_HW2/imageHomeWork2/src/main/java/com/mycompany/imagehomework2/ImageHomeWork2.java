/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.imagehomework2;

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
public class ImageHomeWork2 {

    public static void main(String[] args) {
        try {

            BufferedImage image = ImageIO.read(new File("lena.tif"));

            // Perform histogram equalization
            BufferedImage equalizedImage = histogramEqualization(image);

            // Save the equalized image
            File output = new File("lena_histequal.tif");
            ImageIO.write(equalizedImage, "TIFF", output);

        } catch (IOException e) {

            System.out.println("Did not Work");
        }

    }

    public static BufferedImage histogramEqualization(BufferedImage image) {

        // initial image setup
        int width = 512;
        int height = 512;
        int[] histogram = new int[256];
        int[][] data = new int[width][height];
        int intensityLevel = 0;
        Raster raster = image.getData();

        // Get original histogram
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                intensityLevel = raster.getSample(i, j, 0);
                data[i][j] = intensityLevel;
                histogram[intensityLevel]++;

            }

        }

        // Get PDF value 
        double[] PDF = new double[256];

        double count = 0.0;
        for (int i = 0; i < PDF.length; i++) {
            PDF[i] = ((double) histogram[i] / 262144);
            count = count + PDF[i];
        }

        // Get cdf
        double[] CDF = new double[256];
        CDF[0] = PDF[0];
        for (int i = 1; i < PDF.length; i++) {
            CDF[i] = PDF[i] + CDF[i - 1];
        }

        // Do CDF * 255
        double[] times7 = new double[256];
        for (int i = 0; i < CDF.length; i++) {
            times7[i] = Math.round(CDF[i] * 255);
        }

        // Create new Image 
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[][] newPixelValues = new int[512][512];

        // Set new image values and generate new image 
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int oldPixelValue = data[i][j];
                int newPixelValue = (int) times7[oldPixelValue];
                newPixelValues[i][j] = newPixelValue;
                Color color = new Color(newPixelValues[i][j], newPixelValues[i][j], newPixelValues[i][j]);

                newImage.setRGB(i, j, color.getRGB());
            }
        }

        return newImage;
    }
}
