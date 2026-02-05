# JK Tool

JK Tool is a Java-based utility library primarily designed to convert between address information and latitude/longitude coordinates using the geocoding and reverse geocoding services of Amap (Gaode Map) and Tianditu. This project is suitable for Java applications requiring integration with geocoding services.

## Features

### Geocoding Services
- **Amap Reverse Geocoding**: Implemented via the `AmapRegeoCoder` class, supporting conversion of coordinates into structured address information.
- **Amap Geocoding**: Implemented via the `AmapGeoCoder` class, supporting conversion of address information into coordinates.
- **Amap District Query**: Implemented via the `AmapDistrictQuery` class, supporting area information query based on keywords.
- **Amap Coordinate Conversion**: Implemented via the `AmapCoordinateConverter` class, supporting coordinate conversion through API.
- **Tianditu Reverse Geocoding**: Implemented via the `TiandituGeocoder` class, supporting conversion of coordinates into structured address information and address information into coordinates.
- **Tianditu Administrative Area Query**: Implemented via the `TiandituAdministrative` class, supporting administrative area information query.

### Coordinate Conversion Tools
- **Local Coordinate Conversion**: Implemented via the `CoordinateConverter` class, supporting local conversion between WGS-84, GCJ-02, and BD-09 coordinate systems.
  - **WGS-84 to GCJ-02**: Convert GPS original coordinates to Mars coordinate system, supporting high-precision conversion algorithm.
  - **GCJ-02 to WGS-84**: Restore Mars coordinates to GPS original coordinates, suitable for precise positioning requirements.
  - **GCJ-02 to BD-09**: Convert Mars coordinates (Amap/Tencent Map) to Baidu coordinate system.
  - **BD-09 to GCJ-02**: Convert Baidu coordinate system to Mars coordinate system.
  - **WGS-84 to BD-09**: Directly convert GPS coordinates to Baidu coordinate system.
  - **BD-09 to WGS-84**: Directly restore Baidu coordinates to GPS coordinates.

### General Utility Classes
- **HTTP Request Tool**: Implemented via the `HttpUtil` class, supporting common HTTP request methods such as GET, POST, and POST JSON.
- **String Processing**: Implemented via the `StringUtils` class, providing common operations like string null check, splitting, joining, replacement, and formatting.
- **File Operations**: Implemented via the `FileUtils` class, supporting file creation, reading, writing, deletion, and directory creation and traversal operations.
- **Configuration File Processing**: Implemented via the `PropertiesUtils` class, supporting reading, modifying, and saving properties configuration files from classpath or file system.
- **Bean Field Filtering**: Implemented via the `BeanFieldFilter` class, supporting flexible filtering of fields in Java Beans.
- **Bean Property Operations**: Implemented via the `BeanPropertyUtils` class, providing tools for reading and setting Bean properties.
- **Date and Time Processing**: Implemented via the `DateUtils` class, supporting date formatting, conversion, and calculation functions.
- **Internet Time Synchronization**: Implemented via the `InternetTimeUtils` class, supporting obtaining standard time from internet time servers.
- **Freemarker Templates**: Implemented via the `FreemarkerUtils` class, simplifying template rendering and data binding operations.
- **JWT Token Processing**: Implemented via the `JwtUtils` class, supporting JWT token generation, parsing, and verification functions.

### Encryption and Decryption Tools
- **Security Utility Class**: Implemented via the `SecureUtil` class, providing AES/DES symmetric encryption, MD5/SHA hash algorithms, HMAC message authentication codes, UUID generation, and other security-related functions.
- **AES Encryption and Decryption**: Implemented via the `AESUtil` class, providing complete implementation of AES symmetric encryption algorithm, supporting multiple modes and padding methods.
- **RSA Encryption and Decryption**: Implemented via the `RSAUtil` class, providing RSA asymmetric encryption algorithm key generation, encryption, decryption, signature, and verification functions.
- **DSA Digital Signature**: Implemented via the `DSAUtil` and `DSAKeyExportImport` classes, providing DSA digital signature algorithm key generation, signature, and verification functions.
- **Digest Algorithms**: Implemented via the `DigestUtil` class, supporting common digest algorithms such as MD5, SHA-1, SHA-256, and SHA-512.

### System Utility Classes
- **Hosts File Management**: Implemented via the `HostsFileManager` class, supporting Hosts file reading, editing, and backup functions.
- **DNS Resolution Tools**: Implemented via the `DnsResolver`, `ProxyDnsResolver`, `DoHQuery`, and `DoHWithHttpProxy` classes, supporting DNS query and DNS over HTTPS functions.
- **IP Address Processing**: Implemented via the `IPAddressResolver` and `ProxyIPAddressResolver` classes, supporting IP address resolution and proxy IP identification.
- **Process Management**: Implemented via the `WindowExeProcessManager` class, supporting process startup, query, and management under Windows systems.
- **Logging Tools**: Implemented via the `LogUtil` class, providing concise logging functionality.

### License Management
- **Hardware Information Acquisition**: Implemented via the `HardwareUtils` class, supporting acquisition of computer hardware identification information.
- **License Generation**: Implemented via the `LicenseUtils` class, supporting generation of software licenses based on hardware information.
- **License Verification**: Implemented via the `LicenseInfo` class, supporting verification and parsing of license information.

### API Response Encapsulation
- **Unified Response Result**: Implemented via the `ApiResponse<T>` class, providing a unified API response format, including status code, message, and data.
- **Response Status Codes**: Implemented via the `ApiCode` enum class, defining HTTP standard status codes and business custom status codes.
- **Pagination Response Encapsulation**: Implemented via the `PageResponse<T>` class, encapsulating pagination query results, including total records, records per page, current page number, total pages, and data list.
- **Convenient Static Methods**: Providing `success()`, `fail()`, and other static methods to simplify API response creation.
- **Generic Support**: Supporting generic data types, suitable for various API return data scenarios.
- **Support for Pagination Queries**: Integrating pagination functionality to facilitate processing of large data pagination query results.

## Usage

### Add Dependencies

Ensure your `pom.xml` file includes the required dependencies, such as `OkHttpClient` and other necessary libraries.

### Initialize Reverse Geocoders

#### Amap

```java
AmapRegeoCoder amapRegeoCoder = new AmapRegeoCoder.Builder("your_api_key").build();
AmapResponse response = amapRegeoCoder.reverseGeocode(116.397428, 39.90923);
```

#### Tianditu

```java
TiandituGeocoder tiandituGeocoder = new TiandituGeocoder.Builder("your_api_key").build();
TiandituResponse response = tiandituGeocoder.reverseGeocode(116.397428, 39.90923);
```

### Initialize Geocoder (Amap)

```java
AmapGeoCoder amapGeoCoder = new AmapGeoCoder.Builder("your_api_key").build();
AmapGeoResponse response = amapGeoCoder.geoCode("Beijing");
```

### Initialize District Query (Amap)

```java
AmapDistrictQuery districtQuery = new AmapDistrictQuery.Builder("your_api_key").build();
DistrictResponse response = districtQuery.query("Beijing");
```

### Initialize Administrative Area Query (Tianditu)

```java
TiandituAdministrative administrative = new TiandituAdministrative.Builder("your_api_key").build();
TiandituAdministrativeResponse response = administrative.queryAdministrative("Beijing");
```

### Local Coordinate Conversion

#### Using CoordinateConverter Class

```java
// Create coordinate point
CoordinateConverter.Point wgs84Point = new CoordinateConverter.Point(116.397428, 39.90923); // Tiananmen Square, Beijing

// WGS-84 to GCJ-02
CoordinateConverter.Point gcj02Point = CoordinateConverter.wgs84ToGcj02(wgs84Point);
System.out.println("GCJ-02 Coordinates: " + gcj02Point);

// GCJ-02 to BD-09
CoordinateConverter.Point bd09Point = CoordinateConverter.gcj02ToBd09(gcj02Point);
System.out.println("BD-09 Coordinates: " + bd09Point);

// BD-09 to GCJ-02
CoordinateConverter.Point backToGcj02 = CoordinateConverter.bd09ToGcj02(bd09Point);
System.out.println("Back to GCJ-02: " + backToGcj02);

// GCJ-02 to WGS-84
CoordinateConverter.Point backToWgs84 = CoordinateConverter.gcj02ToWgs84(backToGcj02);
System.out.println("Back to WGS-84: " + backToWgs84);
```

#### Common City Coordinate Conversion Examples

```java
// Beijing
CoordinateConverter.Point beijing = new CoordinateConverter.Point(116.397428, 39.90923);
CoordinateConverter.Point beijingGcj02 = CoordinateConverter.wgs84ToGcj02(beijing);
CoordinateConverter.Point beijingBd09 = CoordinateConverter.gcj02ToBd09(beijingGcj02);

// Shanghai
CoordinateConverter.Point shanghai = new CoordinateConverter.Point(121.4737, 31.2304);
CoordinateConverter.Point shanghaiGcj02 = CoordinateConverter.wgs84ToGcj02(shanghai);
CoordinateConverter.Point shanghaiBd09 = CoordinateConverter.gcj02ToBd09(shanghaiGcj02);

// Guangzhou
CoordinateConverter.Point guangzhou = new CoordinateConverter.Point(113.2644, 23.1291);
CoordinateConverter.Point guangzhouGcj02 = CoordinateConverter.wgs84ToGcj02(guangzhou);
CoordinateConverter.Point guangzhouBd09 = CoordinateConverter.gcj02ToBd09(guangzhouGcj02);
```

#### Batch Conversion Processing

```java
public void batchConvertCoordinates(List<CoordinateConverter.Point> wgs84Points) {
    for (CoordinateConverter.Point wgs84 : wgs84Points) {
        CoordinateConverter.Point gcj02 = CoordinateConverter.wgs84ToGcj02(wgs84);
        CoordinateConverter.Point bd09 = CoordinateConverter.gcj02ToBd09(gcj02);
        
        System.out.printf("Original WGS-84: %s -> GCJ-02: %s -> BD-09: %s%n", 
                          wgs84, gcj02, bd09);
    }
}
```

### Parse Response Data

You can easily obtain structured address or area information from the returned `AmapResponse`, `AmapGeoResponse`, `DistrictResponse`, `TiandituResponse`, or `TiandituAdministrativeResponse` objects.

## Project Structure

### Geocoding Services
- `src/main/java/io/github/jukejuke/map/amap/AmapRegeoCoder.java`: Implementation of Amap reverse geocoding.
- `src/main/java/io/github/jukejuke/map/amap/AmapGeoCoder.java`: Implementation of Amap geocoding.
- `src/main/java/io/github/jukejuke/map/amap/AmapDistrictQuery.java`: Implementation of Amap district query.
- `src/main/java/io/github/jukejuke/map/amap/AmapCoordinateConverter.java`: Implementation of Amap API coordinate conversion.
- `src/main/java/io/github/jukejuke/map/tianditu/TiandituGeocoder.java`: Implementation of Tianditu reverse geocoding.
- `src/main/java/io/github/jukejuke/map/tianditu/TiandituAdministrative.java`: Implementation of Tianditu administrative area query.

### Coordinate Conversion Tools
- `src/main/java/io/github/jukejuke/map/util/CoordinateConverter.java`: Local coordinate conversion utility class, supporting conversion between WGS-84, GCJ-02, and BD-09 coordinate systems.
- `src/test/java/io/github/jukejuke/map/util/CoordinateConverterTest.java`: Complete test cases for coordinate conversion utility class.

### API Response Encapsulation
- `src/main/java/io/github/jukejuke/api/ApiResponse.java`: Unified API response result encapsulation class.
- `src/main/java/io/github/jukejuke/api/ApiCode.java`: API response status code enum class.
- `src/main/java/io/github/jukejuke/api/PageResponse.java`: Pagination response result encapsulation class.

### General Utility Classes
- `src/main/java/io/github/jukejuke/tool/bean/BeanFieldFilter.java`: Bean field filtering utility class.
- `src/main/java/io/github/jukejuke/tool/bean/BeanPropertyUtils.java`: Bean property operation utility class.
- `src/main/java/io/github/jukejuke/tool/date/DateUtils.java`: Date and time processing utility class.
- `src/main/java/io/github/jukejuke/tool/freemarker/FreemarkerUtils.java`: Freemarker template engine utility class.
- `src/main/java/io/github/jukejuke/tool/jwt/JwtUtils.java`: JWT token processing utility class.

### Maven
Add the following to your project's pom.xml dependencies:

```xml
<dependency>
    <groupId>io.github.jukejuke</groupId>
    <artifactId>jk-tool</artifactId>
    <version>0.0.2</version>
</dependency>
```

### Gradle
```
implementation 'io.github.jukejuke:jk-tool:0.0.2'
```

### Download JAR

Click the link below to download `jk-tool-X.X.X.jar`:

- [Maven Central Repository](https://repo1.maven.org/maven2/io/github/jukejuke/jk-tool/0.0.2/)

## JDK Version

- **JDK 11 or higher**

## License

This project is licensed under the Apache 2.0 License. For details, see the [LICENSE](LICENSE) file.

## Contribution

Contributions to this project are welcome. Please follow these steps:

1. Fork this project.
2. Create a new branch.
3. Submit a Pull Request.