package io.github.jukejuke.tool.exec;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class WindowExeProcessManagerTest {
    private WindowExeProcessManager processManager;
    private static final String TEST_EXECUTABLE = "E:\\thirdParty\\hbuilder\\HBuilderX\\HBuilderX.exe";
    private static final String TEST_PROCESS_NAME = "HBuilderX.exe";

    @BeforeEach
    void setUp() {
        processManager = new WindowExeProcessManager(TEST_EXECUTABLE, TEST_PROCESS_NAME);
    }

    @AfterEach
    void tearDown() throws IOException {
        // 确保测试后进程被终止
        if (processManager.isRunning()) {
            //processManager.stop();
        }
    }

    @Test
    void testStartProcess() throws IOException {
        assertFalse(processManager.isRunning(), "进程初始状态应为未运行");
        boolean result = processManager.start();
        //assertTrue(result, "启动进程应该返回true");
        assertTrue(processManager.isRunning(), "启动后进程应该处于运行状态");
    }

    @Test
    void testStopProcess() throws IOException, InterruptedException {
        processManager.start();
        Thread.sleep(1000); // 等待进程启动
        assertTrue(processManager.isRunning(), "启动后进程应该处于运行状态");

        boolean result = processManager.stop();
        assertTrue(result, "停止进程应该返回true");
        assertFalse(processManager.isRunning(), "停止后进程应该处于未运行状态");
    }

    @Test
    void testIsRunning() throws IOException, InterruptedException {
        assertFalse(processManager.isRunning(), "初始状态下进程不应运行");

        processManager.start();
        Thread.sleep(1000);
        assertTrue(processManager.isRunning(), "进程启动后isRunning应该返回true");

        processManager.stop();
        Thread.sleep(1000);
        assertFalse(processManager.isRunning(), "进程停止后isRunning应该返回false");
    }

    @Test
    void testStartAlreadyRunningProcess() throws IOException, InterruptedException {
        processManager.start();
        Thread.sleep(1000);
        assertTrue(processManager.isRunning());

        // 尝试启动已运行的进程
        boolean result = processManager.start();
        assertFalse(result, "启动已运行的进程应该返回false");
    }

    @Test
    void testStopNotRunningProcess() throws IOException {
        assertFalse(processManager.isRunning());

        boolean result = processManager.stop();
        assertFalse(result, "停止未运行的进程应该返回false");
    }

    @Test
    void testRestartProcess() throws IOException, InterruptedException {
        //processManager.start();
        Thread.sleep(1000);
        //int initialPid = getProcessPid();

        processManager.restart();
        Thread.sleep(1000);
        //int newPid = getProcessPid();

        //assertNotEquals(initialPid, newPid, "重启后进程PID应该不同");
        assertTrue(processManager.isRunning());
    }

    @Deprecated
    private int getProcessPid() throws IOException {
       return 0;
    }
}