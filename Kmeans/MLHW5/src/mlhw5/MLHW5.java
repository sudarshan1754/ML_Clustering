package mlhw5;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

/**
 *
 * @author Sudarshan
 */
public class MLHW5 {

    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length < 3) {
            System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
            return;
        }
        try {
            BufferedImage originalImage = ImageIO.read(new File(args[0]));
            int k = Integer.parseInt(args[1]);
            BufferedImage kmeansJpg = kmeans_helper(originalImage, k);
            ImageIO.write(kmeansJpg, "jpg", new File(args[2]));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k) {
        int w = originalImage.getWidth();
        int h = originalImage.getHeight();
        BufferedImage kmeansImage = new BufferedImage(w, h, originalImage.getType());
        Graphics2D g = kmeansImage.createGraphics();
        g.drawImage(originalImage, 0, 0, w, h, null);
        // Read rgb values from the image
        int[] rgb = new int[w * h];
        int count = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                rgb[count++] = kmeansImage.getRGB(i, j);
            }
        }
        // Call kmeans algorithm: update the rgb values
        kmeans(rgb, k);

        // Write the new rgb values to the image
        count = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                kmeansImage.setRGB(i, j, rgb[count++]);
            }
        }
        return kmeansImage;
    }

    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int K) {

        
        int[] c_k = new int[K];
        Random randomGenerator = new Random();
        for (int i = 0; i < K; i++) {
            //int si = randomGenerator.nextInt(555555) - 882555682; //Random Number which is negative
            //c_k[i] = si;
            int si = randomGenerator.nextInt(rgb.length);
            c_k[i] = rgb[si];
            System.out.println("Assignment of k" + (i+1) + ":" + c_k[i]);
            
            //System.out.println(c_k[i]);
        }
//Starts here
        double[] old_distance = new double[rgb.length];
        int[] ai = new int[rgb.length];

        for (int xi = 0; xi < rgb.length; xi++) {
            double[] distance_for_centroid = new double[K];
            for (int k = 0; k < K; k++) {
                distance_for_centroid[k] = Math.pow((Math.abs(rgb[xi]) - Math.abs(c_k[k])), 2);

            }

            double MinVal = distance_for_centroid[0];
            int cluster = 0;

            for (int i = 1; i < distance_for_centroid.length; i++) {
                if (distance_for_centroid[i] < MinVal) {
                    MinVal = distance_for_centroid[i];
                    cluster = i;
                }
            }
            old_distance[xi] = MinVal;
            ai[xi] = cluster;
            //System.out.println(ai[xi]);
        }

        for (int s = 0; s < rgb.length; s++) {
            rgb[s] = c_k[ai[s]];
        }

        //Mean Calcluation
        for (int m = 0; m < K; m++) {
            int sum = 0;
            int c = 1;
            for (int i = 0; i < rgb.length; i++) {
                if (ai[i] == m) {
                    sum = sum + rgb[ai[i]];
                    c++;
                }
            }
            c_k[m] = sum / c;
            //System.out.println(c_k[m]);
        }
        System.out.println("Iteration " + 1 + " Done!");
        //2nd iteration

        
        for (int iter = 2; iter < 500; iter++) {
            int counter = 0;
            for (int xi = 0; xi < rgb.length; xi++) {
                double[] distance_for_centroid = new double[K];
                for (int k = 0; k < K; k++) {
                    distance_for_centroid[k] = Math.pow((Math.abs(rgb[xi]) - Math.abs(c_k[k])), 2);
                }

                double MinVal = distance_for_centroid[0];
                int cluster = 0;

                for (int i = 1; i < distance_for_centroid.length; i++) {
                    if (distance_for_centroid[i] < MinVal) {
                        MinVal = distance_for_centroid[i];
                        cluster = i;
                    }
                }
                if (old_distance[xi] > MinVal) {
                    old_distance[xi] = MinVal;
                    ai[xi] = cluster;
                    counter++;

                }
            }

            for (int s = 0; s < rgb.length; s++) {
                rgb[s] = c_k[ai[s]];
            }

            //Mean Calcluation
            for (int m = 0; m < K; m++) {
                int sum = 0;
                int c = 1;
                for (int i = 0; i < rgb.length; i++) {
                    if (ai[i] == m) {
                        sum = sum + rgb[ai[i]];
                        c++;
                    }
                }
                c_k[m] = sum / c;

            }

            if (counter == 0) {
                
                break;
            }
            System.out.println("Iteration " + iter + " Done!");
        }
    }//Entire KMeans
}//Program
