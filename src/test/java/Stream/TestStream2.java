package Stream;

import lambda.domain.Employer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 同意test1的可以看出Stream可以像sql来操作
 * 一：stream 的三个操作步骤
 *      1. 创建stream
 *      2. 中间操作
 *      3. 终止操作产生结果
 *
 */
public class TestStream2 {
    List<Employer> employers = Arrays.asList(
            new Employer("zhangsan", 18, 5535,null),
            new Employer("lisi", 19, 5238,null),
            new Employer("wangwu", 25, 3380,Employer.Status.BUSY),
            new Employer("zhaoliu", 26, 6380,Employer.Status.VOCATION),
            new Employer("sunqi", 26, 6780,null),
            new Employer("erba", 27, 7780,null),
            new Employer("erba", 27, 7780,null),
            new Employer("erba", 27, 7780,null),
            new Employer("ll", 24, 4780,null)
    );

    //
        /**
         *1.中间操作:
         *    1.1筛选与切片：{
         *         filter-接受Lambda，从流中排出某些元素
         *         limit-截断流，使其元素不超过给定的数量
         *         skip(n)-跳过元素，返回一个扔掉了前n个元素的流。若六中元素不足n个，则返回一个空流，与limit(n)互补
         *         distinct-筛选，通过流所生成的hashcode和equals去除重复的元素
         *     }
         *  结论：中间操作不会产生任何结果，只有出发终止操作才可以产生结果
         *
         *    1.2 映射
         *      map--接受Lambda，将元素转换成其他形式或提取信息，接受一个函数作为参数，该函数会被应用到每个元素上，并将其映射成
         *      一个新的元素。
         *
         *      flatMap -- 接受一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
         *
         *    1.3 排序
         *      sorted(Comparable) -自然排序
         *      sorted(Comparator com) -定制排序
         *
         *
         */

     //中间操作之filter(Predicate<? super T> predicate);
    @Test
    public void test1() {


        //产生Stream
        //中间操作(流水线操作，过滤，筛选，limit都随便)
        //filter(Predicate<? super T> predicate);参数是Predicate函数式接口，即断言型接口，也可以看到内部循环迭代是Stream（流）内部自动迭代的
        Stream<Employer> stream = employers.stream().filter((e) -> {
            System.out.println("中间操作");
            return e.getAge() > 25;
        });
        //注意：中间操作不会产生结果，你可以在此运行test1会没有任何打印结果的,中间操作只会产生新的流，
        // 新的流里面会有一系列对数据流水线操作的过程，特别要注意，流是不会存储数据的，集合数组是存储
        // 数据的，并且数据源在经过流计算后，对原数据源无任何影响，只是相当于copy一个副本

        //终止操作
        stream.forEach(System.out::println);
        //打印结果：

//        中间操作
//                中间操作
//        中间操作
//                中间操作
//        Employer{name='zhaoliu', age=26, salary=6380}
//        中间操作
//        Employer{name='sunqi', age=26, salary=6780}
//        中间操作
//        Employer{name='erba', age=27, salary=7780}
//        中间操作

        //我们发现中间操作，当没有进行对流有limit的操作的时候，发现内部迭代此时是根据数据源即这里的list的长度决定的，长度多少，
        //就会迭代多少次，就会打印 “中间操作” 多少次，但是只有age 大于25得才会返回给新的流，之后新的流是满足年龄大于25的，之后
        //利用终止操作forEach(System.out::println); 打印出来 ；注意：流是没有存储数据的功能，不要误解，而且中间操作不会对数据源
        //产生任何影响


    }




    //中间操作: limit 只返回前3个，工资大于3000的新的流
    @Test
    public void test2() {
        //创建流和中间操作
        Stream<Employer> stream = employers.stream().filter((e) -> {
            System.out.println("中间操作limt");
            return e.getSalary() > 5000;
        }).limit(3);
        //终止操作产生结果
        stream.forEach(System.out::println);
        //打印结果：
//        中间操作limt
//        Employer{name='zhangsan', age=18, salary=5535}
//        中间操作limt
//        Employer{name='lisi', age=19, salary=5238}
//        中间操作limt
//                中间操作limt
//        Employer{name='zhaoliu', age=26, salary=6380}

          //如果使用limit的话，是这样的原理，比如这里limit(3)，他是只要找到满足条件的前三个就停止内部迭代，所以你看到的打印
        //"中间操作limit"只有四个，因为看Employer的集合，首先迭代打印 “中间操作limt”，然后发现第一个满足工资大于5000，然后继续
        //迭代打印 "中间操作limt",并且发现第二个也满足大于5000，以此类推，当发现满足的个数到达三个时，也不在继续向下循环找了，有
        //一定的优化功能

    }


    //skip(n)-跳过元素，返回一个扔掉了前n个元素的流。若六中元素不足n个，则返回一个空流，与limit(n)互补
    @Test
    public void test3() {
        Stream<Employer> stream = employers.stream().filter((e) -> {
            System.out.println("中间操作skip");
            return e.getSalary() > 5000;
        }).skip(2);
//        stream.forEach(System.out::println);
//        中间操作skip
//                中间操作skip
//        中间操作skip
//                中间操作skip
//        Employer{name='zhaoliu', age=26, salary=6380}
//        中间操作skip
//        Employer{name='sunqi', age=26, salary=6780}
//        中间操作skip
//        Employer{name='erba', age=27, salary=7780}
//        中间操作skip

        //结论：会跳过前两个满足条件的
    }

//    distinct-筛选，通过流所生成的hashcode和equals去除重复的元素
    @Test
    public void test4() {
        Stream<Employer> stream = employers.stream().filter((e) -> {
            System.out.println("中间操作skip");
            return e.getSalary() > 5000;
        }).skip(2).distinct();

        stream.forEach(System.out::println);
        //注意啊：Employer要重写equals和hashcode才行
    }


    // --------------------映射 -------------------

    // map--接受Lambda，将元素转换成其他形式或提取信息，接受一个函数作为参数，该函数会被应用到每个元素上，并将其映射成
    //  一个新的元素。
    @Test
    public void test5() {
        //将每个元素转为大写
         List<String> list = Arrays.asList("aa","bb","cc","d");
         list.stream().map((s) -> s.toUpperCase()).forEach(System.out::println);
         //打印结果
//        AA
//        BB
//        CC
//        D
        //只取集合中的Name 注意：Employer::getName采用的是类::实例方法·引用
        employers.stream().map(Employer::getName).forEach(System.out::println);
    }

    //flatMap -- 接受一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
    @Test
    public void test6() {
        List<String> list = Arrays.asList("hello","name","stream","api");
        //原来用map操作，将字符串转为字符,map里面调用test7Filter方法，map返回是一个stream
        Stream<Stream<Character>> streamStream = list.stream().map(TestStream2::test7Filter);
        //注意返回值，是Stream<Stream<Character>>类型，为什么？
        //因为，首先外面的stream是map的返回值类型，二里面的泛型任然是一个stream流，是调用test7Filter返回的结果,再里面是Character
        //就相当于Map<Map<String,String>>，每个字符串调用test7Filter并且返回一个流存在外面的大流，所以是流里面又存在许多流
        //那么流套流，所以在打印的时候，必须得嵌套遍历两次才可以
        streamStream.forEach((stream) -> stream.forEach(System.out::println));

        //这是用map的过程，但是我们用flatMap，就不用嵌套打印了，他内部，将返回的许多小流，全部拼接成一个流，返回，
        //就像是把集合中还有很多的集合全部拼接一个集合，然后只返回这个拼接好的集合
        Stream<Character> characterStream = list.stream().flatMap(TestStream2::test7Filter);
        characterStream.forEach(System.out::println);
    }

    //定义一个方法，参数为字符串str，将字符串转为字符，并且将字符加入到集合中，最后转为stream流返回
    public static Stream<Character> test7Filter(String str) {
        List<Character> list = new ArrayList<>();
        for (Character ac : str.toCharArray()) {
            list.add(ac);
        }
        return  list.stream();
    }

    // --------------------------------------   1.3 排序    -----------------------
    //自然排序  sorted(Comparable) -自然排序
    @Test
    public void test7() {
        List<String> list = Arrays.asList("gh","ab","io","cf");
        //字符串已经对Comparable接口进行实现了
        list.stream().sorted().forEach(System.out::println);

    }
    //定制排序  sorted(Comparator) 得实现Comparator接口
    @Test
    public void test8() {
        Stream<Employer> strame = employers.stream().sorted((e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return e1.getAge().compareTo(e2.getAge());
            }
        });
        strame.forEach(System.out::println);
    }

}
