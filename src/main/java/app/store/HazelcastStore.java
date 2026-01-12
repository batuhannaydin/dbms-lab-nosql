package app.store;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import app.model.Student;

public class HazelcastStore {
    private static HazelcastInstance hz;
    private static IMap<String, Student> map;

    public static void init() {
        // Hazelcast istemcisini başlatıyoruz
        hz = HazelcastClient.newHazelcastClient();
        
        
        map = hz.getMap("student_data_grid"); 
        map.clear(); // mevcut veriyi temizleme

        // veri setimiz
        String[] bolumler = {"Yazılım Müh.", "Siber Güvenlik", "Veri Bilimi", "Astronomi", "Felsefe"};
        String[] yazarlar = {"Yaşar", "Orhan", "Tomris", "Tezer", "Bilge", "Leyla", "Yusuf", "Sait"};
        String[] soyadlar = {"Kemal", "Pamuk", "Uyar", "Özlü", "Karasu", "Erbil", "Atılgan", "Faik"};

        System.out.println("Hazelcast: 10.000 kayıt oluşturuluyor...");

        for (int i = 0; i < 10000; i++) {
            String id = "2026" + String.format("%06d", i); 
            String key = "student_no=" + id; 
            String full_name = yazarlar[i % yazarlar.length] + " " + soyadlar[i % soyadlar.length];
            
            Student s = new Student(id, full_name, bolumler[i % bolumler.length]);
            map.put(key, s); // In-memory saklama
            
            if ((i + 1) % 2000 == 0) {
                System.out.println("Hazelcast: " + (i + 1) + " kayıt eklendi.");
            }
        }
    }

    public static Student get(String query) {
        // query: "student_no=2026000001" formatında
        return map.get(query);
    }
}
