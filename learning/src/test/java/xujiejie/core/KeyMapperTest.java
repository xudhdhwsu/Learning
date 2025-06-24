package xujiejie.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

public class KeyMapperTest {
    
    @Test
    public void testKeyMapping() {
        // 测试有效音符
        assertEquals(KeyEvent.VK_Q, KeyMapper.getKeyCode('Q'));
        assertEquals(KeyEvent.VK_A, KeyMapper.getKeyCode('a')); // 测试小写
        assertEquals(KeyEvent.VK_Z, KeyMapper.getKeyCode('Z'));
        
        // 测试无效音符
        assertEquals(-1, KeyMapper.getKeyCode('I'));
        assertEquals(-1, KeyMapper.getKeyCode('K'));
        assertEquals(-1, KeyMapper.getKeyCode('O'));
        
        // 测试反向映射
        assertEquals('Q', (char) KeyMapper.getNoteChar(KeyEvent.VK_Q));
        assertEquals('A', (char) KeyMapper.getNoteChar(KeyEvent.VK_A));
        assertEquals('Z', (char) KeyMapper.getNoteChar(KeyEvent.VK_Z));
        
        // 测试有效性检查
        assertTrue(KeyMapper.isValidNote('T'));
        assertTrue(KeyMapper.isValidNote('h')); // 小写
        assertFalse(KeyMapper.isValidNote('P'));
    }
    
    @Test
    public void testPrintKeyMap() {
        // 打印键位映射表（用于手动验证）
        KeyMapper.printKeyMap();
    }
}