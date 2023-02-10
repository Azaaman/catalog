package azamat.catalog;

import azamat.catalog.entity.Category;
import azamat.catalog.entity.Characteristic;
import azamat.catalog.entity.CharacteristicMeanning;
import azamat.catalog.entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CreateProduct {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();
            TypedQuery<Category> categoryTypedQuery = manager.createQuery(
                    "select c from Category c", Category.class
            );
            List<Category> categories = categoryTypedQuery.getResultList();
            for (Category category : categories) {
                System.out.printf("%s  [%d] %n", category.getName(), category.getId());
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Выберите категорию товара: ");
            long newCategory = Long.parseLong(in.readLine());
            Product product = new Product();
            Category category = manager.find(Category.class,newCategory);
            product.setCategory(category);
            System.out.print("Введите название товара: ");
            String newName = in.readLine();
            product.setName(newName);
            System.out.print("Введите стоимость товара: ");
            int newPrice = Integer.parseInt(in.readLine());
            product.setPrice(newPrice);
            manager.persist(product);
            TypedQuery<Characteristic> characterisricTypedQuery = manager.createQuery(
                    "select c from Characteristic c where c.category = ?1", Characteristic.class
            );characterisricTypedQuery.setParameter(1, product.getCategory());
            List <Characteristic> characteristics = characterisricTypedQuery.getResultList();
            for(Characteristic characteristic: characteristics ){
                System.out.println(characteristic.getName() + ": ");
                CharacteristicMeanning charMean = new CharacteristicMeanning();
                String newCharMean = in.readLine();
                charMean.setCharacteristic(characteristic);
                charMean.setProduct(product);
                charMean.setName(newCharMean);
                manager.persist(charMean);
            }

            // Выберите категорию: 1 (Процессоры)
            // Название: ___
            // Стоимость: ___
            // Производитель: ___
            // Сокет: ___
            // Тактовая частота: ___
            // ...
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }

        // Смартфоны [1]
        // Процессоры [2]
        // Ноутбуки [3]
        // Выберите категорию товара: ___
        // Введите название товара: ___
        // Введите стоимость товара: ___
    }
}
