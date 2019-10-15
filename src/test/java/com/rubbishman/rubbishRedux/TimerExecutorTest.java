package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.statefullTimer.TimerExecutor;
import com.rubbishman.rubbishRedux.statefullTimer.state.TimerState;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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

        MiddlewareEnhancer<TimerState> enhancer = new MiddlewareEnhancer<>();
        enhancer.addMiddleware(new MyLoggingMiddleware(printStream, "moo"));

        Store.Creator<TimerState> creator = new com.glung.redux.Store.Creator();

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

        assertEquals(6, timer.getState().timers.size());
        for(int i  = 0; i < timer.getState().timers.size(); i++) {
            assertEquals(0, timer.getState().timers.get(i).currentRepeats);
        }

        timer.timerLogic(nowTime + 100);
        assertEquals(1, timer.getState().timers.get(0).currentRepeats);

        timer.timerLogic(nowTime + 150);
        for(int i  = 0; i < timer.getState().timers.size(); i++) {
            assertEquals(1, timer.getState().timers.get(i).currentRepeats);
        }

        assertEquals(
                "moo CreateObject {\"createObject\":{\"id\":0,\"startTime\":0,\"period\":100,\"repeats\":3,\"currentRepeats\":0,\"action\":\"ACTION\"}}" +
                        "moo CreateObject {\"createObject\":{\"id\":0,\"startTime\":10,\"period\":100,\"repeats\":4,\"currentRepeats\":0,\"action\":\"ACTION2\"}}" +
                        "moo CreateObject {\"createObject\":{\"id\":0,\"startTime\":20,\"period\":100,\"repeats\":5,\"currentRepeats\":0,\"action\":\"ACTION3\"}}" +
                        "moo CreateObject {\"createObject\":{\"id\":0,\"startTime\":30,\"period\":100,\"repeats\":5,\"currentRepeats\":0,\"action\":\"ACTION4\"}}" +
                        "moo CreateObject {\"createObject\":{\"id\":0,\"startTime\":40,\"period\":100,\"repeats\":5,\"currentRepeats\":0,\"action\":\"ACTION5\"}}" +
                        "moo CreateObject {\"createObject\":{\"id\":0,\"startTime\":50,\"period\":100,\"repeats\":5,\"currentRepeats\":0,\"action\":\"ACTION6\"}}" +
                        "moo IncrementTimer {\"nowTime\":100,\"subject\":0}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":1}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":2}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":3}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":4}" +
                        "moo IncrementTimer {\"nowTime\":150,\"subject\":5}",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }
}
