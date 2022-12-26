package org.example.Logger;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志系统的抽象类，用于记录日志信息。不同级别的日志记录到不同的日志文件中。<br/>
 * 日志文件的的格式为:logs/yyyy-MM-dd/info、warn、error、debug.txt<br/>
 */
public class Log {

    private static final String LOG_FLOADER = "Logs";
    private static final String INFO = "INFO";
    private static final String WARN = "WARN";
    private static final String ERROR = "ERROR";
    private static final String DEBUG = "DEBUG";
    private static Log logger = null;

    static {
        logger = new Log();
    }

    private Log() {
    }

    /**
     * 单例模式获取日志实体
     *
     * @return
     */
    public static synchronized Log getLogger() {
        if (null == logger) logger = new Log();
        return logger;
    }

    public static void main(String[] args) {
    }

    /**
     * 记录日志
     *
     * @param log         日志信息
     * @param floaderName 文件夹名称
     * @param fileName    文件名
     * @param level       日志级别
     * @throws IOException 可能产生IO异常，如读写错误
     */
    public synchronized void log(String log, String floaderName, String fileName, String level) throws IOException {
        if (!checkFile(floaderName, fileName)) {//如果文件夹和文件都存在
            makeFile(floaderName, fileName);
        }

        //记录日志文件
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(LOG_FLOADER + "/" + floaderName + "/" + fileName, true);
            fo.write((log + "\n").getBytes());
            fo.flush();
            if (ERROR.equals(level)) {
                System.err.println(log);
            } else {
                System.out.println(log);
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != fo) {
                try {
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    System.out.println("关闭文件:" + LOG_FLOADER + "/" + floaderName + "/" + fileName + " 失败！");
                }
            }
        }
    }


    /**
     * 按照指定的文件夹名和文件名创建文件
     *
     * @param floaderName 文件夹名
     * @param fileName    文件名
     */
    private void makeFile(String floaderName, String fileName) {
        if (null == floaderName || null == fileName) {
            System.err.println("文件夹或文件名为空！");
            return;
        }
        File root = new File(LOG_FLOADER);
        try {
            if (!root.isDirectory()) {
                if (!root.mkdir()) {
                    if (!root.mkdir()) {//多次创建失败，返回系统退出
                        System.err.println("无法创建日志文件的根目录：" + LOG_FLOADER + "/。程序退出！");
                        System.exit(0);
                    }
                }
            }
        } catch (SecurityException e) {
            System.err.println("创建日志文件出错(权限不足)." + e.getMessage());
            System.err.println("程序退出！");
            System.exit(0);
        }
        File floader = new File(LOG_FLOADER + "/" + floaderName);
        if (root.isDirectory()) {//root目录创建成功，创建yyyy-MM-dd文件夹
            if (!floader.isDirectory()) {
                try {
                    if (!floader.mkdir()) {
                        if (!floader.mkdir()) {
                            System.err.println("无法创建日志文件夹：" + LOG_FLOADER + "/" + floaderName + "/。程序退出！");
                            System.exit(0);
                        }
                    }
                } catch (SecurityException e) {
                    System.err.println("创建日志文件夹出错(权限不足)." + e.getMessage());
                    System.err.println("程序退出！");
                    System.exit(0);
                }
            }
        }
        //文件夹创建成功则创建文件
        File file = new File(LOG_FLOADER + "/" + floaderName + "/" + fileName);
        if (floader.isDirectory()) {
            if (!file.isFile()) {//文件不存在
                try {
                    if (!file.createNewFile()) {
                        if (!file.createNewFile()) {
                            System.err.println("无法创建日志文件：" + LOG_FLOADER + "/" + floaderName + "/。程序退出！");
                            System.exit(0);
                        }
                    }
                } catch (SecurityException e) {
                    System.err.println("创建日志文件出错(权限不足)." + e.getMessage());
                    System.err.println("程序退出！");
                    System.exit(0);
                } catch (IOException e) {
                    System.err.println("创建日志文件出错." + e.getMessage());
                    System.err.println("程序退出！");
                    System.exit(0);
                }

            }
        }
        if (file.exists()) {
            return;
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param dirName  文件夹名
     * @param fileName 文件名
     * @return 都存在则返回true，否则返回false
     */
    private boolean checkFile(String dirName, String fileName) {
        File directory = new File(LOG_FLOADER + "/" + dirName);
        if (directory.isDirectory()) {//文件加存在
            //判断文件是否存在
            File file = new File(LOG_FLOADER + "/" + dirName + "/" + fileName);
            if (file.isFile()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 记录普通的信息日志到普通日志文件下.日志文件格式为:yyyy-MM-dd-INFRO.txt
     *
     * @param log 日志信息
     */
    public void info(String log) {
        String dirName = getFloaderName("yyyy-MM-dd");
        String fileName = getFileName("yyyy-MM-dd") + "-INFRO.txt";
        String sf = String.format("[%tT] %s-%s %s ", new Date(), Thread.currentThread().getName(), "INFO", log);
        try {
            log(sf, dirName, fileName, INFO);
        } catch (IOException e) {
            System.err.println("INFO模式写文件失败." + e.getMessage() + ":" + e.getCause());
        }
    }

    /**
     * 记录普通日志
     *
     * @param log 日志信息
     * @param e   异常信息类
     */
    public void info(String log, Throwable e) {
        log += "." + e.getMessage() + ":" + e.getCause();
        info(log);
    }

    public void warn(String log) {
        String dirName = getFloaderName("yyyy-MM-dd");
        String fileName = getFileName("yyyy-MM-dd") + "-WARN.txt";
        String sf = String.format("[%tT] %s-%s %s ", new Date(), Thread.currentThread().getName(), "WARN", log);
        try {
            log(sf, dirName, fileName, WARN);
        } catch (IOException e) {
            System.err.println("WARN模式写文件失败." + e.getMessage() + ":" + e.getCause());
        }
    }

    /**
     * 记录警告级别日志
     *
     * @param log
     * @param e
     */
    public void warn(String log, Throwable e) {
        log += "." + e.getMessage() + ":" + e.getCause();
        warn(log);
    }

    /**
     * 记录错误级别日志
     *
     * @param log
     */
    public void error(String log) {
        String dirName = getFloaderName("yyyy-MM-dd");
        String fileName = getFileName("yyyy-MM-dd") + "-ERROR.txt";
        String sf = String.format("[%tT] %s-%s %s ", new Date(), Thread.currentThread().getName(), "ERROR", log);
        try {
            log(sf, dirName, fileName, ERROR);
        } catch (IOException e) {
            System.err.println("ERROR模式写文件失败." + e.getMessage() + ":" + e.getCause());
        }
    }

    public void error(String log, Throwable e) {
        log += "." + e.getMessage() + ":" + e.getCause();
        error(log);
    }

    public void debug(String log) {
        String dirName = getFloaderName("yyyy-MM-dd");
        String fileName = getFileName("yyyy-MM-dd") + "-DEBUG.txt";
        String sf = String.format("[%tT] %s-%s %s ", new Date(), Thread.currentThread().getName(), "DEBUG", log);
        try {
            log(sf, dirName, fileName, DEBUG);
        } catch (IOException e) {
            System.err.println("DEBUG模式写文件失败." + e.getMessage() + ":" + e.getCause());
        }
    }

    public void debug(String log, Throwable e) {
        log += "." + e.getMessage() + ":" + e.getCause();
        debug(log);
    }


    /**
     * 获取制定格式的文件夹名字
     *
     * @param string 日期格式，必须符合JAVA规范
     * @return
     */
    public synchronized String getFloaderName(String string) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(string);
            Date date = new Date();
            return sf.format(date);
        } catch (IllegalArgumentException e) {
            System.err.println("不符合JAVA规范的参数！采取默认参数格式：yyyy-MM-dd");
            getFloaderName("yyyy-MM-dd");
        }
        return null;
    }

    /**
     * 获取文件名字
     *
     * @param string 日期格式，必须符合JAVA规范
     * @return
     */
    public synchronized String getFileName(String string) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(string);
            Date date = new Date();
            return sf.format(date);
        } catch (IllegalArgumentException e) {
            System.err.println("不符合JAVA规范的参数！采取默认参数格式：yyyy-MM-dd");
            getFloaderName("yyyy-MM-dd");
        }
        return null;
    }
}
