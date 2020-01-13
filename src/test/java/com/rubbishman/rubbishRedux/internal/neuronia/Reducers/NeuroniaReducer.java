package com.rubbishman.rubbishRedux.internal.neuronia.Reducers;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup.IRubbishReducer;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.CreatedObjectStore;
import com.rubbishman.rubbishRedux.internal.neuronia.actions.PathwayMovement;
import com.rubbishman.rubbishRedux.internal.neuronia.actions.EndTurn;
import com.rubbishman.rubbishRedux.internal.neuronia.actions.PlaceConcept;
import com.rubbishman.rubbishRedux.internal.neuronia.actions.PlayCard;
import com.rubbishman.rubbishRedux.internal.neuronia.state.Brain;
import com.rubbishman.rubbishRedux.internal.neuronia.state.CurrentThoughtLocation;
import com.rubbishman.rubbishRedux.internal.neuronia.state.InitialThoughtLocation;
import com.rubbishman.rubbishRedux.internal.neuronia.state.ThoughtLocationTransition;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.experience.ConceptPlacement;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.experience.ExperienceCard;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.pathway.PathwayCard;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.pathway.CardMovementComponent;
import com.rubbishman.rubbishRedux.internal.neuronia.state.concept.Concept;
import com.rubbishman.rubbishRedux.internal.neuronia.state.concept.ConceptLocation;

public class NeuroniaReducer extends IRubbishReducer {
    public static final Identifier curLocID = new Identifier(1, CurrentThoughtLocation.class);
    public static final Identifier initLocID = new Identifier(1, InitialThoughtLocation.class);

    private ObjectStore playCard(ObjectStore state, PlayCard playCard) {
        Object card = state.getObject(playCard.cardId);
        if(card instanceof PathwayCard) {
            playPathwayCard((PathwayCard)card);
        } else if(card instanceof ExperienceCard) {
            playExperienceCard((ExperienceCard)card);
        }

        // This would also do something to the card, IE remove from player hand, but we don't have state for that at the moment...!
        return state;
    }

    private void playExperienceCard(ExperienceCard experienceCard) {
        for(ConceptPlacement conceptPlacement: experienceCard.conceptPlacements) {
            if(conceptPlacement.costTier <= 1) { // This would check the player level...
                rubbishContainer.addAction(new PlaceConcept(experienceCard.brainId, conceptPlacement.type, conceptPlacement.x, conceptPlacement.y));
            }
        }
    }

    private void playPathwayCard(PathwayCard pathwayCard) {
        for(CardMovementComponent cmComp: pathwayCard.movements) {
            if(cmComp.costTier <= 1) { // This would check the player level...
                rubbishContainer.addAction(new PathwayMovement(
                        cmComp.movement,
                        cmComp.pickup
                ));
            }
        }
    }

    private ObjectStore cardThoughtMovement(ObjectStore state, PathwayMovement ctMovement) {
        CurrentThoughtLocation prevLocation = state.getObject(curLocID), curLocation = prevLocation;
        ThoughtLocationTransition locTransition;

        switch(ctMovement.move) {
            case NORTH:
                locTransition = new ThoughtLocationTransition(curLocation.x, curLocation.y - 1, ctMovement.pickup, prevLocation.thoughtLocationTransition);
                break;
            case EAST:
                locTransition = new ThoughtLocationTransition(curLocation.x + 1, curLocation.y, ctMovement.pickup, prevLocation.thoughtLocationTransition);
                break;
            case SOUTH:
                locTransition = new ThoughtLocationTransition(curLocation.x, curLocation.y + 1, ctMovement.pickup, prevLocation.thoughtLocationTransition);
                break;
            default: //WEST
                locTransition = new ThoughtLocationTransition(curLocation.x - 1, curLocation.y, ctMovement.pickup, prevLocation.thoughtLocationTransition);
                break;
        }

        Identifier transitionId = state.idGenerator.nextId(ThoughtLocationTransition.class);

        curLocation = new CurrentThoughtLocation(locTransition.x, locTransition.y, transitionId);

        state = state.setObject(curLocID, curLocation);
        state = state.setObject(transitionId, locTransition);

        return state;
    }

    private ObjectStore endTurn(ObjectStore state, EndTurn endTurn) {
        state = state.clearObjects(ThoughtLocationTransition.class);

        CurrentThoughtLocation prevLocation = state.getObject(curLocID);

        state = state.setObject(initLocID, new InitialThoughtLocation(prevLocation.x, prevLocation.y));
        state = state.setObject(curLocID, new CurrentThoughtLocation(prevLocation.x, prevLocation.y, null));

        return state;
    }

    private ObjectStore placeConcept(ObjectStore state, PlaceConcept placeConcept) {
        CreatedObjectStore createdObjectStoreConcept = state.createObject(new Concept(placeConcept.type));

        Brain brain = createdObjectStoreConcept.state.getObject(placeConcept.brainId);
        brain = brain.addConcept(createdObjectStoreConcept.createdObject.id, placeConcept.x, placeConcept.y);

        return createdObjectStoreConcept.state.setObject(placeConcept.brainId, brain);
    }

    public ObjectStore reduce(ObjectStore state, Object action) {
        if(action instanceof PathwayMovement) {
            return cardThoughtMovement(state, (PathwayMovement)action);
        } else if (action instanceof PlayCard) {
            return playCard(state, (PlayCard)action);
        } else if (action instanceof EndTurn) {
            return endTurn(state, (EndTurn)action);
        } else if (action instanceof PlaceConcept) {
            return placeConcept(state, (PlaceConcept)action);
        }
        return state;
    }
}
