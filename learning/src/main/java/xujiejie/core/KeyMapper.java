package xujiejie.core;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * 键位映射器 - 负责音符字符到键盘键值的映射
 * 
 * 原神风物之诗琴有21个键位，分三行排列：
 * 第一行：Q, W, E, R, T, Y, U
 * 第二行：A, S, D, F, G, H, J
 * 第三行：Z, X, C, V, B, N, M
 */
public class KeyMapper {
    
    // 键位映射表：音符字符 -> 键盘键值
    private static final Map<Character, Integer> KEY_MAP = new HashMap<>();
    
    // 反向映射表：键盘键值 -> 音符字符
    private static final Map<Integer, Character> REVERSE_KEY_MAP = new HashMap<>();
    
    static {
        // 初始化键位映射
        // 第一行音符 (高音区)
        mapKey('Q', KeyEvent.VK_Q);
        mapKey('W', KeyEvent.VK_W);
        mapKey('E', KeyEvent.VK_E);
        mapKey('R', KeyEvent.VK_R);
        mapKey('T', KeyEvent.VK_T);
        mapKey('Y', KeyEvent.VK_Y);
        mapKey('U', KeyEvent.VK_U);
        
        // 第二行音符 (中音区)
        mapKey('A', KeyEvent.VK_A);
        mapKey('S', KeyEvent.VK_S);
        mapKey('D', KeyEvent.VK_D);
        mapKey('F', KeyEvent.VK_F);
        mapKey('G', KeyEvent.VK_G);
        mapKey('H', KeyEvent.VK_H);
        mapKey('J', KeyEvent.VK_J);
        
        // 第三行音符 (低音区)
        mapKey('Z', KeyEvent.VK_Z);
        mapKey('X', KeyEvent.VK_X);
        mapKey('C', KeyEvent.VK_C);
        mapKey('V', KeyEvent.VK_V);
        mapKey('B', KeyEvent.VK_B);
        mapKey('N', KeyEvent.VK_N);
        mapKey('M', KeyEvent.VK_M);
    }
    
    /**
     * 添加键位映射
     * @param noteChar 音符字符
     * @param keyCode 键盘键值
     */
    private static void mapKey(char noteChar, int keyCode) {
        KEY_MAP.put(noteChar, keyCode);
        REVERSE_KEY_MAP.put(keyCode, noteChar);
    }
    
    /**
     * 获取音符对应的键盘键值
     * @param noteChar 音符字符
     * @return 键盘键值，如果找不到返回-1
     */
    public static int getKeyCode(char noteChar) {
        return KEY_MAP.getOrDefault(Character.toUpperCase(noteChar), -1);
    }
    
    /**
     * 获取键盘键值对应的音符
     * @param keyCode 键盘键值
     * @return 音符字符，如果找不到返回null
     */
    public static Character getNoteChar(int keyCode) {
        return REVERSE_KEY_MAP.get(keyCode);
    }
    
    /**
     * 检查字符是否为有效的音符键
     * @param noteChar 音符字符
     * @return 是否为有效音符键
     */
    public static boolean isValidNote(char noteChar) {
        return KEY_MAP.containsKey(Character.toUpperCase(noteChar));
    }
    
    /**
     * 获取所有有效音符字符
     * @return 有效音符字符数组
     */
    public static char[] getAllNoteChars() {
        return KEY_MAP.keySet().toString().toCharArray();
    }
    
    /**
     * 获取所有有效键值
     * @return 有效键值数组
     */
    public static int[] getAllKeyCodes() {
        return KEY_MAP.values().stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 打印键位映射表 (用于调试)
     */
    public static void printKeyMap() {
        System.out.println("Genshin Piano Key Mapping:");
        System.out.println("+----------+------------+");
        System.out.println("| Note Key | Key Code   |");
        System.out.println("+----------+------------+");
    
        KEY_MAP.forEach((note, keyCode) -> {
            System.out.printf("|    %c     |   %-8d |\n", note, keyCode);
            System.out.println("+----------+------------+");
        });
    }
}