package org.unibl.etf;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.unibl.etf.model.Proizvod;
import org.unibl.etf.model.Proizvodi;

@Path("/proizvodi")
public class RESTProizvodService {
	

	
	public Proizvodi proizvodi=new Proizvodi();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Proizvod> getProizvodi( ) {
		
		//List<Proizvod> list=new ArrayList<Proizvod>();
		JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379);

		
		
	       try (Jedis jedis = jedisPool.getResource()) {
	            // Find all candy product keys
	            Set<String> productKeys = jedis.keys("20_*");

	            for (String key : productKeys) {
	                Map<String, String> candyProductData = jedis.hgetAll(key);

	                Proizvod p=new Proizvod(Integer.valueOf(key.substring(3)),candyProductData.get("naziv"),candyProductData.get("tipSlatkisa"),candyProductData.get("boja"),candyProductData.get("okus"),Integer.valueOf(candyProductData.get("kolicina")));
	               
	                proizvodi.getList().add(p);
	            }
	            System.out.println(proizvodi.getList().get(0).tipSlatkisa);
	            return proizvodi.getList();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        
	        jedisPool.close();
	        System.out.println("ff");
	        return proizvodi.getList();
	        
			
		}
		
	
	
	
	
}