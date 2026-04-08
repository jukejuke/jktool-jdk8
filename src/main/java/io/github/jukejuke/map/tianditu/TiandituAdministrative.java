package io.github.jukejuke.map.tianditu;

//import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import lombok.Data;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 天地图行政区划服务工具类
 * 封装天地图API的行政区划查询功能
 * 使用{@link Builder}类创建实例以支持灵活配置
 */
public class TiandituAdministrative {
    private final String apiKey;
    private final OkHttpClient httpClient;
    private final String baseUrl = "http://api.tianditu.gov.cn/v2/administrative";

    /**
     * 私有构造函数，通过Builder创建实例
     */
    private TiandituAdministrative(Builder builder) {
        this.apiKey = builder.apiKey;
        this.httpClient = builder.httpClient != null ? builder.httpClient : new OkHttpClient();
    }

    /**
     * 构建者模式，用于灵活配置TiandituAdministrative实例
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

        public TiandituAdministrative build() {
            return new TiandituAdministrative(this);
        }
    }

    /**
     * 执行行政区划查询
     * @param keyword 关键字（行政区划代码或名称）
     * @param childLevel 子级级别（0-3）
     * @param extensions 是否返回扩展信息
     * @return 行政区划响应结果
     * @throws Exception 可能抛出的异常
     */
    public TiandituAdministrativeResponse queryAdministrative(String keyword, int childLevel, boolean extensions) throws Exception {
        // 构建请求URL
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name());
        String requestUrl = String.format("%s?keyword=%s&childLevel=%d&extensions=%s&tk=%s",
                baseUrl, encodedKeyword, childLevel, extensions, apiKey);

        // 创建HTTP请求
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        // 执行请求并处理响应
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            return JSON.parseObject(responseBody, TiandituAdministrativeResponse.class);
        }
    }

    /**
     * 执行行政区划查询（使用默认参数：childLevel=0, extensions=true）
     * @param keyword 关键字（行政区划代码或名称）
     * @return 行政区划响应结果
     * @throws Exception 可能抛出的异常
     */
    public TiandituAdministrativeResponse queryAdministrative(String keyword) throws Exception {
        return queryAdministrative(keyword, 0, true);
    }

    /**
     * 天地图行政区划API响应结果封装类
     */
    @Data
    public static class TiandituAdministrativeResponse {
        private int status;
        private String message;
        private Data1 data;

        @Data
        public static class Data1 {
            private String[] suggestion;
            private District[] district;
        }

        @Data
        public static class District {
            private String gb;
            private String pgb;
            private String name;
            private String boundary;
            private List<Coordinate> boundaryList;
            private Center center;
            private int level;

            /**
             * 获取解析后的边界坐标列表
             * 取第一个多边形，每个坐标点为 Coordinate 对象
             * @return 边界坐标列表
             */
            public List<Coordinate> getBoundaryList() {
                if (boundaryList != null) {
                    return boundaryList;
                }
                boundaryList = new ArrayList<>();
                if (boundary == null || boundary.isEmpty()) {
                    return boundaryList;
                }

                try {
                    // 解析 MULTIPOLYGON 格式
                    String wkt = boundary.trim();
                    if (wkt.startsWith("MULTIPOLYGON")) {
                        // 移除 MULTIPOLYGON 关键字
                        String content = wkt.substring("MULTIPOLYGON".length()).trim();

                        // 找到第一个多边形的开始和结束位置
                        int firstOpenParen = content.indexOf('(');
                        int secondOpenParen = content.indexOf('(', firstOpenParen + 1);

                        if (secondOpenParen != -1) {
                            // 从第二个 '(' 开始找对应的 ')'
                            int depth = 1;
                            int closeParen = -1;
                            for (int i = secondOpenParen + 1; i < content.length(); i++) {
                                if (content.charAt(i) == '(') {
                                    depth++;
                                } else if (content.charAt(i) == ')') {
                                    depth--;
                                    if (depth == 0) {
                                        closeParen = i;
                                        break;
                                    }
                                }
                            }

                            if (closeParen != -1) {
                                // 提取第一个多边形的坐标内容
                                String polygonContent = content.substring(secondOpenParen + 1, closeParen).trim();

                                // 移除可能的前导括号
                                polygonContent = polygonContent.replaceAll("^\\s*\\(\\s*", "");

                                // 分割坐标点
                                String[] pointStrings = polygonContent.split(",");
                                for (String pointStr : pointStrings) {
                                    pointStr = pointStr.trim();
                                    // 移除坐标点前后的括号
                                    pointStr = pointStr.replaceAll("^\\s*\\(+\\s*", "").replaceAll("\\s*\\)+\\s*$", "");
                                    if (pointStr.isEmpty()) {
                                        continue;
                                    }

                                    String[] coords = pointStr.split("\\s+");
                                    if (coords.length >= 2) {
                                        try {
                                            double lng = Double.parseDouble(coords[0].trim());
                                            double lat = Double.parseDouble(coords[1].trim());
                                            boundaryList.add(new Coordinate(lng, lat));
                                        } catch (Exception e) {
                                            // 忽略无法解析的点
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // 解析失败时返回空列表
                }

                return boundaryList;
            }
        }

        /**
         * 经纬度坐标对象
         */
        @Data
        public static class Coordinate {
            private double lng;
            private double lat;

            public Coordinate() {
            }

            public Coordinate(double lng, double lat) {
                this.lng = lng;
                this.lat = lat;
            }
        }

        @Data
        public static class Center {
            private double lng;
            private double lat;
        }
    }
}