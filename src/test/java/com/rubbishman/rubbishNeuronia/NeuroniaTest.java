package com.rubbishman.rubbishNeuronia;

import com.rubbishman.rubbishRedux.external.RubbishContainer;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.CreateObject;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.external.operational.store.IdObject;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.external.setup.RubbishContainerCreator;
import com.rubbishman.rubbishRedux.external.setup.RubbishContainerOptions;
import com.rubbishman.rubbishNeuronia.action.EndTurn;
import com.rubbishman.rubbishNeuronia.action.PlayCard;
import com.rubbishman.rubbishNeuronia.reducer.NeuroniaReducer;
import com.rubbishman.rubbishNeuronia.state.InitialThoughtLocation;
import com.rubbishman.rubbishNeuronia.state.ThoughtLocationTransition;
import com.rubbishman.rubbishNeuronia.state.brain.Brain;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;
import com.rubbishman.rubbishNeuronia.state.card.experience.ConceptPlacement;
import com.rubbishman.rubbishNeuronia.state.card.experience.ExperienceCard;
import com.rubbishman.rubbishNeuronia.state.card.pathway.CardMovementComponent;
import com.rubbishman.rubbishNeuronia.state.card.pathway.Movement;
import com.rubbishman.rubbishNeuronia.state.card.pathway.PathwayCard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NeuroniaTest {
    public static Identifier brainId;

    private RubbishContainer rubbish;
    private StringBuilder stringBuilder = new StringBuilder();
    @Before
    public void setup() {
        RubbishContainerOptions options = new RubbishContainerOptions();

        options
                .setReducer(new NeuroniaReducer());

        rubbish = RubbishContainerCreator.getRubbishContainer(options);
    }

    @Test
    public void testNeuronia() {
        createBrain();
        Brain brain;

        createExperienceCard();
        assertEquals(1, rubbish.actionQueueSize());
        rubbish.performActions();
        assertEquals(6, rubbish.actionQueueSize());
        rubbish.performActions();

        createPathwayCard();
        assertEquals(1, rubbish.actionQueueSize());
        rubbish.performActions();
        assertEquals(8, rubbish.actionQueueSize());
        rubbish.performActions();

        brain = rubbish.getState().getObject(brainId);
        ThoughtLocationTransition curLoc = brain.currentThoughtLocation;

        assertEquals(2, curLoc.x);
        assertEquals(-2, curLoc.y);

        ThoughtLocationTransition thoughtLocTrans = curLoc.prev;

        assertEquals(2, thoughtLocTrans.x);
        assertEquals(-3, thoughtLocTrans.y);

        brain = rubbish.getState().getObject(brainId);

        assertEquals(1, brain.activeMemory.size());
        assertEquals(0, brain.conceptReserve.size());

        rubbish.performAction(new EndTurn(brainId));

        assertEquals(0, rubbish.getState().getObjectsByClass(ThoughtLocationTransition.class).size());

        brain = rubbish.getState().getObject(brainId);

        curLoc = brain.currentThoughtLocation;
        assertEquals(2, curLoc.x);
        assertEquals(-2, curLoc.y);
        assertNull(curLoc.prev);

        InitialThoughtLocation initLoc = brain.initialThoughtLocation;
        assertEquals(2, initLoc.x);
        assertEquals(-2, initLoc.y);

        brain = rubbish.getState().getObject(brainId);

        assertEquals(0, brain.activeMemory.size());
        assertEquals(1, brain.conceptReserve.size());
//        System.out.println(GsonInstance.getInstance().toJson(rubbish.getState()));
    }

    private void createBrain() {
        CreateObject<Brain> createObject = new CreateObject<>(new Brain(),
                new ICreateObjectCallback() {

                    public void postCreateState(Object object) {
                        if(object instanceof IdObject) {
                            brainId = ((IdObject)object).id;
                        }
                    }
                });

        rubbish.performAction(createObject);
    }

    private void createExperienceCard() {
        ExperienceCard experienceCard = new ExperienceCard(brainId, new ConceptPlacement[]{
            new ConceptPlacement(Concept.RED, 4, 5, 0),
            new ConceptPlacement(Concept.TEAL, -6, 3, 0),
            new ConceptPlacement(Concept.PURPLE, 5, -2, 0),
            new ConceptPlacement(Concept.ORANGE, -8, -3, 0),
            new ConceptPlacement(Concept.GREEN, 2, -2, 0),
            new ConceptPlacement(Concept.BLUE, 0, -1, 0)
        });

        final Identifier[] cardId = new Identifier[1];
        CreateObject<ExperienceCard> createObject = new CreateObject<>(experienceCard,
                new ICreateObjectCallback() {

                    public void postCreateState(Object object) {
                        if(object instanceof IdObject) {
                            cardId[0] = ((IdObject)object).id;
                            rubbish.addAction(new PlayCard(cardId[0]));
                        }
                    }
                });

        rubbish.performAction(createObject);
    }

    private void createPathwayCard() {
        PathwayCard pathwayCard = new PathwayCard(brainId, new CardMovementComponent[]{
                new CardMovementComponent(Movement.NORTH, false, 0),
                new CardMovementComponent(Movement.WEST, false, 0),
                new CardMovementComponent(Movement.NORTH, false, 0),
                new CardMovementComponent(Movement.EAST, false, 0),
                new CardMovementComponent(Movement.EAST, false, 0),
                new CardMovementComponent(Movement.NORTH, false, 0),
                new CardMovementComponent(Movement.EAST, false, 0),
                new CardMovementComponent(Movement.SOUTH, true, 0),
        });

        final Identifier[] cardId = new Identifier[1];
        CreateObject<PathwayCard> createObject = new CreateObject<>(pathwayCard,
            new ICreateObjectCallback() {

            public void postCreateState(Object object) {
                if(object instanceof IdObject) {
                    cardId[0] = ((IdObject)object).id;
                    rubbish.addAction(new PlayCard(cardId[0]));
                }
            }
        });

        rubbish.performAction(createObject);
    }
}
