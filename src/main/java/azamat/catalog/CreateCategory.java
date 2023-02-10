package azamat.catalog;

import azamat.catalog.entity.Category;
import azamat.catalog.entity.Characteristic;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CreateCategory {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введите название категории: ");
        String newCategory = in.readLine();
        try {
            manager.getTransaction().begin();
            TypedQuery<Long> CountTypedQuery = manager.createQuery(
                    "select count(c.id) from Category c where ?1 = c.name", Long.class
    );
            CountTypedQuery.setParameter(1, newCategory);
            if(CountTypedQuery.getSingleResult()>0){
                System.out.println("Эта категория уже существует ");
            }else {
                System.out.print("Введите характеристики категорий: ");
                String newChar = in.readLine();
                String[] words = newChar.split(",");
                Category category = new Category();
                category.setName(newCategory);
                manager.persist(category);
                for (String word : words) {
                    Characteristic characteristic = new Characteristic();
                    characteristic.setCategory(category);
                    characteristic.setName(word);
                    manager.persist(characteristic);
                }
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
