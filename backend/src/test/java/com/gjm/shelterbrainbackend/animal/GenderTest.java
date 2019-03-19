package com.gjm.shelterbrainbackend.animal;

import org.junit.Test;

import static org.junit.Assert.*;

public class GenderTest {
    @Test
    public void toStringMale() {
        assertEquals("Meska", Gender.MALE.toString());
    }

    @Test
    public void toStringFemale() {
        assertEquals("Zenska", Gender.FEMALE.toString());
    }
}