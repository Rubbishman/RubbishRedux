package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.IdObject;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.Identifier;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.statefullTimer.TimerExecutor;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class TimerExecutorTest {

    @Test
    public void testTimerExecutor() {
        StringBuilder stringBuilder = new StringBuilder();

        OutputStream myOutput = new OutputStream() {
            public void write(int b) throws IOException {
                stringBuilder.append((char)b);
            }
        };

        PrintStream printStream = new PrintStream(myOutput);

        MiddlewareEnhancer<ObjectStore> enhancer = new MiddlewareEnhancer<>();
        enhancer.addMiddleware(new MyLoggingMiddleware(printStream, "moo"));

        Store.Creator<ObjectStore> creator = new com.glung.redux.Store.Creator();

        creator = enhancer.enhance(creator);

        TimerExecutor timer = new TimerExecutor(creator);

        Long nowTime = 0l;

        timer.createTimer(nowTime,"ACTION", 100, 3);
        timer.createTimer(nowTime + 10,"ACTION2", 100, 4);
        timer.createTimer(nowTime + 20,"ACTION3", 100, 5);
        timer.createTimer(nowTime + 30,"ACTION4", 100, 5);
        timer.createTimer(nowTime + 40,"ACTION5", 100, 5);
        timer.createTimer(nowTime + 50,"ACTION6", 100, 5);

        timer.timerLogic(nowTime);

        assertEquals(6, timer.getState().objectMap.size());
        Iterator<IdObject> iter = timer.getState().objectMap.valIterator();
        while(iter.hasNext()) {
            IdObject obj = iter.next();
            assertEquals(0, ((RepeatingTimer)obj.object).currentRepeats);
        }

        timer.timerLogic(nowTime + 100);
        assertEquals(1, ((RepeatingTimer)timer.getState().objectMap.get(new Identifier(RepeatingTimer.class, 1l)).object).currentRepeats);

        timer.timerLogic(nowTime + 150);

        iter = timer.getState().objectMap.valIterator();
        while(iter.hasNext()) {
            IdObject obj = iter.next();
            assertEquals(1, ((RepeatingTimer)obj.object).currentRepeats);
        }

        assertEquals(
                "moo CreateObject {\"createObject\":{\"startTime\":0,\"period\":100,\"repeats\":3,\"currentRepeats\":0,\"action\":\"ACTION\"}}" +
                        "moo CreateObject {\"createObject\":{\"startTime\":10,\"period\":100,\"repeats\":4,\"currentRepeats\":0,\"action\":\"ACTION2\"}}" +
                        "moo CreateObject {\"createObject\":{\"startTime\":20,\"period\":100,\"repeats\":5,\"currentRepeats\":0,\"action\":\"ACTION3\"}}" +
                        "moo CreateObject {\"createObject\":{\"startTime\":30,\"period\":100,\"repeats\":5,\"currentRepeats\":0,\"action\":\"ACTION4\"}}" +
                        "moo CreateObject {\"createObject\":{\"startTime\":40,\"period\":100,\"repeats\":5,\"currentRepeats\":0,\"action\":\"ACTION5\"}}" +
                        "moo CreateObject {\"createObject\":{\"startTime\":50,\"period\":100,\"repeats\":5,\"currentRepeats\":0,\"action\":\"ACTION6\"}}" +
                        "moo IncrementTimer {\"nowTime\":100,\"subject\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer\"}}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer\"}}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer\"}}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":{\"id\":4,\"clazz\":\"com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer\"}}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":{\"id\":5,\"clazz\":\"com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer\"}}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":{\"id\":6,\"clazz\":\"com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer\"}}",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }
}
