package shareniu.ch1;

/**
 * 自定义类
 */
public class TestA {
    private  int age;
    private  String  name;
    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return age;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "名称："+name+",age:"+age;
    }
}
