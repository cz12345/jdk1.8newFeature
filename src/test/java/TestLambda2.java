import lambda.MyFun;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.Comparator;
import java.util.function.Consumer;

/**
 * 一:Lambda 表达式基本语法：JDK1.8引入新的操作符 ："->"
 * "->"将代码分为两侧：
 * 左侧：参数列表 --->对应着接口中方法的参数列表，方法里几个参数则左侧就写几个参数
 * 右侧：执行功能的Lambda体 --->对应着接口中方法的实现，实现的功能
 * 疑问：假如家口有多个方法呢？这里lambda实现哪个？后面再详解
 * Lambda表达式其实就是接口的实现类
 * <p>
 * Lambda几种表达式:
 * (一):无参数，无返回值
 * () -> System.out.println("xxx");
 * (二):有一个参数，并且无返回值(注意当只有一个参数时，小括号可不写)
 * (三):有两个及以上的参数，并且Lambda体中有多条语句，并且有返回值(注意Lambda体中如果有多条语句，则右侧必须用{})
 * (四):若Lambda体中只有一条语句，则大括号{}和返回值都可以省略不写
 * (五):Lambda表达式的参数类型不用写，因为JVM可以根据目标上下文推出类型（不演示了）
 * <p>
 * 口诀：左右遇一括号省,左侧推断类型省
 * 二：Lambda表达式需要“函数式接口”
 * “函数式接口”：即接口中只有一个抽象方法，我们可以使用注解@FunctionalInterface 修饰来检查接口是否为函数式接口
 */
public class TestLambda2 {

    //表达式一
    @Test
    public void test1() {
        //用线程接口Runnable来举例
        //原来
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("燕燕切克闹");
            }
        };
        r.run();
        //现在用Lambda
        Runnable r1 = () -> System.out.println("Lambda表达式一");
        r1.run();
    }

    //表达式二
    @Test
    public void test2() {
        //以Consumer接口为例
        //原来
        Consumer<String> c = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        //用Lambda,左侧参数列表，名字随意s,x都行,右边是accept实现体，左侧accept方法的参数列表
        //Consumer<String> c1 = (s) -> System.out.println(s);
        //小括号可省略
        Consumer<String> c1 = s -> System.out.println(s);
        c1.accept("好好学习Lambda");
    }

    //表达式三
    @Test
    public void test3() {
        //以Comparator接口为例
        //原来
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        //Lambda (Lambda体中有多条语句，则右侧必须用{})
        Comparator<Integer> c1 = (x, y) -> {
            System.out.println("函数式接口");
            return Integer.compare(x, y);
        };

    }

    //表达式四
    @Test
    public void test4() {
        //以Comparator接口为例
        //原来
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        //Lambda (Lambda体中有一条语句，return和{}都可以省略)
        Comparator<Integer> c1 = (x, y) -> Integer.compare(x, y);

    }

    //函数式接口 ，加上该注解，则接口中只能有一个抽象方法
    @FunctionalInterface
    interface FunctionalInterfaceDemo {
        public void test();
//        public void yest();
    }

    //需求对数值进行计算操作,具体式加还是减，看你实现了
    public Integer getValue(Integer m, MyFun my) {
        return  my.getValue(m);
    }
    //使用Lambda调用上面的方法
    @Test
    public void test5() {
        //计算100的平方
        System.out.println(getValue(100,m -> m*m));
        //计算200加上500
        System.out.println(getValue(200,x -> x + 500));
    }
}
