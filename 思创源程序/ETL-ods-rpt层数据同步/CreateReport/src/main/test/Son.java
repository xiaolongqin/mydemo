/**
 * Created by Tyfunwang on 2015/6/9.
 */
public class Son extends Father{
//    @Override
//    public void fatherSpeak() {
//        System.out.println("I`m Father2!");
//    }
    public void sonSay(){
        System.out.println("I`m Son!");
    }

    public static void main(String[] args) {
        new Son().fatherSpeak();
    }
}
