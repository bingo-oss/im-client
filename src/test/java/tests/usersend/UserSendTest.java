package tests.usersend;

import net.bingosoft.oss.imclient.exception.SendMessageFailException;
import net.bingosoft.oss.imclient.model.Receipt;
import org.junit.Assert;
import org.junit.Test;
import tests.IMTestBase;

/**
 * @author kael.
 */
public class UserSendTest extends IMTestBase{
    @Test
    public void testSendText(){
        // 正常发送消息
        Receipt receipt = client.send(textMessage);
        Assert.assertTrue(receipt.isSuccess());
        
        // 发送消息失败
        boolean error = false;
        try {
            client.send(errorMessage);
        } catch (SendMessageFailException e) {
            error = true;
        }
        Assert.assertTrue(error);
    }
    
    
}
