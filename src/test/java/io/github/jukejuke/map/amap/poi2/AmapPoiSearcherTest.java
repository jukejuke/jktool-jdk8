package io.github.jukejuke.map.amap.poi2;

import okhttp3.OkHttpClient;
import org.junit.Test;
import static org.junit.Assert.*;

public class AmapPoiSearcherTest {
    private static final String API_KEY = "";
    private final OkHttpClient httpClient = new OkHttpClient();

    @Test
    public void testSearchPoi() throws Exception {
        if (API_KEY.isEmpty()) {
            System.out.println("请替换API_KEY进行测试");
            return;
        }

        AmapPoiSearcher searcher = new AmapPoiSearcher.Builder(API_KEY)
                .httpClient(httpClient)
                .build();

        AmapPoiSearcher.AmapPoiResponse response = searcher.searchPoi("北京大学", "141201", "北京市");

        assertEquals("1", response.getStatus());
        assertEquals("10", response.getCount());
        assertEquals(10, response.getPois().length);
        assertEquals("北京大学", response.getPois()[0].getName());
        assertEquals("颐和园路5号", response.getPois()[0].getAddress());
        assertEquals("110108", response.getPois()[0].getAdcode());
    }

    @Test(expected = Exception.class)
    public void testInvalidApiKey() throws Exception {
        AmapPoiSearcher searcher = new AmapPoiSearcher.Builder("invalid_key")
                .httpClient(httpClient)
                .build();

        searcher.searchPoi("测试", "141201", "北京市");
    }

    @Test
    public void testSearchAround() throws Exception {
        if (API_KEY.isEmpty()) {
            System.out.println("请替换API_KEY进行测试");
            return;
        }

        AmapPoiSearcher searcher = new AmapPoiSearcher.Builder(API_KEY)
                .httpClient(httpClient)
                .build();

        // 北京市中心坐标：116.404269,39.913164，搜索半径1000米，大学类型编码141201
        AmapPoiSearcher.AmapPoiResponse response = searcher.searchAround("116.404269,39.913164", 1000, "141201");

        assertEquals("1", response.getStatus());
        assertTrue(Integer.parseInt(response.getCount()) > 0);
        assertNotNull(response.getPois());
        assertTrue(response.getPois().length > 0);
    }

    @Test
    public void testSearchPolygon() throws Exception {
        if (API_KEY.isEmpty()) {
            System.out.println("请替换API_KEY进行测试");
            return;
        }

        AmapPoiSearcher searcher = new AmapPoiSearcher.Builder(API_KEY)
                .httpClient(httpClient)
                .build();

        // 多边形坐标点，格式为"经度1,纬度1|经度2,纬度2|...|经度n,纬度n"
        String polygon = "116.460988,40.006919|116.48231,40.007381|116.47516,39.99713|116.472596,39.985227|116.45669,39.984989|116.460988,40.006919";
        AmapPoiSearcher.AmapPoiResponse response = searcher.searchPolygon(polygon, "肯德基", "050301");

        assertEquals("1", response.getStatus());
        assertTrue(Integer.parseInt(response.getCount()) > 0);
        assertNotNull(response.getPois());
        assertTrue(response.getPois().length > 0);
        // 验证至少有一个结果包含关键词"肯德基"
        boolean hasKfc = false;
        for (AmapPoiSearcher.AmapPoiResponse.Poi poi : response.getPois()) {
            if (poi.getName().contains("肯德基")) {
                hasKfc = true;
                break;
            }
        }
        assertTrue(hasKfc);
    }

    @Test
    public void testSearchById() throws Exception {
        if (API_KEY.isEmpty()) {
            System.out.println("请替换API_KEY进行测试");
            return;
        }

        AmapPoiSearcher searcher = new AmapPoiSearcher.Builder(API_KEY)
                .httpClient(httpClient)
                .build();

        // POI ID列表，多个ID用竖线分隔
        String ids = "B000A7BM4H|B0FFKEPXS2";
        AmapPoiSearcher.AmapPoiResponse response = searcher.searchById(ids);

        assertEquals("1", response.getStatus());
        assertTrue(Integer.parseInt(response.getCount()) > 0);
        assertNotNull(response.getPois());
        // 验证返回结果数量与请求ID数量一致
        String[] idArray = ids.split("\\|");
        assertEquals(idArray.length, response.getPois().length);
    }
}