package testingwithhsqldb;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;


public class HsqlDBTest {
	private static DataSource myDataSource;
	private static Connection myConnection ;
	
	private DAO myObject;
	
	@Before
	public  void setUp() throws IOException, SqlToolError, SQLException {
		// On crée la connection vers la base de test "in memory"
		myDataSource = getDataSource();
		myConnection = myDataSource.getConnection();
		// On crée le schema de la base de test
		executeSQLScript(myConnection, "schema.sql");
		// On y met des données
		executeSQLScript(myConnection, "bigtestdata.sql");		

            	myObject = new DAO(myDataSource);
	}
	
	private void executeSQLScript(Connection connexion, String filename)  throws IOException, SqlToolError, SQLException {
		// On initialise la base avec le contenu d'un fichier de test
		String sqlFilePath = HsqlDBTest.class.getResource(filename).getFile();
		SqlFile sqlFile = new SqlFile(new File(sqlFilePath));

		sqlFile.setConnection(connexion);
		sqlFile.execute();
		sqlFile.closeReader();		
	}
		
	@After
	public void tearDown() throws IOException, SqlToolError, SQLException {
		myConnection.close(); // La base de données de test est détruite ici
             	myObject = null; // Pas vraiment utile

	}

	@Test
	public void findExistingCustomer() throws SQLException {
		String name = myObject.nameOfCustomer(0);
		assertNotNull("Customer exists, name should not be null", name);
		assertEquals("Bad name found !", "Steel", name);
	}

	@Test
	public void nonExistingCustomerReturnsNull() throws SQLException {
		String name = myObject.nameOfCustomer(-1);
		assertNull("name should be null, customer does not exist !", name);
	}
        	
	@Test
	public void findUnknownProduct() throws SQLException {
		assertNull(myObject.findProduct(-1));
	}
	
	@Test
	public void findExistingProduct() throws SQLException {
		// le produit 1 est défini dans le jeu de test SQL
		Product p = myObject.findProduct(1);
		assertEquals("Chair Shoe", p.getname());
	}

	@Test
	public void canCreateNewProduct() throws SQLException {
		Product nouveau = new Product(2, "Un nouveau produit", 12.45f);
		myObject.insertProduct(nouveau);
		// On vérifie qu'on peut le retrouver
		assertEquals(nouveau, myObject.findProduct(2));
	}

	@Test(expected = SQLException.class)
	public void cannotCreateDuplicateKeys() throws SQLException {
		// Le produit 1 existe déjà dans la base de test
		Product existant = new Product(1, "Un produit qui existe", 12.45f);
		myObject.insertProduct(existant); // On doit avoir une exception ici
	}

	@Test(expected = SQLException.class)
	public void priceMustBePositive() throws SQLException {
		// Le produit 1 existe déjà dans la base de test
		Product bad = new Product(2, "Un produit au prix négatif", -12.45f);
		myObject.insertProduct(bad); // On doit avoir une exception ici
	}
	public static DataSource getDataSource() {
		org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
		ds.setUser("sa");
		ds.setPassword("sa");
		return ds;
	}	
}
