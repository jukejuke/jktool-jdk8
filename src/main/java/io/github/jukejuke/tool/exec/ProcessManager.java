package io.github.jukejuke.tool.exec;

import java.io.IOException;

public interface ProcessManager {
    /**
     * 启动进程
     *
     * @return 启动是否成功
     * @throws IOException 启动过程中发生IO异常
     */
    boolean start() throws IOException;

    /**
     * 停止进程
     *
     * @return 停止是否成功
     * @throws IOException 停止过程中发生IO异常
     */
    boolean stop() throws IOException;

    /**
     * 重启进程
     *
     * @throws IOException          重启过程中发生IO异常
     * @throws InterruptedException 重启过程中发生中断异常
     */
    void restart() throws IOException, InterruptedException;

    /**
     * 检查进程是否正在运行
     *
     * @return 进程是否正在运行
     * @throws IOException 检查过程中发生IO异常
     */
    boolean isRunning() throws IOException;

    /**
     * 获取可执行文件路径
     *
     * @return 可执行文件路径
     */
    String getExecutablePath();

    /**
     * 获取命令行参数
     *
     * @return 命令行参数数组
     */
    String[] getArgs();
}