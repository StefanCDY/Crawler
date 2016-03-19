package test;

import java.util.HashMap;
import java.util.Map;
//创建一个线程记录失败的请求
public class HandlerFailThread  extends Thread{
    Map<String, String> failUrl=new HashMap<String, String>();
    public void putFailUrl(String url,String status){
        synchronized (failUrl) {
            failUrl.put(url,status);
        }
    }
    @Override
    public void run() {
        while(true){
            
        }
    }
    public void printFailUrl(){
        for(Map.Entry<String, String> m: failUrl.entrySet()){
            System.out.println("****fail:url:"+m.getKey()+ "  code :"+m.getValue());
        }
    }
}
