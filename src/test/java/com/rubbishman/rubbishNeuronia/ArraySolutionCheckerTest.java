package com.rubbishman.rubbishNeuronia;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.ArraySolutionChecker;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.CostSolution;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.GsonInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

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
                    ImmutableList.of(new CostSolutionAssertion(
                            0,
                            3,
                            1,
                            1
                    ))
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
                    ImmutableList.of()
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
                    ImmutableList.of(new CostSolutionAssertion(
                            0,
                            5,
                            0,
                            1
                    ))
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
                    ImmutableList.of(
                        new CostSolutionAssertion(
                                0,
                                3,
                                0,
                                3
                        ),
                        new CostSolutionAssertion(
                                1,
                                3,
                                0,
                                3
                        )
                    )
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
                    ImmutableList.of(
                            new CostSolutionAssertion(
                                    0,
                                    3,
                                    0,
                                    4
                            ),
                            new CostSolutionAssertion(
                                    1,
                                    3,
                                    4,
                                    0
                            )
                    )
                }
            );
        }

        private ImmutableList<ConceptTrace> requiredConcepts;
        private ImmutableList<ConceptTrace> availableConcepts;
        private List<CostSolutionAssertion> solutionAssertions;

        public ArraySolutionCheckerTest(
                ImmutableList<ConceptTrace> requiredConcepts,
                ImmutableList<ConceptTrace> availableConcepts,
                List<CostSolutionAssertion> solutionAssertions
        ) {
            this.requiredConcepts = requiredConcepts;
            this.availableConcepts = availableConcepts;
            this.solutionAssertions = solutionAssertions;
        }

        @Test
        public void testArrayChecker() {
            List<CostSolution> solutions = ArraySolutionChecker.checkSolution(requiredConcepts, availableConcepts);

            assertEquals("Incorrect number of solutions, " + System.lineSeparator() + GsonInstance.getInstance().toJson(requiredConcepts)
                    + System.lineSeparator() + GsonInstance.getInstance().toJson(availableConcepts),
                    solutionAssertions.size(), solutions.size());

            for(CostSolutionAssertion costSolutionAssert: solutionAssertions) {
                assertEquals(
                "Incorrect solution number, " + System.lineSeparator() + GsonInstance.getInstance().toJson(requiredConcepts)
                        + System.lineSeparator() + GsonInstance.getInstance().toJson(availableConcepts),
                        costSolutionAssert.solutionNumber,
                        solutions.get(costSolutionAssert.solutionIndex).costSolution.size()
                );
                assertEquals(
                "Incorrect skipped number, " + System.lineSeparator() + GsonInstance.getInstance().toJson(requiredConcepts)
                        + System.lineSeparator()+ GsonInstance.getInstance().toJson(availableConcepts),
                        costSolutionAssert.skippedNumber,
                        solutions.get(costSolutionAssert.solutionIndex).skippedItems.size()
                );
                assertEquals(
                "Incorrect remaining number, " + System.lineSeparator() + GsonInstance.getInstance().toJson(requiredConcepts)
                        + System.lineSeparator() + GsonInstance.getInstance().toJson(availableConcepts),
                        costSolutionAssert.remainingNumber,
                        solutions.get(costSolutionAssert.solutionIndex).remainingItems.size()
                );
            }
        }

        private static class CostSolutionAssertion {
            public final int solutionIndex;
            public final int solutionNumber;
            public final int remainingNumber;
            public final int skippedNumber;

            private CostSolutionAssertion(int solutionIndex, int solutionNumber, int remainingNumber, int skippedNumber) {
                this.solutionIndex = solutionIndex;
                this.solutionNumber = solutionNumber;
                this.remainingNumber = remainingNumber;
                this.skippedNumber = skippedNumber;
            }
        }
}
