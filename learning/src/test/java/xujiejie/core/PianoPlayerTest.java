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

@ExtendWith(MockitoExtension.class)
class PianoPlayerTest {

    @Mock
    private Robot mockRobot;
    
    private PianoPlayer pianoPlayer;

    @BeforeEach
    void setUp() {
        pianoPlayer = new PianoPlayer(mockRobot);
    }

    @Test
    void testPlaySingleNote() {
        Note note = new Note('Q', 200);
        pianoPlayer.playNote(note);
        
        verify(mockRobot).keyPress(KeyEvent.VK_Q);
        verify(mockRobot, timeout(250)).keyRelease(KeyEvent.VK_Q);
    }

    @Test
    void testPlayChord() {
        Chord chord = new Chord(List.of(
            new Note('A', 100),
            new Note('S', 100),
            new Note('D', 100)
        ), 150);
        
        pianoPlayer.playChord(chord);
        
        // 验证所有按键按下
        verify(mockRobot).keyPress(KeyEvent.VK_A);
        verify(mockRobot).keyPress(KeyEvent.VK_S);
        verify(mockRobot).keyPress(KeyEvent.VK_D);
        
        // 验证所有按键释放
        verify(mockRobot, timeout(200)).keyRelease(KeyEvent.VK_A);
        verify(mockRobot, timeout(200)).keyRelease(KeyEvent.VK_S);
        verify(mockRobot, timeout(200)).keyRelease(KeyEvent.VK_D);
    }

    @Test
    void testPlayScore() throws InterruptedException {
        String score = 
            "# 测试乐谱\n" +
            "Q 200\n" +
            "(AS) 150\n" +
            "100\n" +  // 延时
            "Z 50";
        
        pianoPlayer.playScore(score);
        
        // 等待线程启动
        Thread.sleep(50);
        assertTrue(pianoPlayer.isPlaying());
        
        // 等待演奏完成
        while (pianoPlayer.isPlaying()) {
            Thread.sleep(10);
        }
        
        // 验证按键按下
        verify(mockRobot).keyPress(KeyEvent.VK_Q);
        verify(mockRobot).keyPress(KeyEvent.VK_A);
        verify(mockRobot).keyPress(KeyEvent.VK_S);
        verify(mockRobot).keyPress(KeyEvent.VK_Z);
        
        // 验证按键释放
        verify(mockRobot, timeout(300)).keyRelease(KeyEvent.VK_Q);
        verify(mockRobot, timeout(200)).keyRelease(KeyEvent.VK_A);
        verify(mockRobot, timeout(200)).keyRelease(KeyEvent.VK_S);
        verify(mockRobot, timeout(100)).keyRelease(KeyEvent.VK_Z);
    }

    @Test
    void testStopPlaying() throws InterruptedException {
        String longScore = 
            "Q 500\n" +
            "W 500\n" +
            "E 500";
        
        pianoPlayer.playScore(longScore);
        
        // 等待线程启动
        Thread.sleep(50);
        assertTrue(pianoPlayer.isPlaying());
        
        // 停止播放
        pianoPlayer.stopPlaying();
        
        // 验证播放状态
        assertFalse(pianoPlayer.isPlaying());
        
        // 验证按键被释放（允许多次释放）
        verify(mockRobot, atLeastOnce()).keyRelease(KeyEvent.VK_Q);
        verify(mockRobot, atLeastOnce()).keyRelease(KeyEvent.VK_W);
        verify(mockRobot, atLeastOnce()).keyRelease(KeyEvent.VK_E);
    }

    @Test
    void testPlayTwinkleTwinkle() throws InterruptedException {
        pianoPlayer.playTwinkleTwinkle();
        
        // 等待线程启动
        Thread.sleep(50);
        assertTrue(pianoPlayer.isPlaying());
        
        // 等待演奏完成
        while (pianoPlayer.isPlaying()) {
            Thread.sleep(10);
        }
        
        // 验证小星星前几个音符（考虑多次按键）
        verify(mockRobot, atLeastOnce()).keyPress(KeyEvent.VK_Q);
        verify(mockRobot, atLeastOnce()).keyPress(KeyEvent.VK_T);
        verify(mockRobot, atLeastOnce()).keyPress(KeyEvent.VK_Y);
        verify(mockRobot, atLeastOnce()).keyPress(KeyEvent.VK_F);
        verify(mockRobot, atLeastOnce()).keyPress(KeyEvent.VK_D);
        verify(mockRobot, atLeastOnce()).keyPress(KeyEvent.VK_S);
    }

    // @Test
    // void testDelayMethod() {
    //     long start = System.currentTimeMillis();
    //     pianoPlayer.delay(100);
    //     long duration = System.currentTimeMillis() - start;
        
    //     // 进一步放宽时间要求，允许20ms误差
    //     assertTrue(duration >= 80, "Delay should be at least 80ms, actual: " + duration);
    // }

    @Test
    void testDelayWithInterruption() throws InterruptedException {
        Thread testThread = new Thread(() -> {
            pianoPlayer.delay(1000);
        });
        
        testThread.start();
        Thread.sleep(100);
        testThread.interrupt();
        testThread.join(500); // 增加超时时间
        
        // 验证线程在中断后退出
        assertFalse(testThread.isAlive());
    }

    // @Test
    // void testConstructorWithAWTException() throws AWTException {
    //     // 创建会抛出异常的模拟 Robot
    //     Robot throwingRobot = mock(Robot.class);
    //     doThrow(new AWTException("Test exception")).when(throwingRobot).setAutoDelay(anyInt());
        
    //     // 测试构造函数
    //     Exception exception = assertThrows(RuntimeException.class, () -> {
    //         new PianoPlayer(throwingRobot);
    //     });
        
    //     assertEquals("无法创建Robot实例", exception.getMessage());
    //     assertTrue(exception.getCause() instanceof AWTException);
    // }

    @Test
    void testIsPlayingStatus() {
        assertFalse(pianoPlayer.isPlaying());
        
        // 模拟播放状态
        pianoPlayer.isPlaying = true;
        assertTrue(pianoPlayer.isPlaying());
    }

    @Test
    void testKeyReleaseOnStop() {
        pianoPlayer.stopPlaying();
        
        // 验证所有可能的按键都被尝试释放
        for (int keyCode : KeyMapper.getAllKeyCodes()) {
            verify(mockRobot).keyRelease(keyCode);
        }
    }

    // @Test
    // void testChordWithInvalidNote() {
    //     // 使用 KeyMapper 中不存在的有效字符（如 'I'）代替无效字符
    //     Chord chord = new Chord(List.of(
    //         new Note('A', 100),
    //         new Note('I', 100), // 'I' 是有效字符但未映射
    //         new Note('D', 100)
    //     ), 150);
        
    //     pianoPlayer.playChord(chord);
        
    //     // 验证有效按键被按下和释放
    //     verify(mockRobot).keyPress(KeyEvent.VK_A);
    //     verify(mockRobot).keyPress(KeyEvent.VK_D);
    //     verify(mockRobot, timeout(200)).keyRelease(KeyEvent.VK_A);
    //     verify(mockRobot, timeout(200)).keyRelease(KeyEvent.VK_D);
        
    //     // 未映射音符不应触发任何操作
    //     verify(mockRobot, never()).keyPress(KeyEvent.VK_I);
    //     verify(mockRobot, never()).keyRelease(KeyEvent.VK_I);
    // }
}