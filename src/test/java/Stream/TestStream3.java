package Stream;

import lambda.domain.Employer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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
 *
 */
public class TestStream3 {
    List<Employer> employers = Arrays.asList(
            new Employer("zhangsan", 18, 5535,Employer.Status.FREE),
            new Employer("wangwu", 25, 3380,Employer.Status.BUSY),
            new Employer("zhaoliu", 26, 6380,Employer.Status.VOCATION),
            new Employer("sunqi", 26, 6780,Employer.Status.BUSY),
            new Employer("ll", 24, 4780,Employer.Status.VOCATION)
    );

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
}
