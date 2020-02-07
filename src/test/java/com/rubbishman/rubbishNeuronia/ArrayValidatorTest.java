package com.rubbishman.rubbishNeuronia;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.CostValidator;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;
import com.rubbishman.rubbishNeuronia.state.cost.ArrayValidator;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

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
                                Concept.TEAL.pickup(),
                                Concept.ORANGE.pickup(),
                                Concept.BLUE.pickup(),
                                Concept.RED.pickup(),
                                Concept.GREEN.pickup()
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
                                Concept.TEAL.pickup(),
                                Concept.ORANGE.pickup(),
                                Concept.BLUE.pickup(),
                                Concept.RED.pickup(),
                                Concept.GREEN.pickup()
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
                                Concept.TEAL.pickup(),
                                Concept.ORANGE.pickup(),
                                Concept.BLUE.pickup(),
                                Concept.RED.pickup(),
                                Concept.GREEN.pickup(),
                                Concept.GREEN.pickup()
                        ),
                        true
                }
        );
    }

    private ImmutableList<Concept> requiredConcepts;
    private ImmutableList<ConceptTrace> availableConcepts;
    private boolean pass;

    public ArrayValidatorTest(
            ImmutableList<Concept> requiredConcepts,
            ImmutableList<ConceptTrace> availableConcepts,
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
