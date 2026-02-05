package io.github.jukejuke.tool.exec;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

/**
 * ProcessManagerFactory测试类
 */
public class ProcessManagerFactoryTest {

    /**
     * 测试创建ProcessManager实例
     */
    @Test
    void testCreateProcessManager() {
        String executablePath = "/bin/echo";
        String processName = "echo";
        String[] args = {"test"};
        
        // 测试创建进程管理器
        ProcessManager processManager = ProcessManagerFactory.createProcessManager(executablePath, processName, args);
        assertNotNull(processManager);
        assertEquals(executablePath, processManager.getExecutablePath());
        assertArrayEquals(args, processManager.getArgs());
        
        // 测试不同参数的构造方法
        ProcessManager processManager2 = ProcessManagerFactory.createProcessManager(executablePath, processName);
        assertNotNull(processManager2);
        assertEquals(executablePath, processManager2.getExecutablePath());
        assertEquals(0, processManager2.getArgs().length);
    }
    
    /**
     * 测试执行简单命令
     */
    @Test
    void testExecuteCommand() throws IOException, InterruptedException {
        String command = System.getProperty("os.name").toLowerCase().contains("win") 
                ? "echo hello world" 
                : "echo 'hello world'";
        
        String result = ProcessManagerFactory.executeCommand(command);
        assertEquals("hello world", result);
    }
    
    /**
     * 测试执行带参数的命令
     */
    @Test
    void testExecuteCommandWithArgs() throws IOException, InterruptedException {
        String command = System.getProperty("os.name").toLowerCase().contains("win") 
                ? "dir /b" 
                : "ls -la | head -5";
        
        // 只验证命令能执行，不验证具体输出，因为不同环境输出不同
        String result = ProcessManagerFactory.executeCommand(command);
        assertNotNull(result);
        // 输出应该不为空
        assertFalse(result.isEmpty());
    }
    
    /**
     * 测试执行错误命令
     */
    @Test
    void testExecuteErrorCommand() {
        String command = System.getProperty("os.name").toLowerCase().contains("win") 
                ? "invalid_command_12345" 
                : "invalid-command-12345";
        
        // 应该抛出IOException
        assertThrows(IOException.class, () -> {
            ProcessManagerFactory.executeCommand(command);
        });
    }
    
    /**
     * 测试操作系统检测
     */
    @Test
    void testOSDetection() {
        String osName = System.getProperty("os.name").toLowerCase();
        String executablePath = "/bin/echo";
        String processName = "echo";
        
        ProcessManager processManager = ProcessManagerFactory.createProcessManager(executablePath, processName);
        
        if (osName.contains("win")) {
            assertTrue(processManager instanceof WindowExeProcessManager);
        } else if (osName.contains("linux") || osName.contains("unix")) {
            assertTrue(processManager instanceof LinuxExeProcessManager);
        } else {
            // 其他操作系统应该抛出异常
            assertThrows(UnsupportedOperationException.class, () -> {
                ProcessManagerFactory.createProcessManager(executablePath, processName);
            });
        }
    }
}