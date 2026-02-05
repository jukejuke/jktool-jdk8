package io.github.jukejuke.map.util;

import lombok.Data;

/**
 * 坐标转换工具类
 * 封装WGS-84、GCJ-02、BD-09坐标系之间的转换算法
 * 经测试BD-09(百度坐标)转回WGS-84、GCJ-02 有偏差，其它坐标系转换无问题
 * 
 * 坐标系说明：
 * - WGS-84：GPS原始坐标，地球坐标系
 * - GCJ-02：火星坐标系，中国境内使用的加密坐标
 * - BD-09：百度坐标系，在GCJ-02基础上进一步加密
 * - CGCS2000：中国国家测绘局2000年发布的大地坐标系（天地图使用）（WGS-84与CGCS2000转换误差在小于10厘米）
 */
public class CoordinateConverter {
    
    private static final double PI = Math.PI;
    private static final double AXIS = 6378245.0;
    private static final double EE = 0.00669342162296594323;
    private static final double X_PI = Math.PI * 3000.0 / 180.0;
    private static final double OFFSET_LAT = 0.006;
    private static final double OFFSET_LNG = 0.0065;

    /**
     * 坐标点类
     */
    @Data
    public static class Point {
        private double longitude; // 经度
        private double latitude;  // 纬度

        public Point(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        @Override
        public String toString() {
            return String.format("Point{longitude=%.6f, latitude=%.6f}", longitude, latitude);
        }
    }

    /**
     * 判断坐标是否在中国境内
     * 
     * @param longitude 经度
     * @param latitude 纬度
     * @return 是否在中国境内
     */
    private static boolean isInChina(double longitude, double latitude) {
        return longitude >= 72.004 && longitude <= 137.8347 && latitude >= 0.8293 && latitude <= 55.8271;
    }

    /**
     * WGS-84 转 GCJ-02
     * 
     * @param wgs84Point WGS-84坐标点
     * @return GCJ-02坐标点
     */
    public static Point wgs84ToGcj02(Point wgs84Point) {
        double wgs84Lng = wgs84Point.getLongitude();
        double wgs84Lat = wgs84Point.getLatitude();

        if (!isInChina(wgs84Lng, wgs84Lat)) {
            return new Point(wgs84Lng, wgs84Lat);
        }

        double dLng = transformLng(wgs84Lng - 105.0, wgs84Lat - 35.0);
        double dLat = transformLat(wgs84Lng - 105.0, wgs84Lat - 35.0);
        double radLat = wgs84Lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLng = (dLng * 180.0) / (AXIS / sqrtMagic * Math.cos(radLat) * PI);
        dLat = (dLat * 180.0) / ((AXIS * (1 - EE)) / (magic * sqrtMagic) * PI);
        double gcjLat = wgs84Lat + dLat;
        double gcjLng = wgs84Lng + dLng;
        return new Point(gcjLng, gcjLat);
    }

    /**
     * GCJ-02 转 WGS-84
     * 
     * @param gcj02Point GCJ-02坐标点
     * @return WGS-84坐标点
     */
    public static Point gcj02ToWgs84(Point gcj02Point) {
        double gcjLng = gcj02Point.getLongitude();
        double gcjLat = gcj02Point.getLatitude();

        if (!isInChina(gcjLng, gcjLat)) {
            return new Point(gcjLng, gcjLat);
        }

        Point offsetPoint = transformGcj02Offset(gcjLng, gcjLat);
        double wgsLng = gcjLng - offsetPoint.getLongitude();
        double wgsLat = gcjLat - offsetPoint.getLatitude();
        return new Point(wgsLng, wgsLat);
    }

    /**
     * GCJ-02 转 BD-09
     * 
     * @param gcj02Point GCJ-02坐标点
     * @return BD-09坐标点
     */
    public static Point gcj02ToBd09(Point gcj02Point) {
        double gcjLng = gcj02Point.getLongitude();
        double gcjLat = gcj02Point.getLatitude();

        if (!isInChina(gcjLng, gcjLat)) {
            return new Point(gcjLng, gcjLat);
        }

        double z = Math.sqrt(gcjLng * gcjLng + gcjLat * gcjLat) + 0.00002 * Math.sin(gcjLat * X_PI);
        double theta = Math.atan2(gcjLat, gcjLng) + 0.000003 * Math.cos(gcjLng * X_PI);
        double bdLng = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new Point(bdLng, bdLat);
    }

    /**
     * BD-09 转 GCJ-02
     * 
     * @param bd09Point BD-09坐标点
     * @return GCJ-02坐标点
     */
    public static Point bd09ToGcj02(Point bd09Point) {
        double bdLng = bd09Point.getLongitude();
        double bdLat = bd09Point.getLatitude();

        if (!isInChina(bdLng, bdLat)) {
            return new Point(bdLng, bdLat);
        }

        double x = bdLng - 0.0065;
        double y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double gcjLng = z * Math.cos(theta);
        double gcjLat = z * Math.sin(theta);
        return new Point(gcjLng, gcjLat);
    }

    /**
     * WGS-84 转 BD-09
     * 
     * @param wgs84Point WGS-84坐标点
     * @return BD-09坐标点
     */
    public static Point wgs84ToBd09(Point wgs84Point) {
        Point gcj02Point = wgs84ToGcj02(wgs84Point);
        return gcj02ToBd09(gcj02Point);
    }

    /**
     * BD-09 转 WGS-84
     * 
     * @param bd09Point BD-09坐标点
     * @return WGS-84坐标点
     */
    public static Point bd09ToWgs84(Point bd09Point) {
        Point gcj02Point = bd09ToGcj02(bd09Point);
        return gcj02ToWgs84(gcj02Point);
    }

    /**
     * 纬度转换函数
     * 
     * @param lng 经度差
     * @param lat 纬度差
     * @return 转换后的纬度
     */
    private static double transformLat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度转换函数
     * 
     * @param lng 经度差
     * @param lat 纬度差
     * @return 转换后的经度
     */
    private static double transformLng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * GCJ-02偏移量计算
     * 
     * @param lng 经度
     * @param lat 纬度
     * @return 偏移量点
     */
    private static Point transformGcj02Offset(double lng, double lat) {
        double dLng = transformLng(lng - 105.0, lat - 35.0);
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLng = (dLng * 180.0) / (AXIS / sqrtMagic * Math.cos(radLat) * PI);
        dLat = (dLat * 180.0) / ((AXIS * (1 - EE)) / (magic * sqrtMagic) * PI);
        return new Point(dLng, dLat);
    }
}