package app.darts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidThrowRepresentationExceptionTest {

    @Test
    void messageFormattedCorrectly() {
        var ex = new InvalidThrowRepresentationException("This input is XYZ not valid.", "w3");
        assertEquals("This input is XYZ not valid. Input: w3. Read more about formatting rules at /format-help",
                ex.getMessage());
    }

}