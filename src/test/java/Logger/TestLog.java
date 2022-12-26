package Logger;

import org.example.Logger.Log;
import org.junit.Test;

public class TestLog {


    private static final Log logger = Log.getLogger();

    @Test
    public  void testLog(){
        logger.info("测试打印日志");
    }
}
