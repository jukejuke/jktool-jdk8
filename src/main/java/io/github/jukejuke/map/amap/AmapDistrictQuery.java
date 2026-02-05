package io.github.jukejuke.map.amap;

//import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import lombok.Data;
import java.io.IOException;
import java.util.List;

/**
 * 高德地图行政区划查询工具类，用于通过高德地图API查询中国行政区划信息
 */
public class AmapDistrictQuery {
    /** HTTP客户端实例，用于发送API请求 */
    private final OkHttpClient client = new OkHttpClient();
    /** 高德地图API密钥 */
    private final String apiKey;
    //private String keywords;
    private Integer subdistrict;
    private String extensions;
    private String output = "json";

    private AmapDistrictQuery(Builder builder) {
        this.apiKey = builder.apiKey;
        //this.keywords = builder.keywords;
        this.subdistrict = builder.subdistrict;
        this.extensions = builder.extensions;
        this.output = builder.output;
    }

    /**
     * 构建AmapDistrictQuery实例的构建器类，用于设置查询参数
     */
    public static class Builder {
        private final String apiKey;
        //private String keywords;
        private Integer subdistrict;
        private String extensions;
        private String output;

        /**
         * 使用指定的API密钥创建Builder实例
         * @param apiKey 高德地图API密钥
         */
        public Builder(String apiKey) {
            this.apiKey = apiKey;
        }

//        public Builder keywords(String keywords) {
//            this.keywords = keywords;
//            return this;
//        }

        public Builder subdistrict(Integer subdistrict) {
            this.subdistrict = subdistrict;
            return this;
        }

        public Builder extensions(String extensions) {
            this.extensions = extensions;
            return this;
        }

        public Builder output(String output) {
            this.output = output;
            return this;
        }

        /**
         * 构建AmapDistrictQuery实例
         * @return 配置好的AmapDistrictQuery实例
         */
        public AmapDistrictQuery build() {
            return new AmapDistrictQuery(this);
        }
    }


    /**
     * 执行行政区划查询
     * @param keywords 查询关键词，可以是城市名、区域名等
     * @return 包含行政区划信息的DistrictResponse对象
     * @throws IOException 如果网络请求失败或响应解析错误时抛出
     */
    public DistrictResponse query(String keywords) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("https://restapi.amap.com/v3/config/district");
        urlBuilder.append("?key=").append(apiKey);
        if (keywords != null) urlBuilder.append("&keywords=").append(keywords);
        if (subdistrict != null) urlBuilder.append("&subdistrict=").append(subdistrict);
        if (extensions != null) urlBuilder.append("&extensions=").append(extensions);
        if (output != null) urlBuilder.append("&output=").append(output);

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return JSON.parseObject(responseBody, DistrictResponse.class);
        }
    }


    /**
     * 高德地图行政区划查询API的响应数据模型
     */
    @Data
    public static class DistrictResponse {
        private String status;
        private String info;
        private String infoCode;
        private String count;
        private Suggestion suggestion;
        private List<District> districts;


        /**
         * 查询建议信息，包含关键词和城市建议
         */
        @Data
        public static class Suggestion {
            private List<String> keywords;
            private List<String> cities;
        }



        /**
         * 行政区划信息模型
         */
        @Data
        public static class District {
            /** 城市编码 */
            private String citycode;
            /** 行政区划代码 */
            private String adCode;
            /** 行政区划名称 */
            private String name;
            /** 区域中心点坐标，格式为"经度,纬度" */
            private String center;
            /** 行政区划级别，可能的值：country（国家）、province（省）、city（市）、district（区/县） */
            private String level;
            private List<District> districts;
        }
    }
}