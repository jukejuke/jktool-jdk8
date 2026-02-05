package io.github.jukejuke.map.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 坐标转换工具类测试
 */
public class CoordinateConverterTest {

    private static final double DELTA = 1e-6; // 精度误差范围

    @Test
    @DisplayName("测试WGS-84转GCJ-02转换")
    public void testWgs84ToGcj02() {
        // 北京天安门坐标
        CoordinateConverter.Point wgs84Point = new CoordinateConverter.Point(104.220860,31.004800);
        CoordinateConverter.Point gcj02Point = CoordinateConverter.wgs84ToGcj02(wgs84Point);
        
        System.out.println("WGS-84原始坐标: " + wgs84Point);
        System.out.println("GCJ-02转换坐标: " + gcj02Point);
        System.out.println("经度: " + gcj02Point.getLongitude()+","+gcj02Point.getLatitude());
        
        assertNotNull(gcj02Point);
        assertTrue(gcj02Point.getLongitude() > wgs84Point.getLongitude(), "经度应该增加");
        assertTrue(gcj02Point.getLatitude() > wgs84Point.getLatitude(), "纬度应该增加");
    }

    @Test
    @DisplayName("测试GCJ-02转WGS-84转换")
    public void testGcj02ToWgs84() {
        // GCJ-02坐标（北京地区）
        CoordinateConverter.Point gcj02Point = new CoordinateConverter.Point(104.249339,30.998391);
        CoordinateConverter.Point wgs84Point = CoordinateConverter.gcj02ToWgs84(gcj02Point);
        
        System.out.println("GCJ-02原始坐标: " + gcj02Point);
        System.out.println("WGS-84转换坐标: " + wgs84Point);
        System.out.println("经度: " + wgs84Point.getLongitude()+","+wgs84Point.getLatitude());
        
        assertNotNull(wgs84Point);
        assertTrue(wgs84Point.getLongitude() < gcj02Point.getLongitude(), "经度应该减少");
        assertTrue(wgs84Point.getLatitude() < gcj02Point.getLatitude(), "纬度应该减少");
    }

    @Test
    @DisplayName("测试GCJ-02转BD-09转换")
    public void testGcj02ToBd09() {
        CoordinateConverter.Point gcj02Point = new CoordinateConverter.Point(104.244406,30.931214);
        CoordinateConverter.Point bd09Point = CoordinateConverter.gcj02ToBd09(gcj02Point);
        
        System.out.println("GCJ-02原始坐标: " + gcj02Point);
        System.out.println("BD-09转换坐标: " + bd09Point);
        System.out.println("经度: " + bd09Point.getLongitude()+","+bd09Point.getLatitude());
        
        assertNotNull(bd09Point);
        assertTrue(bd09Point.getLongitude() > gcj02Point.getLongitude(), "经度应该增加");
        assertTrue(bd09Point.getLatitude() > gcj02Point.getLatitude(), "纬度应该增加");
    }

    @Test
    @DisplayName("测试BD-09转GCJ-02转换")
    public void testBd09ToGcj02() {
        CoordinateConverter.Point bd09Point = new CoordinateConverter.Point(104.27,30.99);
        CoordinateConverter.Point gcj02Point = CoordinateConverter.bd09ToGcj02(bd09Point);
        
        System.out.println("BD-09原始坐标: " + bd09Point);
        System.out.println("GCJ-02转换坐标: " + gcj02Point);
        System.out.println("经度: " + gcj02Point.getLongitude()+","+gcj02Point.getLatitude());
        
        assertNotNull(gcj02Point);
        assertTrue(gcj02Point.getLongitude() < bd09Point.getLongitude(), "经度应该减少");
        assertTrue(gcj02Point.getLatitude() < bd09Point.getLatitude(), "纬度应该减少");
    }

    @Test
    @DisplayName("测试WGS-84转BD-09转换")
    public void testWgs84ToBd09() {
        CoordinateConverter.Point wgs84Point = new CoordinateConverter.Point(104.259310,30.991200);
        CoordinateConverter.Point bd09Point = CoordinateConverter.wgs84ToBd09(wgs84Point);
        
        System.out.println("WGS-84原始坐标: " + wgs84Point);
        System.out.println("BD-09转换坐标: " + bd09Point);
        System.out.println("经度: " + bd09Point.getLongitude()+","+bd09Point.getLatitude());
        
        assertNotNull(bd09Point);
    }

    @Test
    @DisplayName("测试BD-09转WGS-84转换")
    public void testBd09ToWgs84() {
        CoordinateConverter.Point bd09Point = new CoordinateConverter.Point(104.2681702963151,30.99498422184284);
        CoordinateConverter.Point wgs84Point = CoordinateConverter.bd09ToWgs84(bd09Point);
        
        System.out.println("BD-09原始坐标: " + bd09Point);
        System.out.println("WGS-84转换坐标: " + wgs84Point);
        System.out.println("经度: " + wgs84Point.getLongitude()+","+bd09Point.getLatitude());
        
        assertNotNull(wgs84Point);
    }

    @Test
    @DisplayName("测试往返转换精度")
    public void testRoundTripConversion() {
        // 测试WGS-84 -> GCJ-02 -> WGS-84
        CoordinateConverter.Point originalWgs84 = new CoordinateConverter.Point(116.397428, 39.90923);
        CoordinateConverter.Point toGcj02 = CoordinateConverter.wgs84ToGcj02(originalWgs84);
        CoordinateConverter.Point backToWgs84 = CoordinateConverter.gcj02ToWgs84(toGcj02);
        
        System.out.println("原始WGS-84: " + originalWgs84);
        System.out.println("转换到GCJ-02: " + toGcj02);
        System.out.println("转回WGS-84: " + backToWgs84);
        
        assertEquals(originalWgs84.getLongitude(), backToWgs84.getLongitude(), DELTA, "往返转换经度精度检查");
        assertEquals(originalWgs84.getLatitude(), backToWgs84.getLatitude(), DELTA, "往返转换纬度精度检查");

        // 测试GCJ-02 -> BD-09 -> GCJ-02
        CoordinateConverter.Point originalGcj02 = new CoordinateConverter.Point(116.404269, 39.913385);
        CoordinateConverter.Point toBd09 = CoordinateConverter.gcj02ToBd09(originalGcj02);
        CoordinateConverter.Point backToGcj02 = CoordinateConverter.bd09ToGcj02(toBd09);
        
        System.out.println("原始GCJ-02: " + originalGcj02);
        System.out.println("转换到BD-09: " + toBd09);
        System.out.println("转回GCJ-02: " + backToGcj02);
        
        assertEquals(originalGcj02.getLongitude(), backToGcj02.getLongitude(), DELTA, "往返转换经度精度检查");
        assertEquals(originalGcj02.getLatitude(), backToGcj02.getLatitude(), DELTA, "往返转换纬度精度检查");
    }

    @Test
    @DisplayName("测试境外坐标处理")
    public void testForeignCoordinate() {
        // 美国纽约坐标
        CoordinateConverter.Point foreignPoint = new CoordinateConverter.Point(-74.0059, 40.7128);
        
        CoordinateConverter.Point toGcj02 = CoordinateConverter.wgs84ToGcj02(foreignPoint);
        CoordinateConverter.Point toBd09 = CoordinateConverter.gcj02ToBd09(toGcj02);
        
        System.out.println("境外坐标: " + foreignPoint);
        System.out.println("转GCJ-02: " + toGcj02);
        System.out.println("转BD-09: " + toBd09);
        
        // 境外坐标应该保持不变
        assertEquals(foreignPoint.getLongitude(), toGcj02.getLongitude(), DELTA);
        assertEquals(foreignPoint.getLatitude(), toGcj02.getLatitude(), DELTA);
        assertEquals(foreignPoint.getLongitude(), toBd09.getLongitude(), DELTA);
        assertEquals(foreignPoint.getLatitude(), toBd09.getLatitude(), DELTA);
    }

    @Test
    @DisplayName("测试边界情况")
    public void testEdgeCases() {
        // 测试中国边界坐标
        CoordinateConverter.Point[] edgeCases = {
            new CoordinateConverter.Point(72.004, 0.8293),     // 中国西南边界
            new CoordinateConverter.Point(137.8347, 55.8271),  // 中国东北边界
            new CoordinateConverter.Point(105.0, 35.0),        // 中国中心点
        };

        for (CoordinateConverter.Point point : edgeCases) {
            System.out.println("测试边界坐标: " + point);
            
            CoordinateConverter.Point toGcj02 = CoordinateConverter.wgs84ToGcj02(point);
            CoordinateConverter.Point toBd09 = CoordinateConverter.gcj02ToBd09(toGcj02);
            
            System.out.println("  -> GCJ-02: " + toGcj02);
            System.out.println("  -> BD-09: " + toBd09);
            
            assertNotNull(toGcj02);
            assertNotNull(toBd09);
        }
    }

    @Test
    @DisplayName("测试常见城市坐标转换")
    public void testCommonCities() {
        // 北京
        CoordinateConverter.Point beijing = new CoordinateConverter.Point(116.397428, 39.90923);
        testCityCoordinates("北京", beijing);
        
        // 上海
        CoordinateConverter.Point shanghai = new CoordinateConverter.Point(121.4737, 31.2304);
        testCityCoordinates("上海", shanghai);
        
        // 广州
        CoordinateConverter.Point guangzhou = new CoordinateConverter.Point(113.2644, 23.1291);
        testCityCoordinates("广州", guangzhou);
        
        // 深圳
        CoordinateConverter.Point shenzhen = new CoordinateConverter.Point(114.0579, 22.5431);
        testCityCoordinates("深圳", shenzhen);
    }

    private void testCityCoordinates(String cityName, CoordinateConverter.Point wgs84Point) {
        System.out.println("\n" + cityName + "坐标转换:");
        System.out.println("WGS-84: " + wgs84Point);
        
        CoordinateConverter.Point gcj02Point = CoordinateConverter.wgs84ToGcj02(wgs84Point);
        System.out.println("GCJ-02: " + gcj02Point);
        
        CoordinateConverter.Point bd09Point = CoordinateConverter.gcj02ToBd09(gcj02Point);
        System.out.println("BD-09: " + bd09Point);
        
        // 验证转换后坐标的合理性
        assertTrue(gcj02Point.getLongitude() > 70 && gcj02Point.getLongitude() < 140, cityName + " GCJ-02经度范围检查");
        assertTrue(gcj02Point.getLatitude() > 0 && gcj02Point.getLatitude() < 60, cityName + " GCJ-02纬度范围检查");
        assertTrue(bd09Point.getLongitude() > 70 && bd09Point.getLongitude() < 140, cityName + " BD-09经度范围检查");
        assertTrue(bd09Point.getLatitude() > 0 && bd09Point.getLatitude() < 60, cityName + " BD-09纬度范围检查");
    }
}