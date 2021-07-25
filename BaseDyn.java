import org.example.bmsk.Base64Utils;

public class BaseDyn {
    public static String encodeHexBase64(String string) {
        Sub sub = new Sub();
        sub.print();
        return Base64Utils.encodeHexBase64(string);
    }

    public static class Sub {
        public void print() {
            System.out.println("BaseDyn sub");
        }
    }
}