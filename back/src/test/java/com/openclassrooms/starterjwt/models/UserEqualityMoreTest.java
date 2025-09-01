
package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEqualityMoreTest {

    private static User u(long id) {
        return User.builder()
                .id(id)
                .email("x@x.com")
                .firstName("X")
                .lastName("X")
                .password("pwd")
                .admin(false)
                .build();
    }

    @Test
    void equals_sameReference_true() {
        User a = u(1);
        assertThat(a.equals(a)).isTrue();      
    }

    @Test
    void equals_null_false() {
        User a = u(1);
        assertThat(a.equals(null)).isFalse();  
    }

    @Test
    void equals_differentClass_false() {
        User a = u(1);
        Object otherType = new Object();
        assertThat(a.equals(otherType)).isFalse(); 
    }

    @Test
    void equals_bothIdNull_true() {
        User a = u(0); a.setId(null);
        User b = u(0); b.setId(null);
        assertThat(a.equals(b)).isTrue();      
  
        assertThat(a.toString()).isNotNull();
    }

    @Test
    void equals_oneIdNull_false() {
        User a = u(1);         
        User b = u(0); b.setId(null);
        assertThat(a.equals(b)).isFalse();     
        assertThat(b.equals(a)).isFalse();    
    }

    @Test
    void equals_differentIds_false_and_hashCodes_differ() {
        User a = u(1);
        User b = u(2);
        assertThat(a.equals(b)).isFalse();     
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }

    @Test
    void hashCode_stable_whenIdNull_and_builder_toString_covered() {
        User a = u(0); a.setId(null);
        int h1 = a.hashCode();
        int h2 = a.hashCode();
        assertThat(h1).isEqualTo(h2);          

       
        String builderTs = User.builder()
                .email("b@b.com").firstName("B").lastName("B").password("p").admin(true)
                .toString();
        assertThat(builderTs).isNotBlank();
    }
}
