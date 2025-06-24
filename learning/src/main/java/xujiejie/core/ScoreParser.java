package xujiejie.core;

import xujiejie.model.Note;
import xujiejie.model.Chord;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 乐谱解析器 - 将文本乐谱解析为音符或和弦序列
 * 
 * 支持格式：
 * 1. 单音： "Q 200"   -> 音符Q持续200ms
 * 2. 和弦： "(QA) 300" -> 同时按下Q和A持续300ms
 * 3. 延时： "100"     -> 等待100ms
 * 4. 注释： "# 这是注释"
 */
public class ScoreParser {
    
    // 正则表达式匹配模式
    private static final Pattern SINGLE_NOTE_PATTERN = Pattern.compile("^([A-Za-z])\\s+(\\d+)$");
    private static final Pattern CHORD_PATTERN = Pattern.compile("^\\(([A-Za-z]+)\\)\\s+(\\d+)$");
    private static final Pattern DELAY_PATTERN = Pattern.compile("^(\\d+)$");
    
    private final Robot robot;
    
    public ScoreParser(Robot robot) {
        this.robot = robot;
    }
    
    /**
     * 解析文本乐谱
     * @param scoreText 乐谱文本
     * @return 可执行的演奏指令列表
     */
    public List<Runnable> parseScore(String scoreText) {
        List<Runnable> instructions = new ArrayList<>();
        String[] lines = scoreText.split("\\r?\\n");
        
        for (String line : lines) {
            line = line.trim();
            
            // 忽略空行和注释
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            
            // 尝试匹配单音
            Matcher singleMatcher = SINGLE_NOTE_PATTERN.matcher(line);
            if (singleMatcher.find()) {
                char noteChar = singleMatcher.group(1).charAt(0);
                int duration = Integer.parseInt(singleMatcher.group(2));
                Note note = new Note(noteChar, duration);
                instructions.add(() -> playNote(note));
                continue;
            }
            
            // 尝试匹配和弦
            Matcher chordMatcher = CHORD_PATTERN.matcher(line);
            if (chordMatcher.find()) {
                String chordNotes = chordMatcher.group(1);
                int duration = Integer.parseInt(chordMatcher.group(2));
                List<Note> notes = new ArrayList<>();
                for (char c : chordNotes.toCharArray()) {
                    notes.add(new Note(c, duration));
                }
                Chord chord = new Chord(notes, duration);
                instructions.add(() -> playChord(chord));
                continue;
            }
            
            // 尝试匹配延时
            Matcher delayMatcher = DELAY_PATTERN.matcher(line);
            if (delayMatcher.find()) {
                int delay = Integer.parseInt(delayMatcher.group(1));
                instructions.add(() -> delay(delay));
                continue;
            }
            
            System.err.println("无法解析的乐谱行: " + line);
        }
        
        return instructions;
    }
    
    /**
     * 播放单个音符
     * @param note 音符对象
     */
    void playNote(Note note) {
        int keyCode = KeyMapper.getKeyCode(note.getNoteChar());
        if (keyCode != -1) {
            robot.keyPress(keyCode);
            delay(note.getDuration());
            robot.keyRelease(keyCode);
        }
    }
    
    /**
     * 播放和弦
     * @param chord 和弦对象
     */
    void playChord(Chord chord) {
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
     * 精确延时（毫秒）
     * @param millis 毫秒数
     */
    private void delay(long millis) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < millis) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * 解析并演奏乐谱
     * @param scoreText 乐谱文本
     */
    public void playScore(String scoreText) {
        List<Runnable> instructions = parseScore(scoreText);
        for (Runnable instruction : instructions) {
            if (Thread.interrupted()) {
                System.out.println("演奏被中断");
                return;
            }
            instruction.run();
        }
    }
    
    /**
     * 从文件解析并演奏乐谱
     * @param filePath 乐谱文件路径
     */
    public void playScoreFromFile(String filePath) {
        // 实际项目中需要实现文件读取
        // 这里简化实现
        System.out.println("从文件加载乐谱: " + filePath);
        // 伪代码：String scoreText = FileUtil.readFile(filePath);
        // playScore(scoreText);
    }
}