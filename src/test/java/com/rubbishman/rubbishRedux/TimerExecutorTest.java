package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.statefullTimer.executor.TimerExecutor;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
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
        enhancer.addMiddleware(new MyLoggingMiddleware(System.out, "moo"));

        Store.Creator<TimerState> creator = new com.glung.redux.Store.Creator();

        creator = enhancer.enhance(creator);

        TimerExecutor timer = new TimerExecutor(creator);

        RepeatingTimer repeatingTimer1 = timer.createTimer("ACTION", 100, 3);
        RepeatingTimer repeatingTimer2 = timer.createTimer("ACTION2", 100, 4);
        RepeatingTimer repeatingTimer3 = timer.createTimer("ACTION3", 100, 5);
        timer.createTimer("ACTION4", 100, 5);
        timer.createTimer("ACTION5", 100, 5);
        timer.createTimer("ACTION6", 100, 5);

//        timer.startTimer();
        timer.rubbish();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        assertEquals("moo [CREATE:RepeatingTimer { id: 0 startTime: " + repeatingTimer1.timer.startTime + " period: 100 currentRepeats: 0 repeats: 5 action: ACTION]",
//                stringBuilder.toString()/*.replaceAll(System.lineSeparator(), "")*/);

        System.out.println(timer.getState());
    }
}
