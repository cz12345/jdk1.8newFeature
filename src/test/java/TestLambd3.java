import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;  
import java.util.function.Supplier;

/**
 * java8 中Lambda四大核心函数式接口
 * Consumer<T> 消费型接口
 *      void accept(T t);里面的方法无返回值，参数列表一个
 * Supplier<T> 供给型接口
 *      T get();无参数列表，有返回值
 * Function<T,R>:函数型接口
 *      R apply(T t)，有一个参数，有返回值
 * Predicate<T>:断言型接口。做一些判断操作的
 *      boolean test(T t) ,返回值为Boolean，参数列表一个
 *
 * 还有其他Java定义的函数式接口，文档里面做笔记了
 *
 *
 */
public class TestLambd3 {

    //案例：消费型接口
    @Test
    public void test1() {
        xiaofei(1000,(x) -> System.out.println("你喜欢大保健每次消费:"+x*x+"元"));
        }
    public void xiaofei(double money, Consumer<Double> con) {
        con.accept(money);
    }
    //---------------供给型接口---------

    @Test
    public void test2() {
        List<Integer> gongjixing = gongjixing(100, () -> (int) (Math.random() * 100));
        for (int i = 0; i < gongjixing.size(); i++) {
            System.out.println(gongjixing.get(i));
        }
    }
    //产生指定的个数的随机数
    public List<Integer> gongjixing(int num, Supplier<Integer> sp) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Integer j = sp.get();
            list.add(j);;;
        }
        return list;
    }

    //--------------h函数型接口-------------
    @Test
    public void test3() {
        //返回一个字符串长度
        String length = hanshuxing("哈哈哈哈", (str) -> str.length() + "");
        System.out.println(length);
    }
    //传一个参数，返回一个参数
    public String hanshuxing(String str, Function<String,String> fu) {
        String apply = fu.apply(str);
        return apply;
    }

    //----------------断言型接口-----------
    @Test
    public void test4() {
        List<String> list = Arrays.asList("hello","good","yes","no","11");
        List<String> duanyanxing = duanyanxing(list, (str) -> str.length() > 3);
        System.out.println(duanyanxing);
    }

    public List<String> duanyanxing(List<String> strList, Predicate<String> p) {
        List<String> stringList = new ArrayList<>();
        for (String strs : strList) {
            if(p.test(strs)) {
                stringList.add(strs);
            }
        }
        return stringList;
    }


}
