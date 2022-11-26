package app.darts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DartThrowTest {

    private static  final String FORMAT_GUIDANCE_MESSAGE = "Read more about formatting rules at /help";

    @Test
    void parsingSingle() {
        DartThrow dartThrow = DartThrow.of("12");
        assertEquals(12, dartThrow.getScore());
        assertFalse(dartThrow.isDouble());
        assertFalse(dartThrow.isInnerBullsEye());
    }

    @Test
    void parsingSingle_withinAcceptableRange() {
        InvalidThrowRepresentationException ex = assertThrows(InvalidThrowRepresentationException.class,
                () -> DartThrow.of("40"));
        assertEquals("Maximum sector value is 20. Input: 40. " + FORMAT_GUIDANCE_MESSAGE, ex.getMessage());
    }

    @Test
    void parsingDouble_uppercase() {
        DartThrow dartThrow = DartThrow.of("D15");
        assertEquals(30, dartThrow.getScore());
        assertTrue(dartThrow.isDouble());
        assertFalse(dartThrow.isInnerBullsEye());
    }

    @Test
    void parsingDouble_lowercase() {
        DartThrow dartThrow = DartThrow.of("d15");
        assertEquals(30, dartThrow.getScore());
        assertTrue(dartThrow.isDouble());
        assertFalse(dartThrow.isInnerBullsEye());
    }

    @Test
    void parsingDouble_noMoreAlphabetic() {
        InvalidThrowRepresentationException ex = assertThrows(InvalidThrowRepresentationException.class,
                () -> DartThrow.of("DD1"));
        assertEquals("Only the first character can be a non-digit modifier. Input: DD1. " + FORMAT_GUIDANCE_MESSAGE, ex.getMessage());
    }

    @Test
    void parsingDouble_noWhitespace() {
        InvalidThrowRepresentationException ex = assertThrows(InvalidThrowRepresentationException.class,
                () -> DartThrow.of("d 1"));
        assertEquals("Only the first character can be a non-digit modifier. Input: d 1. " + FORMAT_GUIDANCE_MESSAGE,
                ex.getMessage());
    }

    @Test
    void parsingTriple() {
        DartThrow dartThrow = DartThrow.of("t12");
        assertEquals(36, dartThrow.getScore());
        assertFalse(dartThrow.isDouble());
        assertFalse(dartThrow.isInnerBullsEye());
    }

    @Test
    void parsingMiss() {
        DartThrow dartThrow = DartThrow.of("x");
        assertEquals(0, dartThrow.getScore());
        assertFalse(dartThrow.isDouble());
        assertFalse(dartThrow.isInnerBullsEye());
    }

    @Test
    void parsingMiss_mustBeSingleCharacter() {
        InvalidThrowRepresentationException ex = assertThrows(InvalidThrowRepresentationException.class,
                () -> DartThrow.of("xa"));
        assertEquals("String representing board miss can be only a single 'x' character. Input: xa. " + FORMAT_GUIDANCE_MESSAGE,
                ex.getMessage());
    }

    @Test
    void parsingOuterBullsEye() {
        DartThrow dartThrow = DartThrow.of("25");
        assertEquals(25, dartThrow.getScore());
        assertFalse(dartThrow.isDouble());
        assertFalse(dartThrow.isInnerBullsEye());
    }

    @Test
    void parsingInnerBullsEye() {
        DartThrow dartThrow = DartThrow.of("50");
        assertEquals(50, dartThrow.getScore());
        assertFalse(dartThrow.isDouble());
        assertTrue(dartThrow.isInnerBullsEye());
    }

    @Test
    void parsingTooLongLine_validModifier() {
        InvalidThrowRepresentationException ex = assertThrows(InvalidThrowRepresentationException.class,
                () -> DartThrow.of("t123"));
        assertEquals("String representing throw should be 1 to 3 characters long. Input: t123. " + FORMAT_GUIDANCE_MESSAGE,
                ex.getMessage());
    }

    @Test
    void parsingTooLongLine_validSector() {
        InvalidThrowRepresentationException ex = assertThrows(InvalidThrowRepresentationException.class,
                () -> DartThrow.of("tt12"));
        assertEquals("String representing throw should be 1 to 3 characters long. Input: tt12. " + FORMAT_GUIDANCE_MESSAGE,
                ex.getMessage());
    }

    @Test
    void startsWithNotParsableLetter() {
        InvalidThrowRepresentationException ex = assertThrows(InvalidThrowRepresentationException.class,
                () -> DartThrow.of("q2"));
        assertEquals("Letter 'q' is not a valid modifier. Input: q2. " + FORMAT_GUIDANCE_MESSAGE, ex.getMessage());
    }

    @Test
    void singleValueNotParsable() {
        InvalidThrowRepresentationException ex = assertThrows(InvalidThrowRepresentationException.class,
                () -> DartThrow.of("1A"));
        assertEquals("Not parsable as sector value. Input: 1A. " + FORMAT_GUIDANCE_MESSAGE, ex.getMessage());
    }

}