package executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 提供单例线程池
 * 单例
 */
public class ExecutorServiceUtil {

    private ExecutorServiceUtil() {

    }

    public static int cores = Runtime.getRuntime().availableProcessors() > 4 ? 4 : Runtime.getRuntime().availableProcessors();

    private static class ExecutorServiceUtilHandler{

        private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("email-parser-pool-%d").build();

        private static ExecutorService threadPool = new ThreadPoolExecutor(cores + 2, cores + 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }



    public static ExecutorService getThreadPool() {
        return ExecutorServiceUtilHandler.threadPool;
    }

}
