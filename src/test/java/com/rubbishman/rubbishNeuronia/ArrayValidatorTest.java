package com.rubbishman.rubbishNeuronia;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.internal.neuronia.CostValidator.CostValidator;
import com.rubbishman.rubbishRedux.internal.neuronia.state.brain.Concept;
import com.rubbishman.rubbishRedux.internal.neuronia.state.cost.ArrayValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

@RunWith(Parameterized.class)
public class ArrayValidatorTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return ImmutableList.of(
                new Object[] {
                        ImmutableList.of(
                                Concept.ORANGE,
                                Concept.BLUE,
                                Concept.RED
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
                                Concept.ORANGE,
                                Concept.BLUE,
                                Concept.RED,
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
                                Concept.ORANGE,
                                Concept.BLUE,
                                Concept.RED,
                                Concept.GREEN,
                                Concept.GREEN
                        ),
                        ImmutableList.of(
                                Concept.TEAL,
                                Concept.ORANGE,
                                Concept.BLUE,
                                Concept.RED,
                                Concept.GREEN,
                                Concept.GREEN
                        ),
                        true
                }
        );
    }

    private ImmutableList<Concept> requiredConcepts;
    private ImmutableList<Concept> availableConcepts;
    private boolean pass;

    public ArrayValidatorTest(
            ImmutableList<Concept> requiredConcepts,
            ImmutableList<Concept> availableConcepts,
            boolean pass
    ) {
        this.requiredConcepts = requiredConcepts;
        this.availableConcepts = availableConcepts;
        this.pass = pass;
    }

    @Test
    public void testArrayValidator() {
        ArrayValidator arrayVal = new ArrayValidator(requiredConcepts);

        assertEquals("Required: " + requiredConcepts + ", available: " + availableConcepts, pass, CostValidator.validate(arrayVal, availableConcepts));
    }
}
