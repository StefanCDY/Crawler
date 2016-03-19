//package test;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CountDownLatch;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.concurrent.FutureCallback;
//import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
//import org.apache.http.nio.client.HttpAsyncClient;
//import org.apache.http.nio.reactor.IOReactorException;
//
////多线程异步请求
//public class AsynClient{
//    /**
//     * @param args
//     * @throws IOReactorException
//     * @throws InterruptedException
//     */
//    private List<String> urls;
//    private HandlerFailThread failHandler;
//    public AsynClient(List<String> list){
//        failHandler=new HandlerFailThread();
//        urls=list;
//    }
//    public Map<String,String> asynGet() throws IOReactorException,
//            InterruptedException {
//        final HttpAsyncClient httpclient = new DefaultHttpAsyncClient();
//        httpclient.start();
//        int urlLength=urls.size();
//        HttpGet[] requests = new HttpGet[urlLength];
//        int i=0;
//        for(String url : urls){
//            requests[i]=new HttpGet(url);
//            i++;
//        }
//        final CountDownLatch latch = new CountDownLatch(requests.length);
//        final Map<String, String> responseMap=new HashMap<String, String>();
//        try {
//            for (final HttpGet request : requests) {
//                httpclient.execute(request, new FutureCallback<HttpResponse>() {
//
//                    public void completed(final HttpResponse response) {
//                        latch.countDown();
//                        responseMap.put(request.getURI().toString(), response.getStatusLine().toString());
//                        try {
//                            System.out.println(request.getRequestLine() + "->"
//                                    + response.getStatusLine()+"->");
//                            //+readInputStream(response.getEntity().getContent())
//                            
//                        } catch (IllegalStateException e) {
//                            failHandler.putFailUrl(request.getURI().toString(),
//                                    response.getStatusLine().toString());
//                            e.printStackTrace();
//                        } catch (Exception e) {
//                            failHandler.putFailUrl(request.getURI().toString(),
//                                    response.getStatusLine().toString());
//                            e.printStackTrace();
//                        }
//                    }
//
//                    public void failed(final Exception ex) {
//                        latch.countDown();
//                        ex.printStackTrace();
//                        failHandler.putFailUrl(request.getURI().toString(),
//                                ex.getMessage());
//                    }
//
//                    public void cancelled() {
//                        latch.countDown();
//                    }
//
//                });
//            }
//            System.out.println("Doing...");
//        } finally {
//            latch.await();
//            httpclient.shutdown();
//        }
//        System.out.println("Done");
//        failHandler.printFailUrl();
//        return responseMap;
//    }
//    private String readInputStream(InputStream input) throws IOException{
//        byte[] buffer = new byte[128];
//        int len = 0;
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        while((len = input.read(buffer)) >= 0) {
//            bytes.write(buffer, 0, len);
//        }
//        return bytes.toString();
//    }
//    /**
//     * Test
//     * @param args
//     */
//    public static void main(String[] args) {
//        List<String> urls=new ArrayList<String>();
//        urls.add("http://127.0.0.1/examples/servlets/");
//        urls.add("http://127.0.0.1/examples/servlets/");
//        urls.add("http://127.0.0.1/examples/servlets/");
//        for(int i=0;i<10;i++){
//            urls.addAll(urls);
//        }
//        System.out.println(urls.size());
//        AsynClient client=new AsynClient(urls);
//        try {
//            client.asynGet();
//        } catch (IOReactorException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("done");
//    }
//}
