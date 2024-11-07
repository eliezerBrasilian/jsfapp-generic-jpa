
package _jpa;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class JpaUtil {
    
    private static final SessionFactory sessionFactory;
    private static String url;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            /**
             * configura arquivo hubernate.cfg.xml
             */
            System.out.println("Carregando banco de dados com hibernate xml");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            System.out.println("Criado servico de registro");
            String property = configuration.getProperties()
                    .getProperty("hibernate.connection.url");
            url = property;
            System.out.println("Propriedade url conection: " + property);
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
           
            System.out.println("Sessão construída.");

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return JpaUtil.getSessionFactory().getCurrentSession();
    }
    
    public static String getUrl() {
        return url;
    }
}
 

