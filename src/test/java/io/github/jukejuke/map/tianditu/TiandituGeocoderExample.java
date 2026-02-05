package io.github.jukejuke.map.tianditu;

import okhttp3.OkHttpClient;

/**
 * 天地图地理编码使用示例
 * 演示如何使用TiandituGeocoder进行正向地理编码和逆地理编码
 */
public class TiandituGeocoderExample {

    public static void main(String[] args) {
        // 替换为您的天地图API密钥
        String apiKey = "";
        
        // 创建地理编码器实例
        TiandituGeocoder geocoder = new TiandituGeocoder.Builder(apiKey)
                .httpClient(new OkHttpClient())
                .build();
        
        try {
            // 示例1: 正向地理编码 - 通过地址获取坐标
            System.out.println("=== 正向地理编码示例 ===");
            String address = "江苏扬州仪征真州镇万年集贸市场";
            TiandituGeocoder.GeocodeResponse geocodeResponse = geocoder.geocode(address);

            if ("0".equals(geocodeResponse.getStatus())) {
                TiandituGeocoder.GeocodeResponse.Location location = geocodeResponse.getLocation();
                System.out.println("输入地址: " + address);
                System.out.println("解析地址: " + location.getKeyWord());
                System.out.println("经度: " + location.getLon());
                System.out.println("纬度: " + location.getLat());
                System.out.println("匹配级别: " + location.getLevel());
                System.out.println("匹配分数: " + location.getScore());
                System.out.println("搜索版本: " + geocodeResponse.getSearchVersion());
            } else {
                System.out.println("地理编码失败: " + geocodeResponse.getMsg());
            }
            
            // 示例2: 逆地理编码 - 通过坐标获取地址
            System.out.println("\n=== 逆地理编码示例 ===");
            //double longitude = 116.290158;
            //double latitude = 39.894696;
            double longitude = Double.parseDouble(geocodeResponse.getLocation().getLon());
            double latitude = Double.parseDouble(geocodeResponse.getLocation().getLat());
            TiandituGeocoder.TiandituResponse reverseResponse = geocoder.reverseGeocode(longitude, latitude);
            
            if ("0".equals(reverseResponse.getStatus())) {
                System.out.println("输入坐标: (" + longitude + ", " + latitude + ")");
                System.out.println("解析地址: " + reverseResponse.getResult().getFormatted_address());
                System.out.println("经度: " + reverseResponse.getResult().getLocation().getLon());
                System.out.println("纬度: " + reverseResponse.getResult().getLocation().getLat());
            } else {
                System.out.println("逆地理编码失败: " + reverseResponse.getMsg());
            }
            
        } catch (Exception e) {
            System.err.println("请求发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 正向地理编码方法调用示例
     * @param geocoder 地理编码器实例
     * @param address 要查询的地址
     * @return 地理编码结果
     */
    public static TiandituGeocoder.GeocodeResponse performGeocode(TiandituGeocoder geocoder, String address) {
        try {
            return geocoder.geocode(address);
        } catch (Exception e) {
            System.err.println("地理编码请求失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 逆地理编码方法调用示例
     * @param geocoder 地理编码器实例
     * @param longitude 经度
     * @param latitude 纬度
     * @return 逆地理编码结果
     */
    public static TiandituGeocoder.TiandituResponse performReverseGeocode(TiandituGeocoder geocoder, 
                                                                         double longitude, double latitude) {
        try {
            return geocoder.reverseGeocode(longitude, latitude);
        } catch (Exception e) {
            System.err.println("逆地理编码请求失败: " + e.getMessage());
            return null;
        }
    }
}