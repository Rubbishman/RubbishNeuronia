package com.rubbishman.rubbishNeuronia.CostValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.internal.neuronia.state.brain.Concept;
import com.rubbishman.rubbishRedux.internal.neuronia.state.cost.ArrayValidator;
import com.rubbishman.rubbishRedux.internal.neuronia.state.cost.SetValidator;

import java.util.ArrayList;

public class CostValidator {
    /*
        Set: {A,B,C}, all of, any order
        Array: [A,B,C], all of, specific order
        Step over: ^A, must walk over but not pick up
     */

    public static boolean validate(ArrayValidator arrayValidator, ImmutableList<Concept> availableConcepts) {
        for(int i = 0; i <= availableConcepts.size() - arrayValidator.requiredConcepts.size(); i++) {
            for(int k = 0; k < arrayValidator.requiredConcepts.size(); k++) {
                if(arrayValidator.requiredConcepts.get(k) != availableConcepts.get(i + k)) {
                    break; // No match...
                } else if(k == arrayValidator.requiredConcepts.size() - 1) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean validate(SetValidator setValidator, ImmutableList<Concept> availableConcepts) {
        ArrayList<Concept> searchConcepts = new ArrayList<>(setValidator.requiredConcepts.asList());

        for(Concept concept: availableConcepts) {
            searchConcepts.remove(concept);
        }

        return searchConcepts.size() == 0;
    }
}
