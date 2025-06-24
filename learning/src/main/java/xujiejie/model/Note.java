package xujiejie.model;

import xujiejie.core.KeyMapper;

/**
 * 音符模型 - 表示单个音符
 */
public class Note {
    private final char noteChar; // 音符字符（如 'Q', 'A', 'Z' 等）
    private final int duration;   // 持续时间（毫秒）
    private final int velocity;   // 力度/音量（0-127）
    
    /**
     * 构造函数
     * @param noteChar 音符字符
     * @param duration 持续时间（毫秒）
     * @param velocity 力度/音量（0-127）
     */
    public Note(char noteChar, int duration, int velocity) {
        if (!KeyMapper.isValidNote(noteChar)) {
            throw new IllegalArgumentException("无效的音符字符: " + noteChar);
        }
        
        if (duration <= 0) {
            throw new IllegalArgumentException("持续时间必须大于0: " + duration);
        }
        
        if (velocity < 0 || velocity > 127) {
            throw new IllegalArgumentException("力度必须在0-127之间: " + velocity);
        }
        
        this.noteChar = Character.toUpperCase(noteChar);
        this.duration = duration;
        this.velocity = velocity;
    }
    
    /**
     * 简化构造函数（使用默认力度）
     * @param noteChar 音符字符
     * @param duration 持续时间（毫秒）
     */
    public Note(char noteChar, int duration) {
        this(noteChar, duration, 100); // 默认力度100
    }
    
    // Getters
    public char getNoteChar() {
        return noteChar;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public int getVelocity() {
        return velocity;
    }
    
    /**
     * 获取音符对应的键值
     * @return 键盘键值
     */
    public int getKeyCode() {
        return KeyMapper.getKeyCode(noteChar);
    }
    
    @Override
    public String toString() {
        return "Note{" +
                "noteChar=" + noteChar +
                ", duration=" + duration +
                ", velocity=" + velocity +
                '}';
    }
}