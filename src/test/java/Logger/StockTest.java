package Logger;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class StockTest {

    @Test
    public void getStockTest(){
        String url = "http://hq.sinajs.cn/list=sh600151,sz000830,s_sh000001,s_sz399001,s_sz399106";
        try {
            URL u = new URL(url);
            byte[] b = new byte[256];
            InputStream in = null;
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            while (true) {
                try {
                    in = u.openStream();
                    int i;
                    while ((i = in.read(b)) != -1) {
                        bo.write(b, 0, i);
                    }
                    String result = bo.toString();
                    String[] stocks = result.split(";");
                    for (String stock : stocks) {
                        String[] datas = stock.split(",");
                        //根据对照自己对应数据
                    }
                    bo.reset();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
