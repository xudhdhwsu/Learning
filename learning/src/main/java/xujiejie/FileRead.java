package xujiejie;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class FileRead {

    private final Path scanRoot;
    private final Path outputFile;

    public FileRead(String scanRoot, String outputFile) {
        this.scanRoot = Paths.get(scanRoot);
        this.outputFile = Paths.get(outputFile);
    }

    /**
     * 开始扫描指定目录下的所有文件，并将结果写入输出文件
     * 
     * @throws IOException 如果发生IO错误
     */

    public void startScan() throws IOException {
        AtomicLong totalSize = new AtomicLong(0);
        AtomicLong fileCount = new AtomicLong(0);
        try 
            (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))
             {
                
            Files.walk(scanRoot)
                 .parallel()
                 .filter(Files::isRegularFile)
                 .forEach(path -> {
                    try {
                        long size = Files.size(path);
                        String record = formatFileRecord(path, size);

                        synchronized (this) {
                            writer.write(record);
                            writer.newLine();
                            totalSize.addAndGet(size);
                            fileCount.incrementAndGet();
                        }

                    } catch (IOException e) { /* 错误处理 */ e.printStackTrace();}
                });
        } catch (IOException e) { /* 错误处理 */ e.printStackTrace();}

    }
    
    
    /**
     * 格式化文件记录信息
     * 
     * @param file 文件路径
     * @param size 文件大小（字节）
     * @return 格式化后的字符串记录
     */

    private String formatFileRecord(Path file, long size) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%s|%d|%s|%s", 
                file.toAbsolutePath(),
                size,
                sdf.format(new Date(file.toFile().lastModified())),
                Files.isDirectory(file) ? "DIR" : "FILE");
    }
    
    public static void main(String[] args) throws IOException {
        // 示例用法
        FileRead monitor = new FileRead("D:\\Kugou", "scan_report.log");
        monitor.startScan();
    }
}