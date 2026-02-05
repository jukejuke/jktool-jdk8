package io.github.jukejuke.tool.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class WindowExeProcessManager implements ProcessManager {
    private String processName;
    private String executablePath;
    private String[] args;
    private String charsetName;

    public WindowExeProcessManager(String executablePath, String processName) {
        this(executablePath, processName, new String[0]);
    }

    public WindowExeProcessManager(String executablePath, String processName, String[] args) {
        this(executablePath, processName, args, "GBK");
    }

    public WindowExeProcessManager(String executablePath, String processName, String[] args, String charsetName) {
        this.executablePath = executablePath;
        this.processName = processName;
        this.args = args;
        this.charsetName = charsetName;
    }

    public synchronized boolean start() throws IOException {
        Process process = Runtime.getRuntime().exec(executablePath + " " + String.join(" ", args));
        // 等待进程启动完成（可选，根据实际情况调整超时时间）
        try {
            boolean completed = process.waitFor(10, TimeUnit.SECONDS);
            if (!completed) {
                System.out.println("进程启动超时: " + processName);
                return false;
            }
        } catch (InterruptedException e) {
            System.err.println("等待进程启动时发生错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized boolean stop() throws IOException {
        // 再执行 taskkill
        try {
            // 方法1：通过进程名直接终止（会终止所有同名进程）
            Process process = Runtime.getRuntime().exec("taskkill /F /IM " + processName);

            // 读取命令执行结果（可选，用于调试）
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("成功终止进程: " + processName);
                return true;
            } else {
                System.out.println("终止进程失败，退出码: " + exitCode);
                return false;
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("终止进程时发生错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void restart() throws IOException, InterruptedException {
        stop();
        start();
    }

    public synchronized boolean isRunning() throws IOException {
        String cmd = "tasklist /FI \"IMAGENAME eq " + processName + "\"";
        Process process = Runtime.getRuntime().exec(cmd);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains(processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String[] buildCommand() {
        String[] command = new String[1 + args.length];
        command[0] = executablePath;
        System.arraycopy(args, 0, command, 1, args.length);
        return command;
    }

    public String getExecutablePath() {
        return executablePath;
    }

    public String[] getArgs() {
        return args;
    }
}