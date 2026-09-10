package com.argentum.townyCityStates.objects.influencer.abstraction;

import com.argemtum.townyCityStates.objects.influencer.abstraction.Influencer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Influencer abstraction")
public class InfluencerTest {
    static class TestInfluencer extends Influencer {
        TestInfluencer(float initInfluence){
            super(UUID.randomUUID(), initInfluence);
        }
    }

    @Test
    void Influencer_initInfluence(){
        // Arrange + Act
        Influencer influencer = new TestInfluencer(10f);

        // Assert
       assertEquals(10f, influencer.getInfluence());
    }

    @Test
    void addInfluence_increaseValue(){
        // Arrange
        Influencer influencer = new TestInfluencer(0f);

        // Act
        influencer.addInfluence(10f);

        // Assert
        assertEquals(10f, influencer.getInfluence());
    }

    @Test
    void reduceInfluence_reduceValue(){
        // Arrange
        Influencer influencer = new TestInfluencer(10f);

        // Act
        influencer.reduceInfluence(5f);

        // Assert
        assertEquals(5f, influencer.getInfluence());
    }

    @Test
    void reduceInfluence_negativeValue(){
        Influencer influencer = new TestInfluencer(10f);

        influencer.reduceInfluence(20f);

        assertEquals(-10f, influencer.getInfluence());
    }

    @Test
    void addInfluence_negativeAmount_throws(){
        Influencer influencer = new TestInfluencer(10f);

        assertThrows(IllegalArgumentException.class,
                () -> influencer.addInfluence(-10f));
    }

    @Test
    void reduceInfluence_negativeAmount_throws(){
        Influencer influencer = new TestInfluencer(10f);

        assertThrows(IllegalArgumentException.class,
                () -> influencer.reduceInfluence(-10f));
    }
}
