package xujiejie.core;

import xujiejie.model.Note;
import xujiejie.model.Chord;
import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

/**
 * 钢琴演奏器 - 使用Robot类模拟键盘事件演奏音符
 */
public class PianoPlayer {
    private Robot robot;
    boolean isPlaying = false;
    private Thread playThread;
    
    public PianoPlayer() {
        try {
            this.robot = new Robot();
            // 设置按键之间的默认延迟
            this.robot.setAutoDelay(10);
        } catch (AWTException e) {
            throw new RuntimeException("无法创建Robot实例", e);
        }
    }
    
    public PianoPlayer(Robot robot) {
        this.robot = robot;
    }
    
    /**
     * 演奏单个音符
     * @param note 音符对象
     */
    public void playNote(Note note) {
        int keyCode = KeyMapper.getKeyCode(note.getNoteChar());
        if (keyCode != -1) {
            robot.keyPress(keyCode);
            delay(note.getDuration());
            robot.keyRelease(keyCode);
        }
    }
    
    /**
     * 演奏和弦
     * @param chord 和弦对象
     */
    public void playChord(Chord chord) {
        // 按下所有和弦音符
        for (Note note : chord.getNotes()) {
            int keyCode = KeyMapper.getKeyCode(note.getNoteChar());
            if (keyCode != -1) {
                robot.keyPress(keyCode);
            }
        }
        
        // 保持和弦时长
        delay(chord.getDuration());
        
        // 释放所有和弦音符
        for (Note note : chord.getNotes()) {
            int keyCode = KeyMapper.getKeyCode(note.getNoteChar());
            if (keyCode != -1) {
                robot.keyRelease(keyCode);
            }
        }
    }
    
    /**
     * 演奏整个乐谱（文本格式）
     * @param scoreText 乐谱文本
     */
    public void playScore(String scoreText) {
        if (isPlaying) {
            stopPlaying();
        }
        
        isPlaying = true;
        playThread = new Thread(() -> {
            String[] lines = scoreText.split("\\r?\\n");
            for (String line : lines) {
                if (!isPlaying) break;
                
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                // 解析并演奏每一行
                if (line.startsWith("(")) {
                    // 处理和弦
                    String chordPart = line.substring(1, line.indexOf(')'));
                    String[] parts = line.substring(line.indexOf(')') + 1).trim().split("\\s+");
                    int duration = parts.length > 0 ? Integer.parseInt(parts[0]) : 200;
                    
                    List<Note> notes = new ArrayList<>();
                    for (char c : chordPart.toCharArray()) {
                        notes.add(new Note(c, duration));
                    }
                    playChord(new Chord(notes, duration));
                } else if (line.matches("\\d+")) {
                    // 处理延时
                    delay(Integer.parseInt(line));
                } else {
                    // 处理单音
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 1) {
                        char noteChar = parts[0].charAt(0);
                        int duration = parts.length >= 2 ? Integer.parseInt(parts[1]) : 200;
                        playNote(new Note(noteChar, duration));
                    }
                }
            }
            isPlaying = false;
        });
        
        playThread.start();
    }
    
    /**
     * 停止演奏
     */
    public void stopPlaying() {
        isPlaying = false;
        if (playThread != null && playThread.isAlive()) {
            playThread.interrupt();
            try {
                playThread.join(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // 释放所有可能按下的键
        for (int keyCode : KeyMapper.getAllKeyCodes()) {
            robot.keyRelease(keyCode);
        }
    }
    
    /**
     * 检查是否正在演奏
     * @return 是否正在演奏
     */
    public boolean isPlaying() {
        return isPlaying;
    }
    
    /**
     * 精确延时（毫秒）
     * @param millis 毫秒数
     */
    void delay(long millis) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < millis) {
            if (!isPlaying) break;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    /**
     * 演奏示例曲目 - 小星星
     */
    public void playTwinkleTwinkle() {
        String score = "Q 200\n" +
                      "Q 200\n" +
                      "T 200\n" +
                      "T 200\n" +
                      "Y 200\n" +
                      "Y 200\n" +
                      "T 400\n" +
                      "\n" +
                      "F 200\n" +
                      "F 200\n" +
                      "D 200\n" +
                      "D 200\n" +
                      "S 200\n" +
                      "S 200\n" +
                      "Q 400";
        
        playScore(score);
    }
}