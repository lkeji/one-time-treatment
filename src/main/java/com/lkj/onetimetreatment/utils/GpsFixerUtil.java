package com.lkj.onetimetreatment.utils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 从网上找到的被开源的地图纠偏算法</br>
 * <p>
 * 各地图API坐标系统比较与转换;</br>
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;</br>
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。</br>
 *
 * @author weic
 * @version 1.0 2016/5/16
 */
public class GpsFixerUtil {

    //WGS长轴半径
    public static double A = 6378245.0;
    //WGS偏心率的平方
    public static double EE = 0.00669342162296594323;

    /**
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param loc
     * @return double[]{lon, lat}
     */
    public static double[] gps84_To_Gcj02(double[] loc) {
        if (isOutOfChina(loc)) {
            return loc;
        }

        double oriLon = loc[0];
        double oriLat = loc[1];

        double dLat = transformLat(new double[]{oriLon - 105.0, oriLat - 35.0});
        double dLon = transformLon(new double[]{oriLon - 105.0, oriLat - 35.0});
        double radLat = oriLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * Math.PI);
        double mgLat = oriLat + dLat;
        double mgLon = oriLon + dLon;
        return new double[]{mgLon, mgLat};
    }

    /**
     * 火星坐标系 (GCJ-02) to 84
     *
     * @param loc
     * @return
     */
    public static double[] gcj02_To_Gps84(double[] loc) {
        double oriLon = loc[0];
        double oriLat = loc[1];

        double[] transformLoc = transform(new double[]{oriLon, oriLat});
        double lontitude = oriLon * 2 - transformLoc[0];
        double latitude = oriLat * 2 - transformLoc[1];
        return new double[]{lontitude, latitude};
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法
     * 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param loc
     */
    public static double[] gcj02_To_Bd09(double[] loc) {
        double oriLon = loc[0];
        double oriLat = loc[1];
        double z = Math.sqrt(oriLon * oriLon + oriLat * oriLat) + 0.00002 * Math.sin(oriLat * Math.PI);
        double theta = Math.atan2(oriLat, oriLon) + 0.000003 * Math.cos(oriLon * Math.PI);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[]{bd_lon, bd_lat};
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法
     * 将 BD-09 坐标转换成GCJ-02 坐标
     *
     * @param loc
     * @return
     */
    public static double[] bd09_To_Gcj02(double[] loc) {
        double oriLon = loc[0];
        double oriLat = loc[1];
        double x = oriLon - 0.0065;
        double y = oriLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[]{gg_lon, gg_lat};
    }

    /**
     * (BD-09)-->84
     *
     * @param loc
     * @return
     */
    public static double[] bd09_To_Gps84(double[] loc) {
        double[] gcj02 = bd09_To_Gcj02(loc);
        double[] map84 = gcj02_To_Gps84(gcj02);
        return map84;
    }

    /**
     * 84 ---> (BD-09)
     *
     * @param loc
     * @return
     */
    public static double[] gps84_To_Bd09(double[] loc) {
        double[] gcj02 = gps84_To_Gcj02(loc);
        double[] db09 = gcj02_To_Bd09(gcj02);
        return db09;
    }

    /**
     * 粗略判断经纬度是否在中国外面
     *
     * @param loc
     * @return
     */
    public static boolean isOutOfChina(double[] loc) {
        double oriLon = loc[0];
        double oriLat = loc[1];
        if (oriLon < 72.004 || oriLon > 137.8347)
            return true;
        if (oriLat < 0.8293 || oriLat > 55.8271)
            return true;
        return false;
    }

    private static double[] transform(double[] loc) {
        if (isOutOfChina(loc)) {
            return loc;
        }
        double oriLon = loc[0];
        double oriLat = loc[1];
        double dLat = transformLat(new double[]{oriLon - 105.0, oriLat - 35.0});
        double dLon = transformLon(new double[]{oriLon - 105.0, oriLat - 35.0});
        double radLat = oriLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * Math.PI);
        double mgLat = oriLat + dLat;
        double mgLon = oriLon + dLon;
        return new double[]{mgLon, mgLat};
    }

    private static double transformLat(double[] loc) {
        double x = loc[0];
        double y = loc[1];
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double[] loc) {
        double x = loc[0];
        double y = loc[1];
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0
                * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

}
