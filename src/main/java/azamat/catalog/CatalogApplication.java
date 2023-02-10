package azamat.catalog;

import azamat.catalog.entity.Category;
import azamat.catalog.entity.Characteristic;
import azamat.catalog.entity.CharacteristicMeanning;
import azamat.catalog.entity.Product;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CatalogApplication {
    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("main");

    private static final BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int x = 0;
        while (x != 5) {
            System.out.println("Выберите дейстие: \n" +
                    "Создание категории [1] \n" +
                    "Создание товара [2] \n" +
                    "Удаление товарар [3] \n" +
                    "Редактирование товара [4] \n" +
                    "Завершить выполнение [5]");
            int action = Integer.parseInt(IN.readLine());
            x = action;
            switch (action) {
                case 1:
                    createCategory();
                    break;
                case 2:
                    createProduct();
                    break;
                case 3:
                    deleteProduct();
                    break;
                case 4:
                    updateProduct();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Вы выбрали не существующее действие");
                    break;
            }
        }
    }


    private static void createCategory() {
        EntityManager manager = FACTORY.createEntityManager();
        try {
            manager.getTransaction().begin();
            System.out.print("Введите название категории: ");
            String newCategory = IN.readLine();
            TypedQuery<Long> CountTypedQuery = manager.createQuery(
                    "select count(c.id) from Category c where ?1 = c.name", Long.class
            );
            CountTypedQuery.setParameter(1, newCategory);
            if (CountTypedQuery.getSingleResult() > 0) {
                System.out.println("Эта категория уже существует ");
            } else {
                System.out.print("Введите характеристики категорий: ");
                String newChar = IN.readLine();
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


    private static void createProduct() {
        EntityManager manager = FACTORY.createEntityManager();
        try {
            manager.getTransaction().begin();
            TypedQuery<Category> categoryTypedQuery = manager.createQuery(
                    "select c from Category c", Category.class
            );
            List<Category> categories = categoryTypedQuery.getResultList();
            for (Category category : categories) {
                System.out.printf("%s  [%d] %n", category.getName(), category.getId());
            }

            System.out.print("Выберите категорию товара: ");
            long newCategory = Long.parseLong(IN.readLine());
            Product product = new Product();
            Category category = manager.find(Category.class, newCategory);
            product.setCategory(category);
            System.out.print("Введите название товара: ");
            String newName = IN.readLine();
            product.setName(newName);
            System.out.print("Введите стоимость товара: ");
            int newPrice = Integer.parseInt(IN.readLine());
            product.setPrice(newPrice);
            manager.persist(product);
            TypedQuery<Characteristic> characterisricTypedQuery = manager.createQuery(
                    "select c from Characteristic c where c.category = ?1", Characteristic.class
            );
            characterisricTypedQuery.setParameter(1, product.getCategory());
            List<Characteristic> characteristics = characterisricTypedQuery.getResultList();
            for (Characteristic characteristic : characteristics) {
                System.out.println(characteristic.getName() + ": ");
                CharacteristicMeanning charMean = new CharacteristicMeanning();
                String newCharMean = IN.readLine();
                charMean.setCharacteristic(characteristic);
                charMean.setProduct(product);
                charMean.setName(newCharMean);
                manager.persist(charMean);
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }


    private static void deleteProduct() {

        EntityManager manager = FACTORY.createEntityManager();
        try {
            manager.getTransaction().begin();
            System.out.print("Выберите товар: ");
            long deleteProduct = Long.parseLong(IN.readLine());
            Query query1 = manager.createQuery(
                    "delete from Product p where p.id = ?1 "
            );
            query1.setParameter(1, deleteProduct);
            Query query2 = manager.createQuery(
                    "delete from CharacteristicMeanning c where c.product.id = ?1 "
            );
            query2.setParameter(1, deleteProduct);
            query2.executeUpdate();
            query1.executeUpdate();
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }


    private static void updateProduct() {
        EntityManager manager = FACTORY.createEntityManager();
        try {
            manager.getTransaction().begin();
            System.out.print("Выберите товар: ");
            long updateProduct = Long.parseLong(IN.readLine());
            Product product = manager.find(Product.class, updateProduct);
            System.out.print("Введите новое название товара: ");
            String newName = IN.readLine();
            if (newName.equals("")) {
                product.setName(product.getName());
            } else {
                product.setName(newName);
            }
            System.out.print("Введите новую цену товара: ");
            String newPrice = IN.readLine();
            if (newPrice.equals("")) {
                product.setPrice(product.getPrice());
            } else {
                boolean isnumber = newPrice.matches("\\d+$");
                while (isnumber == false) {
                    System.out.println("Введите число без других символов и букв: ");
                    newPrice = IN.readLine();
                    isnumber = newPrice.matches("\\d+$");
                }
                int NewPrice = Integer.parseInt(newPrice);
                product.setPrice(NewPrice);
            }
            TypedQuery<Characteristic> characterisricTypedQuery = manager.createQuery(
                    "select c from Characteristic c where c.category = ?1", Characteristic.class
            );
            characterisricTypedQuery.setParameter(1, product.getCategory());
            List<Characteristic> characteristics = characterisricTypedQuery.getResultList();
            for (Characteristic characteristic : characteristics) {
                System.out.println("Введите новый " + characteristic.getName() + ": ");
                String newCharMean = IN.readLine();
                TypedQuery<CharacteristicMeanning> characterisricMeanTypedQuery = manager.createQuery(
                        "select c from CharacteristicMeanning c where c.product.id = ?1 and c.characteristic.id = ?2", CharacteristicMeanning.class
                );
                characterisricMeanTypedQuery.setParameter(1, product.getId());
                characterisricMeanTypedQuery.setParameter(2, characteristic.getId());
                if (newCharMean.equals("")) {
                    characterisricMeanTypedQuery.getSingleResult().setName(characterisricMeanTypedQuery.getSingleResult().getName());
                } else {
                    characterisricMeanTypedQuery.getSingleResult().setName(newCharMean);
                }
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }


}
