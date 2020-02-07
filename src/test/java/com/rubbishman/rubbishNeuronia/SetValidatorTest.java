package com.rubbishman.rubbishNeuronia;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.vostValidator.CostValidator;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;
import com.rubbishman.rubbishNeuronia.state.cost.SetValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SetValidatorTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return ImmutableList.of(
            new Object[] {
                    ImmutableList.of(
                            Concept.BLUE,
                            Concept.ORANGE,
                            Concept.GREEN
                    ),
                    ImmutableList.of(
                            Concept.TEAL,
                            Concept.ORANGE,
                            Concept.BLUE,
                            Concept.RED,
                            Concept.GREEN
                    ),
                    true
            },
            new Object[] {
                    ImmutableList.of(
                            Concept.BLUE,
                            Concept.ORANGE,
                            Concept.GREEN,
                            Concept.GREEN
                    ),
                    ImmutableList.of(
                            Concept.TEAL,
                            Concept.ORANGE,
                            Concept.BLUE,
                            Concept.RED,
                            Concept.GREEN
                    ),
                    false
            },
            new Object[] {
                    ImmutableList.of(
                            Concept.BLUE,
                            Concept.ORANGE,
                            Concept.GREEN,
                            Concept.GREEN
                    ),
                    ImmutableList.of(
                            Concept.TEAL,
                            Concept.ORANGE,
                            Concept.GREEN,
                            Concept.BLUE,
                            Concept.RED,
                            Concept.GREEN
                    ),
                    true
            }
        );
    }

    private ImmutableList<Concept> requiredConcepts;
    private ImmutableList<Concept> availableConcepts;
    private boolean pass;

    public SetValidatorTest(
            ImmutableList<Concept> requiredConcepts,
            ImmutableList<Concept> availableConcepts,
            boolean pass
            ) {
        this.requiredConcepts = requiredConcepts;
        this.availableConcepts = availableConcepts;
        this.pass = pass;
    }

    @Test
    public void testSetValidator() {
        SetValidator setVal = new SetValidator(requiredConcepts);

        assertEquals("Required: " + requiredConcepts + ", available: " + availableConcepts, pass, CostValidator.validate(setVal, availableConcepts));
    }
}
