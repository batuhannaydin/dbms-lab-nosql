package app.store;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import app.model.Student;
import com.google.gson.Gson;

public class RedisStore {
    static JedisPool jedisPool;
    static Gson gson = new Gson();

    public static void init() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(60); 
        jedisPool = new JedisPool(poolConfig, "localhost", 6379);
        
        // veri seti
        String[] depts = {"AI Engineering", "Bioinformatics", "Data Science", "Cyber Security", "Robotics"};
        String[] names = {"Caner", "Selin", "Bora", "İpek", "Kerem", "Deniz", "Ezgi", "Okan"};
        String[] surnames = {"Öztürk", "Yılmaz", "Aksoy", "Erdemir", "Çetin", "Kaya", "Şen"};

        try (Jedis jedis = jedisPool.getResource()) {
            for (int i = 0; i < 10000; i++) {
                String id = "2026" + String.format("%06d", i); 
                String name = names[i % names.length] + " " + surnames[i % surnames.length];
                Student s = new Student(id, name, depts[i % depts.length]);
                
                // Key formatı (student_no=xxxx)
                jedis.set("student_no=" + id, gson.toJson(s));
            }
        }
    }

    public static Student get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(key);
            return json != null ? gson.fromJson(json, Student.class) : null;
        }
    }
}
