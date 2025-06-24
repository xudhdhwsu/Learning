package xujiejie.core;

import xujiejie.model.Note;
import xujiejie.model.Chord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // 添加 Mockito 扩展
class ScoreParserTest {

    @Mock
    private Robot mockRobot;
    
    private ScoreParser parser;
    
    @BeforeEach
    void setUp() {
        // 在每个测试前初始化解析器
        parser = new ScoreParser(mockRobot);
    }

    @Test
    void testParseSingleNote() {
        String score = "Q 200";
        List<Runnable> instructions = parser.parseScore(score);
        
        assertEquals(1, instructions.size());
        instructions.get(0).run();
        
        verify(mockRobot).keyPress(KeyEvent.VK_Q);
        verify(mockRobot, timeout(250)).keyRelease(KeyEvent.VK_Q);
    }

    @Test
    void testParseChord() {
        String score = "(QA) 300";
        List<Runnable> instructions = parser.parseScore(score);
        
        assertEquals(1, instructions.size());
        instructions.get(0).run();
        
        // 验证按键按下
        verify(mockRobot).keyPress(KeyEvent.VK_Q);
        verify(mockRobot).keyPress(KeyEvent.VK_A);
        
        // 验证按键释放（使用 timeout 处理异步操作）
        verify(mockRobot, timeout(350)).keyRelease(KeyEvent.VK_Q);
        verify(mockRobot, timeout(350)).keyRelease(KeyEvent.VK_A);
    }

    @Test
    void testParseDelay() {
        String score = "150";
        List<Runnable> instructions = parser.parseScore(score);
        
        assertEquals(1, instructions.size());
        long start = System.currentTimeMillis();
        instructions.get(0).run();
        long duration = System.currentTimeMillis() - start;
        
        assertTrue(duration >= 150, "Delay should be at least 150ms");
    }

    @Test
    void testParseMixedScore() {
        String score = 
            "# 测试乐谱\n" +
            "Q 200\n" +
            "(AS) 150\n" +
            "100\n" +
            "Z 50";
        
        List<Runnable> instructions = parser.parseScore(score);
        assertEquals(4, instructions.size());
        
        // 执行所有指令
        instructions.forEach(Runnable::run);
        
        // 验证按键按下
        verify(mockRobot).keyPress(KeyEvent.VK_Q);
        verify(mockRobot).keyPress(KeyEvent.VK_A);
        verify(mockRobot).keyPress(KeyEvent.VK_S);
        verify(mockRobot).keyPress(KeyEvent.VK_Z);
        
        // 验证按键释放
        verify(mockRobot, timeout(250)).keyRelease(KeyEvent.VK_Q);
        verify(mockRobot, timeout(200)).keyRelease(KeyEvent.VK_A);
        verify(mockRobot, timeout(200)).keyRelease(KeyEvent.VK_S);
        verify(mockRobot, timeout(100)).keyRelease(KeyEvent.VK_Z);
    }

    @Test
    void testIgnoreCommentsAndEmptyLines() {
        String score = 
            "\n" +
            "# 空行和注释测试\n" +
            "  \n" +
            "A 100\n" +
            "# 注释行\n";
        
        List<Runnable> instructions = parser.parseScore(score);
        assertEquals(1, instructions.size());
    }

    @Test
    void testInvalidLine() {
        String score = "Invalid Line";
        List<Runnable> instructions = parser.parseScore(score);
        assertTrue(instructions.isEmpty());
    }

    @Test
    void testPlayNote() {
        Note note = new Note('Q', 200);
        parser.playNote(note);
        
        verify(mockRobot).keyPress(KeyEvent.VK_Q);
        verify(mockRobot, timeout(250)).keyRelease(KeyEvent.VK_Q);
    }

    @Test
    void testPlayChord() {
        Chord chord = new Chord(List.of(new Note('A', 100)));
        parser.playChord(chord);
        
        verify(mockRobot).keyPress(KeyEvent.VK_A);
        verify(mockRobot, timeout(150)).keyRelease(KeyEvent.VK_A);
    }
}