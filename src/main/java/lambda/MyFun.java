package lambda;

@FunctionalInterface
public interface MyFun {
    public Integer getValue(Integer m);
    default public void getName() {
        System.out.println("hiheiie1");
    }
}
