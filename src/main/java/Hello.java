/**
 * Created by xuqi on 2018/1/18.
 */
public class Hello {
    static{
        System.loadLibrary("Hello");
    }

    public native void myprint();

    public static void main(String[] args)
    {
        new Hello().myprint();
    }
}
