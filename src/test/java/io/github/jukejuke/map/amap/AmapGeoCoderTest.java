package io.github.jukejuke.map.amap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * 高德地图地理编码工具测试类
 */
public class AmapGeoCoderTest {
    private static final String TEST_API_KEY = "";
    private static final String MOCK_RESPONSE_SUCCESS = "{\"status\":\"1\",\"info\":\"OK\",\"infocode\":\"10000\",\"count\":\"3\",\"geocodes\":[{\"formatted_address\":\"四川省成都市武侯区天府大道北段1089号\",\"country\":\"中国\",\"province\":\"四川省\",\"citycode\":\"028\",\"city\":\"成都市\",\"district\":\"武侯区\",\"township\":[],\"neighborhood\":{\"name\":[],\"type\":[]},\"building\":{\"name\":[],\"type\":[]},\"adcode\":\"510107\",\"street\":\"天府大道北段\",\"number\":[],\"location\":\"104.066259,30.577801\",\"level\":\"门牌号\"},{\"formatted_address\":\"四川省成都市武侯区天府大道北段1089号\",\"country\":\"中国\",\"province\":\"四川省\",\"citycode\":\"028\",\"city\":\"成都市\",\"district\":\"武侯区\",\"township\":[],\"neighborhood\":{\"name\":[],\"type\":[]},\"building\":{\"name\":[],\"type\":[]},\"adcode\":\"510107\",\"street\":\"天府大道北段\",\"number\":[],\"location\":\"104.068436,30.580405\",\"level\":\"门牌号\"},{\"formatted_address\":\"四川省成都市武侯区天府大道中段1089号\",\"country\":\"中国\",\"province\":\"四川省\",\"citycode\":\"028\",\"city\":\"成都市\",\"district\":\"武侯区\",\"township\":[],\"neighborhood\":{\"name\":[],\"type\":[]},\"building\":{\"name\":[],\"type\":[]},\"adcode\":\"510107\",\"street\":\"天府大道中段\",\"number\":[],\"location\":\"104.069189,30.542141\",\"level\":\"门牌号\"}]}";
    private static final String MOCK_RESPONSE_FAILURE = "{\"status\":\"0\",\"info\":\"INVALID_USER_KEY\",\"infocode\":\"10001\"}";

    /**
     * 测试正常地理编码查询（地址转坐标）
     */
    @Test
    public void testGeoCodeSuccess() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody(MOCK_RESPONSE_SUCCESS));
            server.start();

            OkHttpClient client = new OkHttpClient.Builder().build();
            AmapGeoCoder geocoder = new AmapGeoCoder.Builder(TEST_API_KEY)
                    .httpClient(client)
                    .build();

            AmapGeoCoder.AmapGeoResponse response = geocoder.geoCode("成都市天府大道北段1089号");

            assertEquals("1", response.getStatus());
            assertEquals("OK", response.getInfo());
            assertEquals(3, response.getCount());
            assertNotNull(response.getGeocodes());
            assertEquals(3, response.getGeocodes().size());
            
            // 验证第一个结果
            AmapGeoCoder.AmapGeoResponse.Geocode firstResult = response.getGeocodes().get(0);
            assertEquals("104.066259,30.577801", firstResult.getLocation());
            assertEquals("四川省", firstResult.getProvince());
            assertEquals("成都市", firstResult.getCity());
            assertEquals("武侯区", firstResult.getDistrict());
            assertEquals("天府大道北段", firstResult.getStreet());
            assertNotNull(firstResult.getNumber());
            assertNotNull(firstResult.getNeighborhood());
            assertTrue(firstResult.getNeighborhood().getName().isEmpty());
            assertNotNull(firstResult.getBuilding());
            
            // 验证第二个结果
            AmapGeoCoder.AmapGeoResponse.Geocode secondResult = response.getGeocodes().get(1);
            assertEquals("104.068436,30.580405", secondResult.getLocation());
            
            // 验证第三个结果
            AmapGeoCoder.AmapGeoResponse.Geocode thirdResult = response.getGeocodes().get(2);
            assertEquals("天府大道中段", thirdResult.getStreet());
        }
    }

    /**
     * 测试带城市参数的地理编码查询
     */
    @Test
    public void testGeoCodeWithCity() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody(MOCK_RESPONSE_SUCCESS));
            server.start();

            OkHttpClient client = new OkHttpClient.Builder().build();
            AmapGeoCoder geocoder = new AmapGeoCoder.Builder(TEST_API_KEY)
                    .httpClient(client)
                    .build();

            AmapGeoCoder.AmapGeoResponse response = geocoder.geoCode("天府大道北段1089号", "成都市");

            assertNotNull(response.getGeocodes());
            assertEquals("成都市", response.getGeocodes().get(0).getCity());
        }
    }

    /**
     * 测试无效API密钥场景
     */
    @Test
    public void testGeoCodeInvalidApiKey() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody(MOCK_RESPONSE_FAILURE));
            server.start();

            OkHttpClient client = new OkHttpClient.Builder().build();
            AmapGeoCoder geocoder = new AmapGeoCoder.Builder(TEST_API_KEY)
                    .httpClient(client)
                    .build();

            AmapGeoCoder.AmapGeoResponse response = geocoder.geoCode("无效地址");

            assertEquals("0", response.getStatus());
            assertEquals("INVALID_USER_KEY", response.getInfo());
        }
    }

    /**
     * 测试网络异常处理
     */
    @Test
    public void testNetworkErrorHandling() {
        AmapGeoCoder geocoder = new AmapGeoCoder.Builder(TEST_API_KEY)
                .httpClient(new OkHttpClient.Builder().build())
                .build();

        assertThrows(Exception.class, () -> {
            geocoder.geoCode("测试地址");
        });
    }
}