package 串行流和并行流;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;

/**
 * Java1.8 并行流 优化使用 fork/join 框架  ,
 * 内部使用多线程 和窃取模式 实现大数据量的运算，效率比单线程高很多，而且会极大的充分利用cpu的资源
 */
public class TestForkJoin {

    //对一个100亿的数据进行累加：并且流使用的是parallel(),底层是利用FOrk/join框架实现的，具体请看笔记和代码ForkJoinCalculate
    @Test
    public void test1() {
        //rangeClosed是start和end
        //parallel()得到并行流
        //reduce进行规约运算，运算的方式是累加

        Instant start = Instant.now(); //当前系统时间 jdk8新方法
        LongStream.rangeClosed(0, 1000000000000L).parallel().reduce(0, Long::sum);
        Instant end = Instant.now();

        System.out.println("耗时为：：" + Duration.between(start, end).toMillis());
    }

    //如果使用单线程进行计算
    @Test
    public void test2() {
        Long sum = 0L;
        Instant start = Instant.now();
        for (int i = 0; i < 1000000000000L; i++) {
            sum += i;
        }
        Instant end = Instant.now();
        System.out.println("耗时为：：" + Duration.between(start, end).toMillis());
    }

}
