package app.alansari;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class CandidateCode {
    /*
     * Complete the function below.
    */

    public static String[] getTolls(int input1, String[] input2) {
        //Write code here
        return null;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String[] output = null;
        int ip1 = Integer.parseInt(in.nextLine().trim());
        int ip2_size = 0;
        ip2_size = Integer.parseInt(in.nextLine().trim());
        String[] ip2 = new String[ip2_size];
        String ip2_item;
        for (int ip2_i = 0; ip2_i < ip2_size; ip2_i++) {
            try {
                ip2_item = in.nextLine();
            } catch (Exception e) {
                ip2_item = null;
            }
            ip2[ip2_i] = ip2_item;
        }
        System.out.println(ip1 + " Done  " + ip2);
        //output = getTolls(ip1, ip2);
//        for (int output_i = 0; output_i < output.length; output_i++) {
//            System.out.println(String.valueOf(output[output_i]));
//        }
    }
}