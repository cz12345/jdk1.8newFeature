import lambda.domain.Employer;
import lambda.filter.FilterEmployer;
import lambda.filter.impl.FilterEmployerByAgeImpl;
import lambda.filter.impl.FilterEmployerBySalaryImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestLambda {
    List<Employer> employers = Arrays.asList(
            new Employer("zhangsan", 18, 335),
            new Employer("lisi", 19, 338),
            new Employer("wangwu", 25, 3380),
            new Employer("zhaoliu", 26, 6380),
            new Employer("sunqi", 26, 6780),
            new Employer("erba", 27, 7780),
            new Employer("ll", 24, 4780)
    );


    //过滤条件，至于是按照工资过滤还是薪水过滤，就看你传的实现类是哪种了，或者直接匿名内部类
    //返回过滤后的集合
    public List<Employer> filterEmployer(List<Employer> list, FilterEmployer<Employer> filterEmployer) {
        List<Employer> list1 = new ArrayList<Employer>();
        for (Employer employer : list) {
            if (filterEmployer.filter(employer)) {
                list1.add(employer);
            }
        }
        return list1;
    }

    //查询工资大于4000
    @Test
    public void test1() {
        //传接口的实现类，多态
        List<Employer> employers = filterEmployer(this.employers, new FilterEmployerBySalaryImpl());
        //
        for (Employer employer : employers) {
            System.out.println(employer);
        }
    }

    //查询年龄大于24
    @Test
    public void test2() {
        //传接口的实现类，多态
        List<Employer> employers = filterEmployer(this.employers, new FilterEmployerByAgeImpl());
        //
        for (Employer employer : employers) {
            System.out.println(employer);
        }

    }

    //使用匿名内部类方式查询工资小于4000的
    @Test
    public void test3() {
        List<Employer> employers = filterEmployer(this.employers, new FilterEmployer<Employer>() {
            @Override
            public boolean filter(Employer employer) {
                return employer.getSalary() < 4000;
            }
        });
        /*for (Employer employer : employers) {
            System.out.println(employer);
        }*/
        //JDK1.8集合另一种打印方法,注意参数是Concusmer
        employers.forEach(System.out::println);
    }

    //使用Lambda 实现大于7000
    @Test
    public void test4() {
        List<Employer> employers = filterEmployer(this.employers, (e) -> e.getSalary() > 7000);
        employers.forEach(System.out::println);
    }

    //还可以对上述代码继续优化，缩减代码
    @Test
    public void test5() {
        //假设现在没有之前定义的接口以及实现类，现在只有employers集合和test类还有一个domian类
        //那么怎么可以实现像test4,test3，test2等相似的功能呢？
        //演示: 可以看到只有一行代码
//        employers.stream().filter((e) -> e.getSalary() < 4000).forEach(System.out::println);
        //只输出前两行,类似于sql语句
//        employers.stream().filter((e) -> e.getSalary() < 4000).limit(2).forEach(System.out::println);

        //现在我只想拿到灭个实体中name字段
        employers.stream().map(Employer::getName).forEach(System.out::println);

    }
}


