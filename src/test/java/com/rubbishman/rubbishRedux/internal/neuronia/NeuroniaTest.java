package com.rubbishman.rubbishRedux.internal.neuronia;

import com.rubbishman.rubbishRedux.external.RubbishContainer;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.CreateObject;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.external.operational.store.IdObject;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.external.setup.RubbishContainerCreator;
import com.rubbishman.rubbishRedux.external.setup.RubbishContainerOptions;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.GsonInstance;
import com.rubbishman.rubbishRedux.internal.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.internal.neuronia.Reducers.NeuroniaReducer;
import com.rubbishman.rubbishRedux.internal.neuronia.actions.EndTurn;
import com.rubbishman.rubbishRedux.internal.neuronia.actions.PlayCard;
import com.rubbishman.rubbishRedux.internal.neuronia.state.CurrentThoughtLocation;
import com.rubbishman.rubbishRedux.internal.neuronia.state.InitialThoughtLocation;
import com.rubbishman.rubbishRedux.internal.neuronia.state.ThoughtLocationTransition;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.Card;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.CardMovementComponent;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.Movement;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NeuroniaTest {
    public static final Identifier curLocID = new Identifier(1, CurrentThoughtLocation.class);
    public static final Identifier initLocID = new Identifier(1, InitialThoughtLocation.class);

    private RubbishContainer rubbish;
    private PrintStream printStream;
    private StringBuilder stringBuilder = new StringBuilder();
    @Before
    public void setup() {
        RubbishContainerOptions options = new RubbishContainerOptions();
        setupStringLogger(options);

        options
                .setReducer(new NeuroniaReducer());

        rubbish = RubbishContainerCreator.getRubbishContainer(options);
    }

    public void setupStringLogger(RubbishContainerOptions options) {
        OutputStream myOutput = new OutputStream() {
            public void write(int b) {
                stringBuilder.append((char)b);
            }
        };

        printStream = new PrintStream(myOutput);

        options.addMiddleware(new MyLoggingMiddleware(printStream, "MOO"));
    }

    @Test
    public void testNeuronia() {
        createInitialThoughtLocation();
        createCard();
        assertEquals(1, rubbish.actionQueueSize());
        rubbish.performActions();
        assertEquals(6, rubbish.actionQueueSize());
        rubbish.performActions();
        CurrentThoughtLocation curLoc = rubbish.getState().getObject(curLocID);

        assertEquals(2, curLoc.x);
        assertEquals(-2, curLoc.y);

        assertEquals(6, rubbish.getState().getObjectsByClass(ThoughtLocationTransition.class).size());

        ThoughtLocationTransition thoughtLocTrans = rubbish.getState().getObject(curLoc.thoughtLocationTransition);
        thoughtLocTrans = rubbish.getState().getObject(thoughtLocTrans.prev);

        assertEquals(2, thoughtLocTrans.x);
        assertEquals(-3, thoughtLocTrans.y);

        rubbish.performAction(new EndTurn(initLocID, curLocID));

        assertEquals(0, rubbish.getState().getObjectsByClass(ThoughtLocationTransition.class).size());

        curLoc = rubbish.getState().getObject(curLocID);
        assertEquals(2, curLoc.x);
        assertEquals(-2, curLoc.y);
        assertNull(curLoc.thoughtLocationTransition);

        InitialThoughtLocation initLoc = rubbish.getState().getObject(initLocID);
        assertEquals(2, initLoc.x);
        assertEquals(-2, initLoc.y);
    }

    private void printThoughtTransition(ThoughtLocationTransition thoughtLocTrans) {
        System.out.println(GsonInstance.getInstance().toJson(thoughtLocTrans));
        while(thoughtLocTrans.prev != null) {
            thoughtLocTrans = rubbish.getState().getObject(thoughtLocTrans.prev);
            System.out.println(GsonInstance.getInstance().toJson(thoughtLocTrans));
        }
    }

    private void createInitialThoughtLocation() {
        InitialThoughtLocation initLocation = new InitialThoughtLocation(0,0);
        CurrentThoughtLocation curLocation = new CurrentThoughtLocation(0,0, null);

        rubbish.performAction(new CreateObject<>(initLocation));
        rubbish.performAction(new CreateObject<>(curLocation));
    }

    private void createCard() {
        Card card = new Card(new CardMovementComponent[]{
                new CardMovementComponent(Movement.NORTH, false, 0),
                new CardMovementComponent(Movement.NORTH, false, 0),
                new CardMovementComponent(Movement.EAST, false, 0),
                new CardMovementComponent(Movement.NORTH, false, 0),
                new CardMovementComponent(Movement.EAST, false, 0),
                new CardMovementComponent(Movement.SOUTH, true, 0),
        });

        final Identifier[] cardId = new Identifier[1];
        CreateObject<Card> createObject = new CreateObject<>(card,
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
