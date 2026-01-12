package app.store;

import com.mongodb.client.*;
import org.bson.Document;
import app.model.Student;
import com.google.gson.Gson;

public class MongoStore {
    static MongoClient client;
    static MongoCollection<Document> collection;
    static Gson gson = new Gson();

    public static void init() {
        client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("nosqllab");
        collection = db.getCollection("student_registry"); // İsim değişti
        collection.drop();

        for (int i = 0; i < 10000; i++) {
            String id = "2026" + String.format("%06d", i);
            Student s = new Student(id, "Kayit-" + i, "Muhendislik");
            collection.insertOne(Document.parse(gson.toJson(s)));
        }
    }

    public static Student get(String param) {
        // Parametre "student_no=2026000001" olarak geliyor ve içindeki id'yi ayıklıyoruz kullanmak için
        String idValue = param.contains("=") ? param.split("=")[1] : param;
        Document doc = collection.find(new Document("student_no", idValue)).first();
        return doc != null ? gson.fromJson(doc.toJson(), Student.class) : null;
    }
}
