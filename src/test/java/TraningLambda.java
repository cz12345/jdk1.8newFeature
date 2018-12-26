import lambda.domain.Employer;
import lambda.filter.FilterEmployer;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Lambda练习
 */
public class TraningLambda {
    List<Employer> employers = Arrays.asList(
            new Employer("zhangsan", 18, 335,null),
            new Employer("lisi", 19, 338,null),
            new Employer("wangwu", 25, 3380,null),
            new Employer("zhaoliu", 26, 6380,null),
            new Employer("sunqi", 26, 6780,null),
            new Employer("erba", 27, 7780,null),
            new Employer("ll", 24, 4780,null)
    );


    //1. 使用Lambda进行排序，先按年龄排序，如果年龄相等，那么就照name排序
    //使用Collection.sort()方法，通过定制排序比较两个Employer（先按年龄，如果年龄一样再按姓名）
    //使用Lambda作为参数传递
    @Test
    public void test1() {
        Collections.sort(employers,(e1,e2) -> {
            if(e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return Integer.compare(e1.getAge(),e2.getAge());
            }
        });

        //打印排序后的
        for (Employer e: employers) {
            System.out.println(e);
        }
    }

    //2. 使用Lambda定制方法，将字符串转为大写
    @Test
    public void test2() {

    }
}
