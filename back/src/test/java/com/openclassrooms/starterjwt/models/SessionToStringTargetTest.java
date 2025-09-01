package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SessionToStringTargetTest {

    @Test
    void entity_toString_is_called() {
        Session s = new Session();
        s.setName("Yoga AM");
        s.setDescription("Flow");
        String ts = s.toString();
        assertThat(ts).isNotNull();
        
        assertThat(ts).contains("Session"); 
    }

    @Test
    void builder_toString_is_called() {
        Session.SessionBuilder builder = Session.builder()
                .id(123L)
                .name("Yoga Builder");
        String tb = builder.toString();
        assertThat(tb).isNotNull();
        assertThat(tb).contains("Session.SessionBuilder");
    }
}
