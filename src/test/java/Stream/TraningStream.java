package Stream;

import lambda.domain.Employer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Stream练习
 */
public class TraningStream {

    List<Employer> employers = Arrays.asList(
            new Employer("zhangsan", 18, 5535,Employer.Status.FREE),
            new Employer("wangwu", 25, 3380,Employer.Status.BUSY),
            new Employer("zhaoliu", 26, 6380,Employer.Status.VOCATION),
            new Employer("sunqi", 26, 6780,Employer.Status.BUSY),
            new Employer("ll", 24, 4780,Employer.Status.VOCATION),
            new Employer("ll", 24, 4780,Employer.Status.VOCATION)
    );
    /*
      给定一个数字列表，如何返回一个由每个数字的平方构成的列表呢？
      给定[1,2,3,4,5],应该返回[1,4,9,16,25]。
     */
    @Test
    public void test1() {
        Integer[] array = {1,2,3,4,5};
        Arrays.stream(array).map((x) -> x*x).forEach(System.out::println);



    }
}