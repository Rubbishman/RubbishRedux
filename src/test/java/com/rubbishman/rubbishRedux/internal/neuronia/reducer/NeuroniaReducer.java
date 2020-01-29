package com.rubbishman.rubbishRedux.internal.neuronia.reducer;

import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup.IRubbishReducer;
import com.rubbishman.rubbishRedux.internal.neuronia.action.PathwayMovement;
import com.rubbishman.rubbishRedux.internal.neuronia.action.EndTurn;
import com.rubbishman.rubbishRedux.internal.neuronia.action.PlaceConcept;
import com.rubbishman.rubbishRedux.internal.neuronia.action.PlayCard;
import com.rubbishman.rubbishRedux.internal.neuronia.state.brain.Brain;
import com.rubbishman.rubbishRedux.internal.neuronia.state.ThoughtLocationTransition;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.experience.ConceptPlacement;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.experience.ExperienceCard;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.pathway.PathwayCard;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.pathway.CardMovementComponent;

public class NeuroniaReducer extends IRubbishReducer {
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
                        pathwayCard.brainId,
                        cmComp.movement,
                        cmComp.pickup
                ));
            }
        }
    }

    private ObjectStore cardThoughtMovement(ObjectStore state, PathwayMovement ctMovement) {
        Brain brain = state.getObject(ctMovement.brainId);
        ThoughtLocationTransition prevLocation = brain.currentThoughtLocation, curLocation = prevLocation;
        ThoughtLocationTransition locTransition;

        switch(ctMovement.move) {
            case NORTH:
                locTransition = new ThoughtLocationTransition(curLocation.x, curLocation.y - 1, ctMovement.pickup, prevLocation.prev);
                break;
            case EAST:
                locTransition = new ThoughtLocationTransition(curLocation.x + 1, curLocation.y, ctMovement.pickup, prevLocation.prev);
                break;
            case SOUTH:
                locTransition = new ThoughtLocationTransition(curLocation.x, curLocation.y + 1, ctMovement.pickup, prevLocation.prev);
                break;
            default: //WEST
                locTransition = new ThoughtLocationTransition(curLocation.x - 1, curLocation.y, ctMovement.pickup, prevLocation.prev);
                break;
        }

        if(ctMovement.pickup) {
            if(brain.hasConcept(locTransition.x, locTransition.y)) {
                brain = brain.pickupConcept(locTransition.x, locTransition.y);
                state = state.setObject(ctMovement.brainId, brain);
            }
        }

        curLocation = new ThoughtLocationTransition(locTransition.x, locTransition.y, ctMovement.pickup, prevLocation);

        state = state.setObject(ctMovement.brainId, brain.setCurrentLocation(curLocation));

        return state;
    }

    private ObjectStore endTurn(ObjectStore state, EndTurn endTurn) {
        Brain brain = state.getObject(endTurn.brainId);

        state = state.clearObjects(ThoughtLocationTransition.class);

        state = state.setObject(endTurn.brainId, brain.endTurn());

        return state;
    }

    private ObjectStore placeConcept(ObjectStore state, PlaceConcept placeConcept) {
        Brain brain = state.getObject(placeConcept.brainId);
        brain = brain.addConcept(placeConcept.type, placeConcept.x, placeConcept.y);

        return state.setObject(placeConcept.brainId, brain);
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
