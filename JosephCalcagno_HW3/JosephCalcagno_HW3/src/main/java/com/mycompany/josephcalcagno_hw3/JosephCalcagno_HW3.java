package com.mycompany.josephcalcagno_hw3;

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
public class JosephCalcagno_HW3 {

    public static void main(String[] args) {
        guassianFilter(); // Question 1
        lapFilter(); // Question 2 

    }

    public static void guassianFilter() {
        try {

            // load in image 
            File file1 = new File("lenna-noise.tif");
            BufferedImage image1 = ImageIO.read(file1);
            BufferedImage result = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);

            // Generating the Kernel
            int size = 5;

            double[][] kernelMatrix = new double[size][size];

            double sigma = 2.0;

            double k = 1.67;

            // Kernel formula 
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int s = i - size / 2;
                    int t = j - size / 2;

                    // exp(s.^2+t.^2) / (2*sigma^2)*(-1))*K
                    kernelMatrix[i][j] = Math.exp((Math.pow(s, 2) + Math.pow(t, 2)) / (2 * Math.pow(sigma, 2)) * (-1) * k);

                }
            }

            for (int x = 0; x < 512; x++) {
                for (int y = 0; y < 512; y++) {

                    int intensity = applyGaussianKernel(image1, x, y, kernelMatrix);

                    Color color = new Color(intensity, intensity, intensity);

                    result.setRGB(x, y, color.getRGB());

                }
            }

            ImageIO.write(result, "tif", new File("lena-guassian.tif"));
        } catch (IOException e) {

            System.out.println("Did not work");

        }

    }

    public static int applyGaussianKernel(BufferedImage image, int x, int y, double[][] kernel) {

        int sizeofKernel = kernel.length;

        double sum = 0;

        double allWeight = 0;

        int radius = sizeofKernel / 2;

        Raster raster1 = image.getData();

        // Boundary Checkign
        for (int i = 0; i < sizeofKernel; i++) {
            for (int j = 0; j < sizeofKernel; j++) {

                int xPos = x + (i - radius);
                int yPos = y + (j - radius);

                // Boundaries 
                if (xPos >= 0 && xPos < 512 && yPos >= 0 && yPos < 512) {

                    int intensity = raster1.getSample(xPos, yPos, 0);
                    double weight = kernel[i][j];
                    sum += intensity * weight;
                    allWeight += weight;
                }
            }
        }

        // Normalize
        sum /= allWeight;

        int result = (int) Math.min(Math.max(sum, 0), 255);

        return result;
    }

    public static void lapFilter() {
        try {
            File file1 = new File("lena.tif");
            BufferedImage image1 = ImageIO.read(file1);

            BufferedImage finalImage = lapImage(image1);
            BufferedImage outputImage = finalLap(image1, finalImage, -1);

            ImageIO.write(outputImage, "tif", new File("lena-sharpen.tif"));

        } catch (IOException e) {
            System.out.println("Did not work");
        }
    }

    public static BufferedImage lapImage(BufferedImage image) {
        // Matrix
        int[][] matrix = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};
        int kernelSize = matrix.length;

        Raster raster1 = image.getData();

        BufferedImage result = new BufferedImage(512, 512, BufferedImage.TYPE_BYTE_GRAY);

        // Loops the matrix over each pixel
        for (int x = 0; x < 512; x++) {
            for (int y = 0; y < 512; y++) {
                int sum = 0;

                // Neighbor operations on pixel
                for (int i = 0; i < kernelSize; i++) {
                    for (int j = 0; j < kernelSize; j++) {
                        int xPos = x + j - 1;
                        int yPos = y + i - 1;

                        // Checking boundaries
                        if (xPos >= 0 && xPos < 512 && yPos >= 0 && yPos < 512) {

                            // Sum added for neighbors 
                            sum += (raster1.getSample(xPos, yPos, 0) * matrix[i][j]);
                        }
                    }
                }
                int newValue = Math.min(Math.max(sum, 0), 255);
                Color color = new Color(newValue, newValue, newValue);
                result.setRGB(x, y, color.getRGB());

            }
        }
        return result;
    }

    public static BufferedImage finalLap(BufferedImage image1, BufferedImage image2, double weight) {
        int width = 512;
        int height = 512;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        Raster raster1 = image1.getData();
        Raster raster2 = image2.getData();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int firstImagePixel = raster1.getSample(x, y, 0);

                int secondImagePixel = raster2.getSample(x, y, 0);

                // adding weighted value
                int newValue = (int) (firstImagePixel + weight * secondImagePixel);

                newValue = Math.min(Math.max(newValue, 0), 255);

                Color color = new Color(newValue, newValue, newValue);
                result.setRGB(x, y, color.getRGB());
            }
        }
        return result;
    }

}
