package io.github.jukejuke.map.amap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * 高德地图逆地理编码工具测试类
 */
public class AmapRegeoCoderTest {
    private static final String TEST_API_KEY = "";
    private static final String MOCK_RESPONSE_SUCCESS = "{\"status\":\"1\",\"regeocode\":{\"addressComponent\":{\"city\":\"成都市\",\"province\":\"四川省\",\"adcode\":\"510107\",\"district\":\"武侯区\",\"towncode\":\"510107021000\",\"streetNumber\":{\"number\":\"1089号\",\"location\":\"104.068436,30.580405\",\"direction\":\"西\",\"distance\":\"156.335\",\"street\":\"天府大道北段\"},\"country\":\"中国\",\"township\":\"桂溪街道\",\"businessAreas\":[{\"location\":\"104.070316,30.570218\",\"name\":\"交子商圈\",\"id\":\"510107\"},{\"location\":\"104.036956,30.585228\",\"name\":\"石羊场\",\"id\":\"510107\"},{\"location\":\"104.052800,30.582150\",\"name\":\"金融城央双地铁全业态商业街区\",\"id\":\"510107\"}]},\"formatted_address\":\"四川省成都市武侯区桂溪街道交子金融广场(建设中)\"},\"info\":\"OK\",\"infocode\":\"10000\"}";
    private static final String MOCK_RESPONSE_FAILURE = "{\"status\":\"0\",\"info\":\"INVALID_USER_KEY\",\"infocode\":\"10001\"}";

    /**
     * 测试正常逆地理编码查询
     */
    @Test
    public void testReverseGeocodeSuccess() throws Exception {
        // 创建MockWebServer
        try (MockWebServer server = new MockWebServer()) {
            // 配置Mock响应
            server.enqueue(new MockResponse().setBody(MOCK_RESPONSE_SUCCESS));
            server.start();

            // 创建自定义OkHttpClient指向Mock服务器
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            // 创建测试对象
            AmapRegeoCoder geocoder = new AmapRegeoCoder.Builder(TEST_API_KEY)
                    .httpClient(client)
                    .build();

            // 执行测试
            AmapRegeoCoder.AmapResponse response = geocoder.reverseGeocode(104.07, 30.58);

            // 验证结果
            assertEquals("1", response.getStatus());
            assertEquals("OK", response.getInfo());
            assertNotNull(response.getRegeocode());
            assertEquals("四川省成都市武侯区桂溪街道交子金融广场(建设中)", response.getRegeocode().getFormattedAddress());
            assertEquals("四川省", response.getRegeocode().getAddressComponent().getProvince());
            assertEquals("成都市", response.getRegeocode().getAddressComponent().getCity());
            assertEquals("武侯区", response.getRegeocode().getAddressComponent().getDistrict());
            assertEquals("510107", response.getRegeocode().getAddressComponent().getAdcode());
            assertNotNull(response.getRegeocode().getAddressComponent().getStreetNumber());
            assertEquals("1089号", response.getRegeocode().getAddressComponent().getStreetNumber().getNumber());
            assertEquals("天府大道北段", response.getRegeocode().getAddressComponent().getStreetNumber().getStreet());
            assertNotNull(response.getRegeocode().getAddressComponent().getBusinessAreas());
            assertEquals(3, response.getRegeocode().getAddressComponent().getBusinessAreas().size());
            assertEquals("交子商圈", response.getRegeocode().getAddressComponent().getBusinessAreas().get(0).getName());
        }
    }

    /**
     * 测试API密钥无效场景
     */
    @Test
    public void testReverseGeocodeInvalidApiKey() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody(MOCK_RESPONSE_FAILURE));
            server.start();

            OkHttpClient client = new OkHttpClient.Builder().build();
            AmapRegeoCoder geocoder = new AmapRegeoCoder.Builder(TEST_API_KEY)
                    .httpClient(client)
                    .build();

            AmapRegeoCoder.AmapResponse response = geocoder.reverseGeocode(104.07, 30.58);

            assertEquals("0", response.getStatus());
            assertEquals("INVALID_USER_KEY", response.getInfo());
            assertNull(response.getRegeocode());
        }
    }

    /**
     * 测试网络异常处理
     */
    @Test
    public void testNetworkErrorHandling() {
        // 创建无法连接的服务器地址
        AmapRegeoCoder geocoder = new AmapRegeoCoder.Builder(TEST_API_KEY)
                .httpClient(new OkHttpClient.Builder().build())
                .build();

        // 验证网络异常时是否抛出异常
        assertThrows(Exception.class, () -> {
            geocoder.reverseGeocode(116.481028, 39.921983);
        });
    }
}