package io.github.jukejuke.tool.exec;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

/**
 * LinuxExeProcessManager测试类
 * 注意：此测试需要在Linux环境下运行
 */
public class LinuxExeProcessManagerTest {

    /**
     * 测试获取进程ID方法
     */
    @Test
    void testFindPid() throws IOException {
        // 在Linux环境下测试findPid方法
        LinuxExeProcessManager processManager = new LinuxExeProcessManager("ps", "java");
        
        // 由于在Windows环境下无法测试，这里只做简单的空指针检查
        assertNotNull(processManager);
        
        // 注意：实际测试需要在Linux环境下运行
        // String pid = processManager.findPid("java");
        // System.out.println("Java进程PID: " + pid);
    }
    
    /**
     * 测试构建命令方法
     */
    @Test
    void testBuildCommand() {
        String executablePath = "/bin/echo";
        String[] args = {"hello", "world"};
        LinuxExeProcessManager processManager = new LinuxExeProcessManager(executablePath, "echo", args);
        
        // 通过反射调用buildCommand方法
        try {
            java.lang.reflect.Method method = LinuxExeProcessManager.class.getDeclaredMethod("buildCommand");
            method.setAccessible(true);
            String[] command = (String[]) method.invoke(processManager);
            
            assertNotNull(command);
            assertEquals(3, command.length);
            assertEquals(executablePath, command[0]);
            assertEquals("hello", command[1]);
            assertEquals("world", command[2]);
        } catch (Exception e) {
            e.printStackTrace();
            fail("反射调用buildCommand方法失败: " + e.getMessage());
        }
    }
}