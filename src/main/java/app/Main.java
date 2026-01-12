package app;

import static spark.Spark.*;
import com.google.gson.Gson;
import app.store.*;

public class Main {
    public static void main(String[] args) {
        port(8080);
        Gson gson = new Gson();

        // Sunucuları başlatma
        RedisStore.init();
        HazelcastStore.init(); 
        MongoStore.init();

        // Urlmiz : localhost:8080/nosql-lab-rd/student_no=xxxxxxxxxx 
        // param ifadesi URL'nin sonundaki öğrenci nosuna göre json'ı yakalar
        
        get("/nosql-lab-rd/:param", (req, res) -> {
            res.type("application/json");
            return gson.toJson(RedisStore.get(req.params(":param")));
        });

        get("/nosql-lab-hz/:param", (req, res) -> {
            res.type("application/json");
            return gson.toJson(HazelcastStore.get(req.params(":param")));
        });

        get("/nosql-lab-mon/:param", (req, res) -> {
            res.type("application/json");
            return gson.toJson(MongoStore.get(req.params(":param")));
        });
    }
}
