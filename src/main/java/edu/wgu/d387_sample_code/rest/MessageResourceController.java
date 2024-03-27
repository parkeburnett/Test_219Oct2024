package edu.wgu.d387_sample_code.rest;

import edu.wgu.d387_sample_code.model.response.StringResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.Executors.newFixedThreadPool;

@RestController
@CrossOrigin
public class MessageResourceController {
    static ExecutorService messageExecutor = newFixedThreadPool(2);
    @RequestMapping(path ="message", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public StringResponse getMessage(){
        AtomicReference<String> english = new AtomicReference<>("");
        AtomicReference<String> french = new AtomicReference<>("");
        Properties properties = new Properties();
        Future EN = messageExecutor.submit(() -> {
            try{
                InputStream stream = new ClassPathResource("Internationalization_en_CA.properties").getInputStream();
                properties.load(stream);
                english.set(properties.getProperty("welcome"));
                System.out.println(properties.getProperty("welcome"));

            }catch (Exception e){
                e.printStackTrace();
            }
        });

        Future FR = messageExecutor.submit(() -> {
            try{
                InputStream stream = new ClassPathResource("Internationalization_fr_CA.properties").getInputStream();
                properties.load(stream);
                System.out.println(properties.getProperty("welcome"));
                french.set(properties.getProperty("welcome"));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        while(!EN.isDone() || !FR.isDone()){
            try{
                Thread.sleep(1);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return new StringResponse(english.get() + " / " + french.get());
    }

}
