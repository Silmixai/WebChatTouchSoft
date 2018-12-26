package com.mixail;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsoleHelperTest {

    private String HELLO = "Hello";
    private  BufferedReader reader;



    @Test
    void writeMessage() {



    }

    @Test
    void readString() {
        try {
            reader=mock(BufferedReader.class);
            when(reader.readLine()).thenReturn("Hello");
            assertEquals(HELLO,reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}