package com.vibe.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 类功能名称
 *
 * @Description 数据统计工具类
 * @Author lxx-nice@163.com
 * @Create 2020/05/11
 * @Module 智慧园区
 */

public class DataStatisticsUtils {
    /**
     * 求和
     *
     * @param arr
     * @return
     */
    public static double getSum(double[] arr) {
        double sum = 0;
        for (double num : arr) {
            sum += num;
        }
        return sum;
    }

    /**
     * 求均值
     *
     * @param arr
     * @return
     */
    public static double getMean(double[] arr) {
        return getSum(arr) / arr.length;
    }

    /**
     * 求众数
     *
     * @param arr
     * @return
     */
    public static double getMode(double[] arr) {
        Map<Double, Integer> map = new HashMap<Double, Integer>();
        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(arr[i])) {
                map.put(arr[i], map.get(arr[i]) + 1);
            } else {
                map.put(arr[i], 1);
            }
        }
        int maxCount = 0;
        double mode = -1;
        Iterator<Double> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            double num = iter.next();
            int count = map.get(num);
            if (count > maxCount) {
                maxCount = count;
                mode = num;
            }
        }
        return mode;
    }

    /**
     * 求中位数
     *
     * @param arr
     * @return
     */
    public static double getMedian(double[] arr) {
        double[] tempArr = Arrays.copyOf(arr, arr.length);
        Arrays.sort(tempArr);
        if (tempArr.length % 2 == 0) {
            return (tempArr[tempArr.length >> 1] + tempArr[(tempArr.length >> 1) - 1]) / 2;
        } else {
            return tempArr[(tempArr.length >> 1)];
        }
    }


    /**
     * 求中列数
     *
     * @param arr
     * @return
     */
    public static double getMidrange(double[] arr) {
        double max = arr[0], min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return (min + max) / 2;
    }

    /**
     * 求四分位数
     *
     * @param arr
     * @return 存放三个四分位数的数组
     */
    public static double[] getQuartiles(double[] arr) {
        double[] tempArr = Arrays.copyOf(arr, arr.length);
        Arrays.sort(tempArr);
        double[] quartiles = new double[3];
        // 第二四分位数（中位数）
        quartiles[1] = getMedian(tempArr);
        // 求另外两个四分位数
        if (tempArr.length % 2 == 0) {
            quartiles[0] = getMedian(Arrays.copyOfRange(tempArr, 0, tempArr.length / 2));
            quartiles[2] = getMedian(Arrays.copyOfRange(tempArr, tempArr.length / 2, tempArr.length));
        } else {
            quartiles[0] = getMedian(Arrays.copyOfRange(tempArr, 0, tempArr.length / 2));
            quartiles[2] = getMedian(Arrays.copyOfRange(tempArr, tempArr.length / 2 + 1, tempArr.length));
        }
        return quartiles;
    }

    /**
     * 求极差
     *
     * @param arr
     * @return
     */
    public static double getRange(double[] arr) {
        double max = arr[0], min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return max - min;
    }

    /**
     * 求四分位数极差
     *
     * @param arr
     * @return
     */
    public static double getQuartilesRange(double[] arr) {
        return getRange(getQuartiles(arr));
    }

    /**
     * 求截断均值
     *
     * @param arr 求值数组
     * @param p   截断量p，例如p的值为20，则截断20%（高10%，低10%）
     * @return
     */
    public static double getTrimmedMean(double[] arr, int p) {
        selectSortDescendingArray(arr);
        int tmp = arr.length * p / 100;
        double[] tempArr = Arrays.copyOfRange(arr, tmp, arr.length - tmp);
        return getMean(tempArr);
    }

    /**
     * 求方差
     *
     * @param arr
     * @return
     */
    public static double getVariance(double[] arr) {
        double variance = 0;
        double sum = 0, sum2 = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            sum2 += arr[i] * arr[i];
        }
        variance = sum2 / arr.length - (sum / arr.length) * (sum / arr.length);
        return variance;
    }

    /**
     * 求绝对平均偏差(AAD)
     *
     * @param arr
     * @return
     */
    public static double getAbsoluteAverageDeviation(double[] arr) {
        double sum = 0;
        double mean = getMean(arr);
        for (int i = 0; i < arr.length; i++) {
            sum += Math.abs(arr[i] - mean);
        }
        return sum / arr.length;
    }

    /**
     * 求中位数绝对偏差(MAD)
     *
     * @param arr
     * @return
     */
    public static double getMedianAbsoluteDeviation(double[] arr) {
        double[] tempArr = new double[arr.length];
        double median = getMedian(arr);
        for (int i = 0; i < arr.length; i++) {
            tempArr[i] = Math.abs(arr[i] - median);
        }
        return getMedian(tempArr);
    }

    /**
     * 求标准差
     * @param arr
     * @return
     */
    public static double getStandardDevition(double[] arr) {
        double sum = 0;
        double mean = getMean(arr);
        for (int i = 0; i < arr.length; i++) {
            sum += Math.sqrt((arr[i] - mean) * (arr[i] - mean));
        }
        return (sum / (arr.length - 1));
    }

    //选择排序对数据进行降序排序(double)
    public static void selectSortDescendingArray(double[] arr){
        for(int i = 0; i<arr.length-1;i++){//i<arr.length-1;最后一个不用比较
          for(int j = i+1;j<arr.length;j++){
                if(arr[i]<arr[j]){
                    double temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                    }
                }
            }
        }
//选择排序对数据进行升序排序(double)
    public static void selectSortAscendingArray(double[] arr){
            for(int i = 0; i<arr.length-1;i++){//i<arr.length-1;最后一个不用比较
                for(int j = i+1;j<arr.length;j++){
                    if(arr[i]>arr[j]){
                        double temp = arr[j];
                        arr[j] = arr[i];
                        arr[i] = temp;
                     }
                   }
                }
        }
    public static void main(String[] args) {
        double[] arr = {200.2,65.3,98.2,69,600};
        System.out.println(getTrimmedMean(arr,20));
    }
}
