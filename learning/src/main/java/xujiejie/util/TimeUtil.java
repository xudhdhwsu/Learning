package xujiejie.util;

/**
 * 时间工具类 - 提供精确的时间控制方法
 */
public class TimeUtil {
    
    /**
     * 精确延时（毫秒）
     * @param millis 毫秒数
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 高精度延时（纳秒级）
     * @param nanos 纳秒数
     */
    public static void sleepNanos(long nanos) {
        long start = System.nanoTime();
        while (System.nanoTime() - start < nanos) {
            // 忙等待（适用于短时间延时）
        }
    }
    
    /**
     * 获取当前时间戳（毫秒）
     * @return 当前时间戳
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }
    
    /**
     * 获取当前时间戳（纳秒）
     * @return 当前时间戳（纳秒）
     */
    public static long nanoTime() {
        return System.nanoTime();
    }
    
    /**
     * 将BPM（每分钟节拍数）转换为毫秒/节拍
     * @param bpm 每分钟节拍数
     * @return 每拍毫秒数
     */
    public static double bpmToMsPerBeat(double bpm) {
        if (bpm <= 0) {
            throw new IllegalArgumentException("BPM必须大于0");
        }
        return 60000.0 / bpm;
    }
    
    /**
     * 计算音符实际持续时间
     * @param noteValue 音符时值（1=全音符, 4=四分音符等）
     * @param bpm 每分钟节拍数
     * @return 持续时间（毫秒）
     */
    public static double calculateDuration(int noteValue, double bpm) {
        if (noteValue <= 0) {
            throw new IllegalArgumentException("音符时值必须大于0");
        }
        return bpmToMsPerBeat(bpm) * (4.0 / noteValue);
    }
}