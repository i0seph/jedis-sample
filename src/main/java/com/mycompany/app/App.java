package com.mycompany.app;

import java.util.HashSet;
import java.util.Set;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.HostAndPort;

import java.util.UUID;

public class App {

    public static void main(String[] args) throws InterruptedException {

	Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
	//Jedis Cluster will attempt to discover cluster nodes automatically
	jedisClusterNodes.add(new HostAndPort("172.19.0.2", 6379));
	jedisClusterNodes.add(new HostAndPort("172.19.0.3", 6379));
	jedisClusterNodes.add(new HostAndPort("172.19.0.4", 6379));
	JedisCluster jc = new JedisCluster(jedisClusterNodes, 1000, 2);

	while(true){
		UUID one = UUID.randomUUID();
		UUID two = UUID.randomUUID();
		try {
			jc.set(one.toString(), two.toString());
			String value = jc.get(one.toString());

		        System.out.println(value);
		        System.out.flush();
		}
		catch(redis.clients.jedis.exceptions.JedisClusterMaxAttemptsException e){
		        System.out.println("fail cluster node");
		        System.out.flush();
		}
		catch(redis.clients.jedis.exceptions.JedisClusterException e){
		        System.out.println("broken clster");
		        System.out.flush();
		}
		catch(redis.clients.jedis.exceptions.JedisNoReachableClusterNodeException e){
			System.out.println("timeout");
		        System.out.flush();
		}
//		Thread.sleep();
	}
    }

}
