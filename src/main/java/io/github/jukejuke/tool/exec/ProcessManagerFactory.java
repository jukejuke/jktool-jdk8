package io.github.jukejuke.tool.exec;

import java.io.IOException;

public class ProcessManagerFactory {
    private ProcessManagerFactory() {
        // 私有构造方法，防止实例化
    }

    /**
     * 根据当前操作系统创建合适的ProcessManager实例
     *
     * @param executablePath 可执行文件路径
     * @param processName    进程名
     * @return ProcessManager实例
     */
    public static ProcessManager createProcessManager(String executablePath, String processName) {
        return createProcessManager(executablePath, processName, new String[0]);
    }

    /**
     * 根据当前操作系统创建合适的ProcessManager实例
     *
     * @param executablePath 可执行文件路径
     * @param processName    进程名
     * @param args           命令行参数
     * @return ProcessManager实例
     */
    public static ProcessManager createProcessManager(String executablePath, String processName, String[] args) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return new WindowExeProcessManager(executablePath, processName, args);
        } else if (osName.contains("linux") || osName.contains("unix")) {
            return new LinuxExeProcessManager(executablePath, processName, args);
        } else {
            throw new UnsupportedOperationException("不支持的操作系统: " + osName);
        }
    }

    /**
     * 根据当前操作系统创建合适的ProcessManager实例
     *
     * @param executablePath 可执行文件路径
     * @param processName    进程名
     * @param args           命令行参数
     * @param charsetName    字符集
     * @return ProcessManager实例
     */
    public static ProcessManager createProcessManager(String executablePath, String processName, String[] args, String charsetName) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return new WindowExeProcessManager(executablePath, processName, args, charsetName);
        } else if (osName.contains("linux") || osName.contains("unix")) {
            return new LinuxExeProcessManager(executablePath, processName, args, charsetName);
        } else {
            throw new UnsupportedOperationException("不支持的操作系统: " + osName);
        }
    }

    /**
     * 执行命令并返回结果
     *
     * @param command 命令
     * @return 命令执行结果
     * @throws IOException          执行过程中发生IO异常
     * @throws InterruptedException 执行过程中发生中断异常
     */
    public static String executeCommand(String command) throws IOException, InterruptedException {
        return executeCommand(command, "UTF-8");
    }

    /**
     * 执行命令并返回结果
     *
     * @param command     命令
     * @param charsetName 字符集
     * @return 命令执行结果
     * @throws IOException          执行过程中发生IO异常
     * @throws InterruptedException 执行过程中发生中断异常
     */
    public static String executeCommand(String command, String charsetName) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("sh", "-c", command);
        }
        
        Process process = processBuilder.start();
        
        StringBuilder output = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream(), charsetName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }
        
        StringBuilder error = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getErrorStream(), charsetName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                error.append(line).append(System.lineSeparator());
            }
        }
        
        process.waitFor();
        
        if (error.length() > 0) {
            throw new IOException("命令执行错误: " + error.toString());
        }
        
        return output.toString().trim();
    }
}