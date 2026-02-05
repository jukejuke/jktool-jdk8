package io.github.jukejuke.map.amap;

import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AmapCoordinateConverterTest {
    private AmapCoordinateConverter converter;
    private static final String API_KEY = ""; // 请替换为实际API密钥

    @Before
    public void setUp() {
        // 初始化转换器实例
        converter = new AmapCoordinateConverter.Builder(API_KEY)
                .httpClient(new OkHttpClient())
                .build();
    }

    @Test
    public void testGpsToAmapConversion() throws Exception {
        // 测试GPS坐标转换
        String testLocations = "116.481499,39.990475";
        AmapCoordinateConverter.CoordinateSystem sourceCoordsys = AmapCoordinateConverter.CoordinateSystem.GPS;

        AmapCoordinateConverter.AmapCoordinateResponse response = converter.convert(testLocations, sourceCoordsys);

        // 验证响应状态
        assertEquals("1", response.getStatus());
        assertEquals("ok", response.getInfo());
        assertEquals("10000", response.getInfocode());
        assertEquals("116.487585177952,39.991754014757", response.getLocations());
    }

    @Test
    public void testBatchConversion() throws Exception {
        // 测试批量坐标转换
        String testLocations = "116.481499,39.990475|121.473701,31.230416";
        AmapCoordinateConverter.CoordinateSystem sourceCoordsys = AmapCoordinateConverter.CoordinateSystem.GPS;

        AmapCoordinateConverter.AmapCoordinateResponse response = converter.convert(testLocations, sourceCoordsys);

        // 验证批量转换结果
        assertEquals("1", response.getStatus());
        assertEquals("ok", response.getInfo());
        assertEquals("10000", response.getInfocode());
        assertTrue("应返回多个转换结果", response.getLocations().contains(";"));
    }

    @Test(expected = Exception.class)
    public void testInvalidApiKey() throws Exception {
        // 测试无效API密钥（预期抛出异常）
        AmapCoordinateConverter invalidConverter = new AmapCoordinateConverter.Builder("invalid_key")
                .httpClient(new OkHttpClient())
                .build();

        invalidConverter.convert("116.481499,39.990475", AmapCoordinateConverter.CoordinateSystem.GPS);
    }
}