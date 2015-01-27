package com.mkyong.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{	
	CacheManager cm;
	SimpleDateFormat df = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
	
    public static void main( String[] args ) throws IOException
    {
    	@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
    	
        App app = new App();
        app.run();
        
    }
    
    public void run() throws IOException{
    	cm =  CacheManager.newInstance("src/main/resources/ehcache.xml");
    	Cache cache = cm.getCache("testcache");
    	printHelp();
    	
    	String parameter = "";
    	Scanner scanner = new Scanner(System.in);
    	while(true){

    	    String input = scanner.next();
    		switch (input) {
			case "fill":
				fillCache(cache);
				System.out.println("cache is filled");
				break;
			case "get":
				parameter = scanner.next();
				getFromCache(cache, parameter);
				break;
			case "stats":
				printCacheStatistics(cache);
				break;
			case "evict":
				evictCacheElements(cache);
				break;
			case "exists":
				parameter = scanner.next();
				checkIfExistsInCache(cache, parameter);
				break;
			default:
				printHelp();
				break;
			}
    		
    		if(input.equals("exit")){
    			System.out.println("exit application\n");
    			break;
    		}
    	}
    	scanner.close();
    }
    
    public void printCacheStatistics(Cache cache){
    	System.out.println("time: "+df.format(new Date()));
    	System.out.println("Objects in cache: " +cache.getStatistics().getSize());
		System.out.println("Evicted count is: "+cache.getStatistics().cacheEvictedCount());
		System.out.println("Hit count is: " +cache.getStatistics().cacheHitCount());
		System.out.println("Missed count is: "+cache.getStatistics().cacheMissCount());
		System.out.println("Expired count is: "+cache.getStatistics().cacheExpiredCount());
		System.out.println("Heap Size is: " +cache.getStatistics().getLocalHeapSize());
		System.out.println("Heap Size in bytes: " +cache.getStatistics().getLocalHeapSizeInBytes());
		//cache.getStatistics().cacheExpiredOperation()
    }
    
    public void fillCache(Cache cache){
    	for (int i = 0; i < 110; i++) {
    		cache.put(new Element(String.valueOf(i), "new Element with value: " +i));
		}
    }
    
    public void getFromCache(Cache cache, String key){
    	Element element = cache.get(key);
    	
		if(element != null){
			System.out.println("Element value is: "+element.getObjectValue().toString());
			System.out.println("Created in: " +df.format(new Date(element.getCreationTime())));
			System.out.println("Expires in: " +df.format(new Date(element.getExpirationTime())));
		}else{
			System.out.println("Key " +key +" is not in cache");
		}
    }
    
    public void evictCacheElements(Cache cache){
    	cache.evictExpiredElements();
    	System.out.println("All elements evicted");
    }
    
    public void checkIfExistsInCache(Cache cache, String key){
    	System.out.println("Element "+key+ " exist in cache: " +cache.isKeyInCache(key));
    }
    
    public void printHelp(){
    	System.out.println("Please enter your command:\n"
    			+ "fill -> fill cache\n"
    			+ "get KEY -> get key from cache\n"
    			+ "stats -> prints statistics\n"
    			+ "evict -> evict cache elements\n"
    			+ "exists KEY ->  check if exists\n"
    			+ "exit -> exits the application\n");
    }
}
