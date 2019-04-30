package shareniu.ch7;

import org.camunda.bpm.engine.impl.digest.DatabasePrefixHandler;
import org.junit.Test;

public class DatabasePrefixHandlerTest {

    @Test
    public  void  testgeneratePrefix(){
        DatabasePrefixHandler databasePrefixHandler=new DatabasePrefixHandler();
        String shareniu1 = databasePrefixHandler.generatePrefix("shareniu1");
        System.out.println(shareniu1);
    }
    @Test
    public  void  testretrieveAlgorithmName(){
        String encryptedPasswordWithPrefix="{SHA-shareniu}QXObWC2SqvI9OX2p6hDUbCj9grPXu8Ce1zM27wDrSKjCpZp8XhFe0FuoVWHxdPzavqH2H4EyZsrTDL+r6y/vHA==";
      //  String encryptedPasswordWithPrefix="{SHA-512}QXObWC2SqvI9OX2p6hDUbCj9grPXu8Ce1zM27wDrSKjCpZp8XhFe0FuoVWHxdPzavqH2H4EyZsrTDL+r6y/vHA==";
        //String encryptedPasswordWithPrefix="shareniu1";
        DatabasePrefixHandler databasePrefixHandler=new DatabasePrefixHandler();
        String shareniu1 = databasePrefixHandler.retrieveAlgorithmName(encryptedPasswordWithPrefix);
        System.out.println(shareniu1);
    }
    @Test
    public  void  testremovePrefix(){
       // String encryptedPasswordWithPrefix="{SHA-shareniu}QXObWC2SqvI9OX2p6hDUbCj9grPXu8Ce1zM27wDrSKjCpZp8XhFe0FuoVWHxdPzavqH2H4EyZsrTDL+r6y/vHA==";
        String encryptedPasswordWithPrefix="shareniu1";
        DatabasePrefixHandler databasePrefixHandler=new DatabasePrefixHandler();
        String shareniu1 = databasePrefixHandler.removePrefix(encryptedPasswordWithPrefix);
        System.out.println(shareniu1);
    }

}
