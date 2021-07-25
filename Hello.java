public class Hello{
    public void print(){
        Sub sub = new Sub();
        sub.print();
        System.out.println("从文件中动态加载类成功");
    }

    public static class Sub {
        public void print() {
            System.out.println("Hello sub");
        }
    }
}