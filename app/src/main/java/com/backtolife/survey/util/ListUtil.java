package com.backtolife.survey.util;

import com.google.common.primitives.Floats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtil {
    public static float[] loadFloatArray(String fname) throws IOException {
        return loadFloatArray(new BufferedReader(new FileReader(fname)));
    }

    public static float[] loadFloatArray(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        String[] stringArray = line.split(",");
        float[] floatArray = new float[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            floatArray[i] = Float.parseFloat(stringArray[i]);
        }
        return floatArray;
    }


    public static List<Integer> loadIntegerList(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        String[] stringArray = line.split(",");
        List<Integer> res = new ArrayList<>(stringArray.length);
        for (int i = 0; i < stringArray.length; i++) {
            res.set(i, Integer.parseInt(stringArray[i]));
        }
        return res;
    }

    public static List<Double> loadDoubleList(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        String[] stringArray = line.split(",");
        List<Double> res = new ArrayList<>(stringArray.length);
        for (int i = 0; i < stringArray.length; i++) {
            res.set(i, Double.parseDouble(stringArray[i]));
        }
        return res;
    }


    public static int argmax(float[] elems) {
        float max = elems[0];
        int bestIdx = 0;
        for (int i = 1; i < elems.length; i++) {
            if (elems[i] > max) {
                max = elems[i];
                bestIdx = i;
            }
        }
        return bestIdx;
    }

    public static void writeDoubleList(List<Double> arr, BufferedWriter writer) throws IOException {
        for (double value: arr) {
            writer.write(Double.toString(value));
            writer.write(",");
        }
        writer.write("\n");
    }

    public static void writeIntegerList(List<Integer> arr, BufferedWriter writer) throws IOException {
        for (Integer value: arr) {
            writer.write(Integer.toString(value));
            writer.write(",");
        }
        writer.write("\n");
    }

    public static<K, V> Map<K, V> createMap1(K key, V val){
        HashMap<K, V> res = new HashMap<>();
        res.put(key, val);
        return res;
    }

    public static<K, V> Map<K, V> createMap2(K key1, V val1, K key2, V val2){
        HashMap<K, V> res = new HashMap<>();
        res.put(key1, val1);
        res.put(key2, val2);
        return res;
    }

    public static byte[] toByteArray(double[] doubleArray){
        int times = Double.SIZE / Byte.SIZE;
        byte[] bytes = new byte[doubleArray.length * times];
        for(int i=0;i<doubleArray.length;i++){
            ByteBuffer.wrap(bytes, i*times, times).putDouble(doubleArray[i]);
        }
        return bytes;
    }

    public static double[] toDoubleArray(byte[] byteArray){
        int times = Double.SIZE / Byte.SIZE;
        double[] doubles = new double[byteArray.length / times];
        for(int i=0;i<doubles.length;i++){
            doubles[i] = ByteBuffer.wrap(byteArray, i*times, times).getDouble();
        }
        return doubles;
    }

    public static byte[] toByteArray(int[] intArray){
        int times = Integer.SIZE / Byte.SIZE;
        byte[] bytes = new byte[intArray.length * times];
        for(int i=0;i<intArray.length;i++){
            ByteBuffer.wrap(bytes, i*times, times).putInt(intArray[i]);
        }
        return bytes;
    }

    public static int[] toIntArray(byte[] byteArray){
        int times = Integer.SIZE / Byte.SIZE;
        int[] ints = new int[byteArray.length / times];
        for(int i=0;i<ints.length;i++){
            ints[i] = ByteBuffer.wrap(byteArray, i*times, times).getInt();
        }
        return ints;
    }

    public static float[] loadFloatData(InputStream stream) throws IOException {
        DataInputStream in = new DataInputStream(stream);
        List<Float> res = new ArrayList<>();
        while(in.available() > 0) {
            res.add(in.readFloat());
        }
        return Floats.toArray(res);
    }
}
