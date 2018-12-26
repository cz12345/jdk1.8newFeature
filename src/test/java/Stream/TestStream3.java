package Stream;

import lambda.domain.Employer;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 终止操作：
 *    1. 查找与匹配
 *      allMatch - 检查是否匹配所有的元素
 *      anyMatch - 检查是否至少匹配一个元素
 *      noneMatch - 检查是否没有匹配所有元素
 *      findFirst - 返回第一个元素
 *      findAny - 返回当前六中的任意元素
 *      count - 返回流中元素的总个数
 *      max - 返回流中最大值
 *      min - 返回流中最小值
 *
 *    2. 规约 -
 *      reduce(T identity,BinaryOperator) 可以将流中元素反复结合起来，得到一个值 可以将流中元素反复结合起来，得到一个值。 返回 T
 *      reduce(BinaryOperator b) 可以将流中元素反复结合起来，得到一个值。 返回 Optional<T>
 *
 *    3. 收集 - 将流转换为其他形式。接收一个 Collector接口的 实现，用于给Stream中元素做汇总的方法
 *      Collector 接口中方法的实现决定了如何对流执行收集操作(如收
 *      集到 List、Set、Map)。但是 Collectors 实用类提供了很多静态
 *      方法，可以方便地创建常见收集器实例，说白了Collectors工具类很多帮我们已经实现了Collector接口的方法
 */
public class TestStream3 {
    List<Employer> employers = Arrays.asList(
            new Employer("zhangsan", 18, 5535,Employer.Status.FREE),
            new Employer("wangwu", 25, 3380,Employer.Status.BUSY),
            new Employer("zhaoliu", 26, 6380,Employer.Status.VOCATION),
            new Employer("sunqi", 26, 6780,Employer.Status.BUSY),
            new Employer("ll", 24, 4780,Employer.Status.VOCATION),
            new Employer("ll", 24, 4780,Employer.Status.VOCATION)
    );

    // 1. 查找与匹配
    @Test
    public void test1() {
        // allMatch - 检查是否匹配所有的元素 ,检查流中所有元素是不是都等于BUSY
        boolean b = employers.stream().allMatch((e) -> e.getStatus().equals(Employer.Status.BUSY)); //false
        // anyMatch - 检查是否至少匹配一个元素
        employers.stream().allMatch((e) -> e.getStatus().equals(Employer.Status.BUSY)); //true
        // noneMatch - 检查是否没有匹配所有元素 ，是不是一个都没有等于BUSY的
        employers.stream().noneMatch((e) -> e.getStatus().equals(Employer.Status.BUSY)); //false
        // findFirst - 返回第一个元素,首先按照工资从小到大排序（默认升序），然后取第一个，也就是工资最少的
        //注意返回值为为Optional<Employer>这个，Optional可以处理空指针异常，
        Optional<Employer> first = employers.stream().sorted((e1, e2) -> Integer.compare(e1.getSalary(), e2.getSalary())).findFirst();
        Supplier<Employer> e = Employer::new;
        //    Optional的orElse表示如果first流中返回的结果为null，则用参数来代替，类似于sql中的IFNULL（a）
        first.orElse(e.get());
        Employer employer = first.get();
        System.out.println(employer);
        // findAny - 返回当前流中的任意元素
        Optional<Employer> any = employers.stream().filter((e1) -> e1.getStatus().equals(Employer.Status.BUSY)).findAny();
        Employer employer1 = any.get();

        // count - 返回流中元素的总个数
        long count = employers.stream().count();
        System.out.println(count);
        // max - 返回流中最大值 ,返回最大值，按照工资作为标准
        Optional<Employer> max = employers.stream().max((e1, e2) -> Integer.compare(e1.getSalary(), e2.getSalary()));
        System.out.println(max.get());
        // min - 返回流中最小值,可以用Map，只取工资，然后按照工资作为标准，取最小值，只返回Salary,注意写法啊，全是方法引用
        Optional<Integer> min = employers.stream().map(Employer::getSalary).min(Integer::compare);
        System.out.println(min.get());

    }

    // 2. reduce(T identity,BinaryOperator) / reduce(BinaryOperator)
    // map和reduce的连接用法是最常用的，通常称为map-reduce模式
    @Test
    public void test2() {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        //是将集合中的数组每个元素相加，第一个参数是求和的初始值为0,
        //注意reduce的第二个参数是一个函数式接口，使用lambda对其接口的方法进行实现，
        //逻辑是首先拿0作为x的参数值，然后取集合的第一个元素作为参数的y的值，然后相加，相加的结果
        //依然作为x的值，再次从集合中取第二个值作为参数y的值，相加，以此类推，注意reduce的参数类型和返回值值类型
        //是根据数据源的泛型决定的，Interger,Double,Lon,Float。。。。
        Integer reduce = list.stream().reduce(0, (x, y) -> x + y);
        System.out.println(reduce);

        //将Employer中工资求和
        Integer sum = employers.stream().map(Employer::getSalary).reduce(0, (x, y) -> x + y);
        // == 下面
        Optional<Integer> sum1 = employers.stream().map(Employer::getSalary).reduce(Integer::sum);
        //这种结果跟上面一样，只不过下面是reduce的另一个重载方法，reduce(BinaryOperator bin),里面的Integer::sum
        //是jdk1.8里Integer的static的求和方法，注意返回值为Optional,因为没有默认值了，所以返回的结果可能为null
        System.out.println("工资sum:" + sum);
        System.out.println("工资sum1："+sum1.get());
    }

    // 3. 收集 - 将流转换为其他形式。接收一个 Collector接口的 实现，用于给Stream中元素做汇总的方法
    @Test
    public void test3() {
        // toList 可以经过流计算过，将新的结果加入到新的集合List中返回
        //提取所有的名字将所有的名字添加到list中返回,注意collect(Collectors.toList())方法,返回是一个List
        List<String> list = employers.stream().map(Employer::getName).collect(Collectors.toList());
        list.forEach(System.out::println);
        System.out.println("--------------------------");
        //想转为set 那就toSet,还可以去重,注意如果去重的是一个对象得实现equals和hashcode，这是去重的字符串就不需要了
        Set<String> set = employers.stream().map(Employer::getName).collect(Collectors.toSet());
        set.forEach(System.out::println);

        //如果想把收集的元素加入到自己创建的集合中，比如ArratList，HashSet，LinkedHashSet等等,但不能是Map集合
        ArrayList<String> arrayList = employers.stream().map(Employer::getName).collect(Collectors.toCollection(ArrayList::new));
        HashSet<String> hashSet = employers.stream().map(Employer::getName).collect(Collectors.toCollection(HashSet::new));
        System.out.println("--------------------------");

        // 统计总数
        Long count = employers.stream().collect(Collectors.counting());
        System.out.println(count);
        System.out.println("---------------------------");
        //统计满足条件的总数,两种写法都行
        Long count1 = employers.stream().filter((e) -> e.getSalary() > 4000).collect(Collectors.counting());
        Long count2 = employers.stream().filter((e) -> e.getSalary() > 4000).count();
        System.out.println("count1:"+count1);
        System.out.println("count2:"+count1);

        // 平均数，按工资求,注意这里不能写map了，要写在平均值的参数里面，返回值为double
        employers.stream().collect(Collectors.averagingDouble(Employer::getSalary));
        //averagingDouble的参数类型是ToDoubleFunction的函数式接口，并且接口中方法是一个参数，并且只有一个返回值，
        //所以无参数的，类::实例方法 的引用是没毛病的

        //总和 ，按工资求和
        Integer collect = employers.stream().collect(Collectors.summingInt(Employer::getSalary));

        //最大值,以工资为标准
        Optional<Employer> optionalEmployer = employers.stream().collect(Collectors.maxBy((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())));
        System.out.println(optionalEmployer.get());
        //最小值，
        Optional<Integer> min = employers.stream().map(Employer::getSalary).collect(Collectors.minBy(Integer::compareTo));
        System.out.println(min);

        //分组 ，按状态分组
        Map<Employer.Status,List<Employer>> grouBy1 =  employers.stream().collect(Collectors.groupingBy(Employer::getStatus));
        //打印发现状态一样的为一组
        // 多级分组，先按状态分组，再按年龄段分组：年龄段为，青年，中年，老年
        Map<Employer.Status,Map<String,List<Employer>>> map = employers.stream().collect(Collectors.groupingBy(Employer::getStatus,Collectors.groupingBy((e) -> {
            //注意需要对e进行强转一下
            if(((Employer)e).getAge() <= 35) {
                return "青年";
            } else if (((Employer)e).getAge() <= 50) {
                return "中年";
            } else
                return "老年";
        })));
        System.out.println("-------------------------------------");
        //分区 ，以工资为8000为界限开始分区
        Map<Boolean, List<Employer>> collect1 = employers.stream().collect(Collectors.partitioningBy((e) -> e.getSalary() > 8000));
        //打印
        System.out.println(collect1);
        System.out.println("--------------------------------------");
        //join拼接
        String collect2 = employers.stream().map(Employer::getName).collect(Collectors.joining(","));
        System.out.println(collect2);
        //打印·zhangsan,wangwu,zhaoliu,sunqi,ll,ll

    }

}
