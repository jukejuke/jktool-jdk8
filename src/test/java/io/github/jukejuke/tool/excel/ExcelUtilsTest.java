package io.github.jukejuke.tool.excel;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * ExcelUtils 测试类
 * 测试 Excel 导出功能的各种场景
 */
public class ExcelUtilsTest {

    /**
     * 测试基本的 Excel 导出功能
     */
    @Test
    public void testExportBasic() {
        // 创建测试数据
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "张三", 25, "男"));
        userList.add(new User(2, "李四", 30, "女"));
        userList.add(new User(3, "王五", 35, "男"));

        // 创建表头映射
        Map<String, String> headers = new HashMap<>();
        headers.put("id", "ID");
        headers.put("name", "姓名");
        headers.put("age", "年龄");
        headers.put("gender", "性别");

        // 导出到字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ExcelUtils.export(userList, headers, "用户信息", outputStream);
            // 验证导出结果
            byte[] excelBytes = outputStream.toByteArray();
            assertNotNull(excelBytes);
            assertTrue(excelBytes.length > 0);
            System.out.println("Excel 导出成功，文件大小：" + excelBytes.length + " 字节");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Excel 导出失败：" + e.getMessage());
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试空数据列表的情况
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExportEmptyData() throws Exception {
        // 创建空数据列表
        List<User> userList = new ArrayList<>();

        // 创建表头映射
        Map<String, String> headers = new HashMap<>();
        headers.put("id", "ID");
        headers.put("name", "姓名");

        // 导出到字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExcelUtils.export(userList, headers, "用户信息", outputStream);
    }

    /**
     * 测试空表头映射的情况
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExportEmptyHeaders() throws Exception {
        // 创建测试数据
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "张三", 25, "男"));

        // 创建空表头映射
        Map<String, String> headers = new HashMap<>();

        // 导出到字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExcelUtils.export(userList, headers, "用户信息", outputStream);
    }

    /**
     * 测试空输出流的情况
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExportNullOutputStream() throws Exception {
        // 创建测试数据
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "张三", 25, "男"));

        // 创建表头映射
        Map<String, String> headers = new HashMap<>();
        headers.put("id", "ID");
        headers.put("name", "姓名");

        ExcelUtils.export(userList, headers, "用户信息", null);
    }

    /**
     * 测试使用注解配置的 Excel 导出功能
     */
    @Test
    public void testExportWithAnnotation() throws Exception {
        // 创建测试数据
        List<UserWithAnnotation> userList = new ArrayList<>();
        userList.add(new UserWithAnnotation(1, "张三", 25, "男", new java.util.Date()));
        userList.add(new UserWithAnnotation(2, "李四", 30, "女", new java.util.Date()));
        userList.add(new UserWithAnnotation(3, "王五", 35, null, new java.util.Date())); // 测试默认值

        // 导出到字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ExcelUtils.exportWithAnnotation(userList, "用户信息", outputStream);
            // 验证导出结果
            byte[] excelBytes = outputStream.toByteArray();
            assertNotNull(excelBytes);
            assertTrue(excelBytes.length > 0);
            System.out.println("使用注解配置的 Excel 导出成功，文件大小：" + excelBytes.length + " 字节");
        } catch (Exception e) {
            e.printStackTrace();
            fail("使用注解配置的 Excel 导出失败：" + e.getMessage());
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试导出到本地文件
     */
    @Test
    public void testExportToLocalFile() throws Exception {
        // 创建测试数据
        List<UserWithAnnotation> userList = new ArrayList<>();
        userList.add(new UserWithAnnotation(1, "张三", 25, "男", new java.util.Date()));
        userList.add(new UserWithAnnotation(2, "李四", 30, "女", new java.util.Date()));
        userList.add(new UserWithAnnotation(3, "王五", 35, null, new java.util.Date())); // 测试默认值

        // 创建本地文件路径（使用时间戳避免文件冲突）
        String fileName = "test_excel_export_" + System.currentTimeMillis() + ".xlsx";
        java.io.File file = new java.io.File(fileName);

        // 导出到本地文件
        try (java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file)) {
            ExcelUtils.exportWithAnnotation(userList, "用户信息", fileOutputStream);
            // 验证文件是否存在
            assertTrue("导出文件不存在", file.exists());
            // 验证文件大小是否大于0
            assertTrue("导出文件大小为0", file.length() > 0);
            System.out.println("Excel 导出到本地文件成功，文件路径：" + file.getAbsolutePath() + "，文件大小：" + file.length() + " 字节");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Excel 导出到本地文件失败：" + e.getMessage());
        } finally {
            // 清理测试文件
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("测试文件已清理");
                }
            }
        }
    }

    /**
     * 测试流式导出功能
     */
    @Test
    public void testExportWithAnnotationStreaming() throws Exception {
        // 创建测试数据
        List<UserWithAnnotation> userList = new ArrayList<>();
        // 添加1000条数据，测试流式导出的性能
        for (int i = 1; i <= 1000; i++) {
            userList.add(new UserWithAnnotation(i, "用户" + i, 20 + i % 30, i % 2 == 0 ? "男" : "女", new java.util.Date()));
        }

        // 创建本地文件路径（使用时间戳避免文件冲突）
        String fileName = "test_excel_streaming_export_" + System.currentTimeMillis() + ".xlsx";
        java.io.File file = new java.io.File(fileName);

        // 流式导出到本地文件
        try (java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file)) {
            long startTime = System.currentTimeMillis();
            ExcelUtils.exportWithAnnotationStreaming(userList, "用户信息", fileOutputStream);
            long endTime = System.currentTimeMillis();
            // 验证文件是否存在
            assertTrue("导出文件不存在", file.exists());
            // 验证文件大小是否大于0
            assertTrue("导出文件大小为0", file.length() > 0);
            System.out.println("Excel 流式导出成功，导出数据条数：" + userList.size() + "，耗时：" + (endTime - startTime) + "ms，文件大小：" + file.length() + " 字节");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Excel 流式导出失败：" + e.getMessage());
        } finally {
            // 清理测试文件
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("测试文件已清理");
                }
            }
        }
    }

    /**
     * 测试通过Supplier接口流式获取数据的导出功能
     */
    @Test
    public void testExportWithStream() throws Exception {
        // 创建本地文件路径（使用时间戳避免文件冲突）
        String fileName = "test_excel_stream_supplier_" + System.currentTimeMillis() + ".xlsx";
        java.io.File file = new java.io.File(fileName);

        // 模拟数据计数器
        final int[] counter = {0};
        final int totalCount = 100;

        // 流式导出到本地文件
        try {
            long startTime = System.currentTimeMillis();
            ExcelUtils.exportWithStream(fileName, () -> {
                // 模拟流式获取数据，当计数器达到总条数时返回null
                if (counter[0] >= totalCount) {
                    return null;
                }
                // 创建并返回数据对象
                UserWithAnnotation user = new UserWithAnnotation(
                        ++counter[0],
                        "用户" + counter[0],
                        20 + counter[0] % 30,
                        counter[0] % 2 == 0 ? "男" : "女",
                        new java.util.Date()
                );
                return user;
            });
            long endTime = System.currentTimeMillis();
            // 验证文件是否存在
            assertTrue("导出文件不存在", file.exists());
            // 验证文件大小是否大于0
            assertTrue("导出文件大小为0", file.length() > 0);
            System.out.println("Excel 流式获取数据导出成功，导出数据条数：" + totalCount + "，耗时：" + (endTime - startTime) + "ms，文件大小：" + file.length() + " 字节");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Excel 流式获取数据导出失败：" + e.getMessage());
        } finally {
            // 清理测试文件
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("测试文件已清理");
                }
            }
        }
    }

    /**
     * 测试通过Supplier接口流式获取数据并导出到输出流的功能
     */
    @Test
    public void testExportWithStreamToOutputStream() throws Exception {
        // 创建本地文件路径（使用时间戳避免文件冲突）
        String fileName = "test_excel_stream_outputstream_" + System.currentTimeMillis() + ".xlsx";
        java.io.File file = new java.io.File(fileName);

        // 模拟数据计数器
        final int[] counter = {0};
        final int totalCount = 50;

        // 流式导出到本地文件
        try (java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file)) {
            long startTime = System.currentTimeMillis();
            ExcelUtils.exportWithStream(fileOutputStream, () -> {
                // 模拟流式获取数据，当计数器达到总条数时返回null
                if (counter[0] >= totalCount) {
                    return null;
                }
                // 创建并返回数据对象
                UserWithAnnotation user = new UserWithAnnotation(
                        ++counter[0],
                        "用户" + counter[0],
                        20 + counter[0] % 30,
                        counter[0] % 2 == 0 ? "男" : "女",
                        new java.util.Date()
                );
                return user;
            });
            long endTime = System.currentTimeMillis();
            // 验证文件是否存在
            assertTrue("导出文件不存在", file.exists());
            // 验证文件大小是否大于0
            assertTrue("导出文件大小为0", file.length() > 0);
            System.out.println("Excel 流式获取数据导出到输出流成功，导出数据条数：" + totalCount + "，耗时：" + (endTime - startTime) + "ms，文件大小：" + file.length() + " 字节");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Excel 流式获取数据导出到输出流失败：" + e.getMessage());
        } finally {
            // 清理测试文件
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("测试文件已清理");
                }
            }
        }
    }

    /**
     * 测试通过Consumer接口流式处理Excel数据的导入功能
     */
    @Test
    public void testImportWithStream() throws Exception {
        // 创建测试数据
        List<UserWithAnnotation> userList = new ArrayList<>();
        userList.add(new UserWithAnnotation(1, "张三", 25, "男", new java.util.Date()));
        userList.add(new UserWithAnnotation(2, "李四", 30, "女", new java.util.Date()));
        userList.add(new UserWithAnnotation(3, "王五", 35, "男", new java.util.Date()));

        // 先导出到文件
        String fileName = "test_excel_import_stream_" + System.currentTimeMillis() + ".xlsx";
        java.io.File file = new java.io.File(fileName);

        try (java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file)) {
            ExcelUtils.exportWithAnnotation(userList, "用户信息", fileOutputStream);
        }

        // 再通过流式方式导入
        final List<UserWithAnnotation> importedList = new ArrayList<>();
        try (java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file)) {
            ExcelUtils.importWithStream(fileInputStream, UserWithAnnotation.class, user -> {
                importedList.add(user);
                System.out.println("处理数据：" + user.getName());
            });
            // 验证导入的数据条数
            assertEquals("导入的数据条数与导出的不一致", userList.size(), importedList.size());
            System.out.println("Excel 流式导入成功，导入数据条数：" + importedList.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Excel 流式导入失败：" + e.getMessage());
        } finally {
            // 清理测试文件
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("测试文件已清理");
                }
            }
        }
    }

    /**
     * 测试导入功能
     */
    @Test
    public void testImportFromExcel() throws Exception {
        // 创建测试数据
        List<UserWithAnnotation> userList = new ArrayList<>();
        userList.add(new UserWithAnnotation(1, "张三", 25, "男", new java.util.Date()));
        userList.add(new UserWithAnnotation(2, "李四", 30, "女", new java.util.Date()));
        userList.add(new UserWithAnnotation(3, "王五", 35, "男", new java.util.Date()));

        // 先导出到文件
        String fileName = "test_excel_import_" + System.currentTimeMillis() + ".xlsx";
        java.io.File file = new java.io.File(fileName);

        try (java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file)) {
            ExcelUtils.exportWithAnnotation(userList, "用户信息", fileOutputStream);
        }

        // 再从文件导入
        try (java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file)) {
            List<UserWithAnnotation> importedList = ExcelUtils.importFromExcel(fileInputStream, UserWithAnnotation.class);
            // 验证导入的数据条数
            assertEquals("导入的数据条数与导出的不一致", userList.size(), importedList.size());
            System.out.println("Excel 导入成功，导入数据条数：" + importedList.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Excel 导入失败：" + e.getMessage());
        } finally {
            // 清理测试文件
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("测试文件已清理");
                }
            }
        }
    }

    /**
     * 测试用户实体类
     */
    static class User {
        private int id;
        private String name;
        private int age;
        private String gender;

        public User(int id, String name, int age, String gender) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

    /**
     * 测试使用注解配置的用户实体类
     */
    static class UserWithAnnotation {
        @ExcelColumn(name = "用户ID", order = 1, width = 10, alignment = ExcelAlignment.CENTER)
        private int id;
        
        @ExcelColumn(name = "姓名", order = 2, width = 15)
        private String name;
        
        @ExcelColumn(name = "年龄", order = 3, width = 8, alignment = ExcelAlignment.RIGHT)
        private int age;
        
        @ExcelColumn(name = "性别", order = 4, width = 8, defaultValue = "未知")
        private String gender;
        
        @ExcelColumn(name = "注册时间", order = 5, width = 20, format = "yyyy-MM-dd HH:mm:ss", alignment = ExcelAlignment.CENTER)
        private java.util.Date registerTime;

        public UserWithAnnotation() {
        }

        public UserWithAnnotation(int id, String name, int age, String gender, java.util.Date registerTime) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.registerTime = registerTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public java.util.Date getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(java.util.Date registerTime) {
            this.registerTime = registerTime;
        }
    }
}
