package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void constructor_sets_message() {
        MessageResponse resp = new MessageResponse("Hello world");

        assertEquals("Hello world", resp.getMessage());
    }

    @Test
    void setter_and_getter_work() {
        MessageResponse resp = new MessageResponse("Init");

        resp.setMessage("Updated");

        assertEquals("Updated", resp.getMessage());
    }
}
