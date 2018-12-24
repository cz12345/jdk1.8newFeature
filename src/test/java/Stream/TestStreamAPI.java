package Stream;

import lambda.domain.Employer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 一,Stream的三个操作步骤：
 *      1. 创建Stream
 *      2. 中间操作
 *      3. 终止操作（终端操作）
 */
public class TestStreamAPI {


    //创建Stream
    @Test
    public void test1() {
        //1.可以通过Collection系列集合提供的stream() 或 parallelStream()
        List<String> list = new ArrayList<>();
        Stream<String> stream = list.stream();

        //2. 可以通过Arrays中的静态方法stream()获取数组流
        Employer[] employers = new Employer[10];
        Stream<Employer> stream1 = Arrays.stream(employers);

        //3. 通过Stream类中的静态方法of()
        Stream<String> stream2 = Stream.of("aa", "bb", "cc");

        //4. 创建无限流
        //4.1 创建无限流第一种方式：迭代 iterate(final T seed, final UnaryOperator<T> f)
        Stream<Integer> stream3 = Stream.iterate(0, (x) -> x + 2);

        //终止操作，产生结果
//        stream3.forEach(System.out::println);
        //只取前10个
//        stream3.limit(10).forEach(System.out::println);//0 2 4 6 8 10 .....

        //4.2 创建无限流第二种方式：生成
        //generate(Supplier<T> s) ,
        //打印前5个随机数
        Stream.generate(() -> Math.random()*100 ).limit(5).forEach(System.out::println);


    }
}
