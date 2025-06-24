package xujiejie.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 和弦模型 - 表示同时演奏的多个音符
 */
public class Chord {
    private final List<Note> notes; // 和弦中的音符
    private final int duration;     // 和弦持续时间（毫秒）
    
    /**
     * 构造函数
     * @param notes 音符列表
     * @param duration 和弦持续时间（毫秒）
     */
    public Chord(List<Note> notes, int duration) {
        if (notes == null || notes.isEmpty()) {
            throw new IllegalArgumentException("和弦必须包含至少一个音符");
        }
        
        if (duration <= 0) {
            throw new IllegalArgumentException("持续时间必须大于0: " + duration);
        }
        
        this.notes = new ArrayList<>(notes);
        this.duration = duration;
    }
    
    /**
     * 简化构造函数（使用默认持续时间）
     * @param notes 音符列表
     */
    public Chord(List<Note> notes) {
        this(notes, 200); // 默认持续时间200毫秒
    }
    
    // Getters
    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }
    
    public int getDuration() {
        return duration;
    }
    
    /**
     * 获取和弦中的所有音符字符
     * @return 音符字符数组
     */
    public char[] getNoteChars() {
        return notes.stream()
                .map(Note::getNoteChar)
                .map(String::valueOf)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString()
                .toCharArray();
    }
    
    /**
     * 获取和弦中的所有键值
     * @return 键值数组
     */
    public int[] getKeyCodes() {
        return notes.stream()
                .mapToInt(Note::getKeyCode)
                .toArray();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Chord{");
        for (Note note : notes) {
            sb.append(note.getNoteChar());
        }
        sb.append(", duration=").append(duration).append("}");
        return sb.toString();
    }
}