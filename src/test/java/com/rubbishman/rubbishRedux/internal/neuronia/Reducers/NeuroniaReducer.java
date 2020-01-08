package com.rubbishman.rubbishRedux.internal.neuronia.Reducers;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup.IRubbishReducer;
import com.rubbishman.rubbishRedux.internal.neuronia.actions.CardThoughtMovement;
import com.rubbishman.rubbishRedux.internal.neuronia.actions.EndTurn;
import com.rubbishman.rubbishRedux.internal.neuronia.actions.PlayCard;
import com.rubbishman.rubbishRedux.internal.neuronia.state.CurrentThoughtLocation;
import com.rubbishman.rubbishRedux.internal.neuronia.state.InitialThoughtLocation;
import com.rubbishman.rubbishRedux.internal.neuronia.state.ThoughtLocationTransition;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.Card;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.CardMovementComponent;

public class NeuroniaReducer extends IRubbishReducer {
    public static final Identifier curLocID = new Identifier(1, CurrentThoughtLocation.class);
    public static final Identifier initLocID = new Identifier(1, InitialThoughtLocation.class);

    public ObjectStore playCard(ObjectStore state, PlayCard playCard) {
        Card card = state.getObject(playCard.cardId);
        for(CardMovementComponent cmComp: card.movements) {
            if(cmComp.costTier <= 1) { // This would check the player level...
                rubbishContainer.addAction(new CardThoughtMovement(
                        cmComp.movement,
                        cmComp.pickup
                ));
            }
        }
        // This would also do something to the card, IE remove from player hand, but we don't have state for that at the moment...!
        return state;
    }

    public ObjectStore cardThoughtMovement(ObjectStore state, CardThoughtMovement ctMovement) {
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

    public ObjectStore endTurn(ObjectStore state, EndTurn endTurn) {
        state = state.clearObjects(ThoughtLocationTransition.class);

        CurrentThoughtLocation prevLocation = state.getObject(curLocID);

        state = state.setObject(initLocID, new InitialThoughtLocation(prevLocation.x, prevLocation.y));
        state = state.setObject(curLocID, new CurrentThoughtLocation(prevLocation.x, prevLocation.y, null));

        return state;
    }

    public ObjectStore reduce(ObjectStore state, Object action) {
        if(action instanceof CardThoughtMovement) {
            return cardThoughtMovement(state, (CardThoughtMovement)action);
        } else if (action instanceof PlayCard) {
            return playCard(state, (PlayCard)action);
        } else if (action instanceof EndTurn) {
            return endTurn(state, (EndTurn)action);
        }
        return state;
    }
}
