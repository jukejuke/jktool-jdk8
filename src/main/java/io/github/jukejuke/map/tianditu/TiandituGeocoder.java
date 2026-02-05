package io.github.jukejuke.map.tianditu;

//import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import lombok.Data;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 天地图地理编码工具类
 * 封装天地图API的正向地理编码（地址转坐标）和逆地理编码查询功能
 * 使用{@link Builder}类创建实例以支持灵活配置
 */
public class TiandituGeocoder {
    private final String apiKey;
    private final OkHttpClient httpClient;
    private final String baseUrl = "http://api.tianditu.gov.cn/geocoder";

    /**
     * 私有构造函数，通过Builder创建实例
     */
    private TiandituGeocoder(Builder builder) {
        this.apiKey = builder.apiKey;
        this.httpClient = builder.httpClient != null ? builder.httpClient : new OkHttpClient();
    }

    /**
     * 构建者模式，用于灵活配置TiandituGeocoder实例
     */
    public static class Builder {
        private final String apiKey;
        private OkHttpClient httpClient;

        public Builder(String apiKey) {
            this.apiKey = apiKey;
        }

        public Builder httpClient(OkHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public TiandituGeocoder build() {
            return new TiandituGeocoder(this);
        }
    }

    /**
     * 执行正向地理编码（地址转坐标）
     * @param keyWord 地址关键词
     * @return 地理编码响应结果
     * @throws Exception 可能抛出的异常
     */
    public GeocodeResponse geocode(String keyWord) throws Exception {
        // 构建请求参数
        Map<String, Object> dsMap = new HashMap<>();
        dsMap.put("keyWord", keyWord);
        
        // 转换为JSON并URL编码
        String dsJson = JSON.toJSONString(dsMap);
        String encodedDs = URLEncoder.encode(dsJson, StandardCharsets.UTF_8.name());

        // 构建完整URL
        String requestUrl = String.format("%s?ds=%s&tk=%s", this.baseUrl, encodedDs, apiKey);

        // 创建HTTP请求
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        // 执行请求并处理响应
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            return JSON.parseObject(responseBody, GeocodeResponse.class);
        }
    }

    /**
     * 执行逆地理编码查询（通过经纬度获取地址）
     * @param longitude 经度
     * @param latitude 纬度
     * @return 地理编码响应结果
     * @throws Exception 可能抛出的异常
     */
    public TiandituResponse reverseGeocode(double longitude, double latitude) throws Exception {
        // 构建请求参数
        Map<String, Object> postStrMap = new HashMap<String, Object>();
        postStrMap.put("lon", longitude);
        postStrMap.put("lat", latitude);
        postStrMap.put("ver", 1);
        
        // 转换为JSON并URL编码
        String postStrJson = JSON.toJSONString(postStrMap);
        String encodedPostStr = URLEncoder.encode(postStrJson, StandardCharsets.UTF_8.name());

        // 构建完整URL
        String requestUrl = String.format("%s?postStr=%s&type=geocode&tk=%s",
                this.baseUrl, encodedPostStr, apiKey);

        // 创建HTTP请求
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        // 执行请求并处理响应
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            return JSON.parseObject(responseBody, TiandituResponse.class);
        }
    }

    /**
     * 天地图正向地理编码API响应结果封装类
     */
    @Data
    public static class GeocodeResponse {
        private String msg;
        private Location location;
        private String searchVersion;
        private String status;

        @Data
        public static class Location {
            private int score;
            private String level;
            private String lon;
            private String lat;
            private String keyWord;
        }
    }

    /**
     * 天地图逆地理编码API响应结果封装类
     */
    @Data
    public static class TiandituResponse {
        private String msg;
        private String status;
        private Result result;

        @Data
        public static class Result {
            private String formatted_address;
            private Location location;
            private AddressComponent addressComponent;
        }

        @Data
        public static class Location {
            private double lon;
            private double lat;
        }

        @Data
        public static class AddressComponent {
            private String address;
            private String town;
            private String nation;
            private String city;
            private String county_code;
            private String poi_position;
            private String county;
            private String city_code;
            private String address_position;
            private String poi;
            private String province_code;
            private String town_code;
            private String province;
            private String road;
            private int road_distance;
            private int address_distance;
            private int poi_distance;
        }
    }
}