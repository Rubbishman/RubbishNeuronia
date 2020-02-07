package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;
import com.rubbishman.rubbishNeuronia.state.cost.ArrayValidator;
import com.rubbishman.rubbishNeuronia.state.cost.SetValidator;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

import java.util.ArrayList;

public class CostValidator {
    /*
        Set: {A,B,C}, all of, any order
        Array: [A,B,C], all of, specific order
        Step over: ^A, must walk over but not pick up
     */

    //TODO, these need to return the remaining ConceptTraces so that we can chain multiple validation's together.

    public static boolean validate(ArrayValidator arrayValidator, ImmutableList<ConceptTrace> availableConcepts) {
        for(int i = 0; i <= availableConcepts.size() - arrayValidator.requiredConcepts.size(); i++) {
            for(int k = 0; k < arrayValidator.requiredConcepts.size(); k++) {
                ConceptTrace conTrace = availableConcepts.get(i + k);
                if(arrayValidator.requiredConcepts.get(k) != conTrace.concept && conTrace.pickup) {
                    break; // No match...
                } else if(k == arrayValidator.requiredConcepts.size() - 1) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean validate(SetValidator setValidator, ImmutableList<ConceptTrace> availableConcepts) {
        ArrayList<Concept> searchConcepts = new ArrayList<>(setValidator.requiredConcepts.asList());

        for(ConceptTrace concept: availableConcepts) {
            searchConcepts.remove(concept.concept);
        }

        return searchConcepts.size() == 0;
    }
}
