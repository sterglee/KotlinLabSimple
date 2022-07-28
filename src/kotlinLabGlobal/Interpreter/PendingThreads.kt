package kotlinLabGlobal.Interpreter

import kotlinLabGlobal.Interpreter.PendingThreads
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// keeps the most recent threads in order to be able to cancel tasks
public class PendingThreads {
     val maxThreadsToKeep = 50

    // we keep the most recently executed maxThreadsToKeep
    var recentThreads = arrayOfNulls<Thread>(maxThreadsToKeep)
    var insertCursor = 0 // cursor that points where to insert a new thread. 
    var nthreads = 16
    var pool = Executors.newFixedThreadPool(nthreads)

    // add a new thread
    fun addThread(thread: Thread?) {
        recentThreads[insertCursor] = thread
        insertCursor++ // increment current insert location
        if (insertCursor == maxThreadsToKeep) insertCursor = 0
    }

    // cancel all pending threads
    fun cancelPendingThreads() {
        var cntThreadsCanceled = 0
        for (i in 0 until maxThreadsToKeep) if (recentThreads[i] != null) {
            val examinedThread = recentThreads[i]
            //System.out.println("thread["+i+"] state = "+examinedThread.getState().toString());
            if (examinedThread!!.state != Thread.State.TERMINATED) {
                examinedThread.stop() // cancel the thread
                cntThreadsCanceled++
            }
            recentThreads[i] = null
        }
        println("\nCancelled $cntThreadsCanceled  threads")
    }
}