package com.rubbishman.rubbishNeuronia;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.ArraySolutionChecker;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.CostSolution;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.CostValidator;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;
import com.rubbishman.rubbishNeuronia.state.cost.ArrayValidator;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.CostValidator;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;
import com.rubbishman.rubbishNeuronia.state.cost.ArrayValidator;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ArraySolutionCheckerTest {

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return ImmutableList.of(
                    new Object[] {
                            ImmutableList.of(
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.RED.pickup()
                            ),
                            ImmutableList.of(
                                    Concept.TEAL.pickup(),
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.RED.pickup(),
                                    Concept.GREEN.pickup()
                            ),
                            1
                    },
                    new Object[] {
                            ImmutableList.of(
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.RED.pickup(),
                                    Concept.GREEN.pickup(),
                                    Concept.GREEN.pickup()
                            ),
                            ImmutableList.of(
                                    Concept.TEAL.pickup(),
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.RED.pickup(),
                                    Concept.GREEN.pickup()
                            ),
                            0
                    },
                    new Object[] {
                            ImmutableList.of(
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.RED.pickup(),
                                    Concept.GREEN.pickup(),
                                    Concept.GREEN.pickup()
                            ),
                            ImmutableList.of(
                                    Concept.TEAL.pickup(),
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.RED.pickup(),
                                    Concept.GREEN.pickup(),
                                    Concept.GREEN.pickup()
                            ),
                            1
                    },
                    new Object[] {
                            ImmutableList.of(
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.stepOver(),
                                    Concept.RED.pickup()
                            ),
                            ImmutableList.of(
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.stepOver(),
                                    Concept.RED.stepOver(),
                                    Concept.BLUE.stepOver(),
                                    Concept.RED.pickup()
                            ),
                            2
                    },
                    new Object[] {
                            ImmutableList.of(
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.RED.pickup()
                            ),
                            ImmutableList.of(
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.RED.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.ORANGE.pickup(),
                                    Concept.BLUE.pickup(),
                                    Concept.RED.pickup()
                            ),
                            2
                    }
            );
        }

        private ImmutableList<ConceptTrace> requiredConcepts;
        private ImmutableList<ConceptTrace> availableConcepts;
        private int solutionNumber;

        public ArraySolutionCheckerTest(
                ImmutableList<ConceptTrace> requiredConcepts,
                ImmutableList<ConceptTrace> availableConcepts,
                int solutionNumber
        ) {
            this.requiredConcepts = requiredConcepts;
            this.availableConcepts = availableConcepts;
            this.solutionNumber = solutionNumber;
        }

        @Test
        public void testArrayChecker() {
            List<CostSolution> solutions = ArraySolutionChecker.checkSolution(requiredConcepts, availableConcepts);

            assertEquals("Required: " + requiredConcepts + ", available: " + availableConcepts, solutionNumber, solutions.size());
        }
}
