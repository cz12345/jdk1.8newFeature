package 方法引用和构造器引用;

import lambda.domain.Employer;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 方法引用：如Lambda体中的内容已经被实现了，我们可以使用“方法引用”
 *              （可以理解为方法引用是Lambda表达式的另一种表现形式）
 *
*    主要有三种语法格式 :
 *      1.对象::实例方法名
 *      2.类::静态方法名
 *      3.类::实例方法名:如果第一个参数是方法的调用者，第二个参数是方法的参数， 或者无参数时，那么可以用类::实例方法名；
 *
 *      注意：所有的引用方式都必须先满足Lambda体中调用方法的参数列表和返回值类型，要与函数式接口中抽象方法的函数列表类型一致！！！
 *
 *      二：构造器引用
 *      注意：构造器要有与之对应参数列表的构造器，并且参数列表和返回值要和函数式接口的方法一致
 *
 *
 *      三：数组引用：
 *
 *
 */
public class TestMethodRef {

    @Test
    public void test1() {
        Consumer<String> con = (x) -> System.out.println(x);
        PrintStream out = System.out;
        //可以看到println()方法已经在PrintStream里面实现了，并且，并且，注意啦：
        // public void println(boolean x) {
        //        synchronized (this) {
        //            print(x);
        //            newLine();
        //        }
        //    }
        //方法中的参数列表和返回值和Consumer中的void accept(T t);方法的参数列表和返回类型一致，方法名无所谓
        //那么此时可以用方法的引用写法：对象：实例方法名
        Consumer<String> con1 = System.out::println; //连参数x都不用写了，对参数列表不用写
        //然后调用accept并且传参数，你可以这样认为，我把别人实现的Consumer接口中的方法，搬到这里来，就相当于引用别人的
        //但是只是搬过来，在你调用的时候，虽然调用的是Consumer接口中的accept(T)方法，但是其指向是引用的实现方法，在运行时
        //会自动调用其实现方法，这过程有点类似于Java多态机制
        con1.accept("1哈哈");

        //-------------用Supplier--------
        //原来：
        Employer e = new Employer();
        e.setAge(10);
        e.setName("cz");
        e.setSalary(9);
        //原来，注意泛型一定要写，这样后面的Lambda体中才能根据上下文推断返回值
//        Supplier<Integer> s = () -> 1; == Supplier<Integer> s = () -> return 1;
        Supplier<Integer> s = () -> e.getAge();
        //Lambda体中e.getAge()已经被Employer类实现了，并且参数列表和返回值和Supplier接口中的T get()一致
        //用方法引用
        Supplier<Integer> s1 = e::getAge;
        Integer age = s1.get();
        System.out.println("age:"+age);
    }

    //类::静态方法名
    @Test
    public void test2() {
        Comparator<Integer> c = (x,y) -> Integer.compare(x,y);
        //方法引用，Integer中已经实现了compare(x,y)并且返回值和函数式接口Comparator中的
        // int compare(T o1, T o2);参数列表与返回值一致
        Comparator<Integer> c1 = Integer::compare;//参数一致
        int compare = c1.compare(1, 1);
    }

    // 类::实例方法名,规则：如果第一个参数是方法的调用者，第二个参数是方法的参数，或者无参数时，那么可以用类名::实例方法名；
    //需要注意的是：类名::实例方法名时，如果是无参数时，得满足，返回是Function<Employer, String>类型，或者其他函数式接口类型,
    //只要是带Function的，再看接口里的方法是一个参数并且只有一个返回值时，那么就满足无参数是的方法引用

    @Test
    public void test3() {
        //使用
        BiPredicate<String,String> b = (x,y) -> x.equals(y);
        //方法引用，x为字符串，类名肯定为String了
        BiPredicate<String,String> b1 = String::equals;
        boolean test = b1.test("1", "2");

        //无参数时,getName时无参数列表的，
        Function<Employer, String> getName = Employer::getName;

    }


    //构造器引用
    @Test
    public void test4() {
        Employer e = new Employer();
        Supplier<Employer> c = () -> new Employer();
        Employer employer = c.get();
        //采用构造器引用
        Supplier<Employer> c1 = Employer::new;
        Employer employer1 = c1.get();
        //以上是采用无参构造器的
        //采用有参构造器的引用方式
        //采用一个参数的，构造器要有一个参数的构造器，并且参数列表和返回值要和函数式接口的方法一致
        //原来,
        Function<Integer,Employer> f = (age) -> new Employer(age);
        //方法引用
        Function<Integer,Employer> f1 = Employer::new;
        Employer apply = f1.apply(100);
        System.out.println(apply);
    }

    //数组引用
    @Test
    public void test5() {
        Function<Integer,String[]> f = (s) -> new String[s];
        //数组引用
        Function<Integer,String[]> f1 = String[]::new;
        //调用
        String[] apply = f1.apply(10);
        System.out.println(apply.length);//10
    }

}
