package testingwithhsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAO {
	private final DataSource myDataSource;
	
	public DAO(DataSource dataSource) {
		myDataSource = dataSource;
	}

	/**
	 * Renvoie le nom d'un client à partir de son ID
	 * @param id la clé du client à chercher
	 * @return le nom du client (LastName) ou null si pas trouvé
	 * @throws SQLException 
	 */
	public String nameOfCustomer(int id) throws SQLException {
		String result = null;
		
		String sql = "SELECT LastName FROM Customer WHERE ID = ?";
		try (Connection myConnection = myDataSource.getConnection(); 
		     PreparedStatement statement = myConnection.prepareStatement(sql)) {
			statement.setInt(1, id); // On fixe le 1° paramètre de la requête
			try ( ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// est-ce qu'il y a un résultat ? (pas besoin de "while", 
                                        // il y a au plus un enregistrement)
					// On récupère les champs de l'enregistrement courant
					result = resultSet.getString("LastName");
				}
			}
		}
                 catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new SQLException(ex.getMessage());}
		// dernière ligne : on renvoie le résultat
		return result;
	}
        
        public void insertProduct(Product produit) throws SQLException {
            String sql ="INSERT INTO PRODUCT VALUES(?, ?, ?)";
            
            try (Connection connection = myDataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql);){
                   statement.setInt(1,produit.getID());
                   statement.setString(2,produit.getname());
                   statement.setFloat(3,produit.getprice());
                   
                   statement.executeUpdate();
            }
             catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new SQLException(ex.getMessage());}
        }

        public Product findProduct(int ID) throws SQLException {
		Product resultat = null;

		String requete = "SELECT * FROM PRODUCT WHERE PRODUCT_ID = ?";
		try (Connection connection = myDataSource.getConnection(); 
			PreparedStatement state = connection.prepareStatement(requete);) {

			state.setInt(1, ID);
			try (ResultSet rs = state.executeQuery()) {
				if (rs.next()) {
					String name = rs.getString("NAME");
					int price = rs.getInt("PRICE");
					resultat = new Product(ID, name, price);
				} 
			}
		}
                 catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new SQLException(ex.getMessage());}

		return resultat;
	}
	
}
