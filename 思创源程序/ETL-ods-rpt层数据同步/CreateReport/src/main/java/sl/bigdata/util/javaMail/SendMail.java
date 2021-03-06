package sl.bigdata.util.javaMail;

import javax.mail.MessagingException;

/**
 * Created by 小龙
 * on 15-04-9
 * at 下午3:20.
 */
public class SendMail {

    //发送重置密码邮件
    public static boolean sendresetPassMail(String toAddress, boolean flag,String content) throws MessagingException {
        return new SetParameter().resetPassMail(toAddress,flag,content);

    }

    //发送认证邮件
    public static boolean sendMail(String toAddress, boolean flag,String content) throws MessagingException {
        return new SetParameter().setMailMessage(toAddress,flag,content);

    }
    //发送消息邮件
    public static boolean sendMsgs2User(String toAddress, boolean flag,String content) throws MessagingException {
        return new SetParameter().setMsg2User(toAddress, flag, content);

    }
    //OSS发送消息邮件
    public static boolean sendMsgs2Oss(String toAddress, boolean flag,String content) throws MessagingException {
        return new SetParameter().setMsg2Oss(toAddress,flag,content);

    }

}
