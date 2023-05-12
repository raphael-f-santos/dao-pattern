package com.daopattern.dao;

import com.daopattern.model.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDAO {
    
    private final String insertSQL = "INSERT INTO pessoa(name, age) VALUES(?, ?)";
    private final String selectSQL = "SELECT id, name, age FROM pessoa WHERE id = ?";
    private final String updateSQL = "UPDATE pessoa SET name = ?, age = ? WHERE id = ?";
    private final String consultSQL = "SELECT * FROM pessoa WHERE name LIKE ?";
    private final String deleteSQL = "DELETE FROM pessoa WHERE id = ?";
    
    private final ArrayList<Person> people = new ArrayList<Person>();
    
    private Connection connection;
    
    private Connection getConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                return connection;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
        
        connection = Connecter.getConnection();
        return connection;
    }
    
    private void insertAttributes(PreparedStatement stmt, Object[] atributtes) throws SQLException {
		
	int index = 1;
	for(Object atributte: atributtes) {
	    if(atributte instanceof String string) {
		stmt.setString(index, string);				
            } else if(atributte instanceof Integer) {
                stmt.setInt(index, (Integer) atributte);
	    }
			
            index++;
        }
    }
    
    public int insert(Object... atributtes) {
	try {
            PreparedStatement stmt = getConnection()
                    .prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            
            insertAttributes(stmt, atributtes);
            
            if(stmt.executeUpdate() > 0) {
                ResultSet result = stmt.getGeneratedKeys();
				
                if(result.next()) {
                    return result.getInt(1);
                }
            }
			
            return -1;
        } catch(SQLException e) {
            throw new RuntimeException(e);
	}
    }
    
    public boolean update(int id, Object... atributtes){
        try{
            PreparedStatement stmt = getConnection().prepareStatement(selectSQL);
            stmt.setInt(1, id);
            
            ResultSet result = stmt.executeQuery();
            
            if(result.next()){
  
                stmt.close();
                
                stmt = getConnection().prepareStatement(updateSQL);
                stmt.setInt(3, id);
                insertAttributes(stmt, atributtes);
                
                return stmt.execute();
            }   
            
            return false;
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
 
    }
    
     public boolean delete(int id){
        try{
            PreparedStatement stmt = getConnection().prepareStatement(deleteSQL);
            stmt.setInt(1, id);
            
            return stmt.executeUpdate() > 0;
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    public void consult(String value){      
        try {
            PreparedStatement stmt = getConnection().prepareStatement(consultSQL);
            stmt.setString(1, "%"+value+"%");
            
            ResultSet result = stmt.executeQuery();
            
            while(result.next()){
                people.add(new Person(
                         result.getInt("id"), 
                        result.getString("name"), 
                         result.getInt("age")));
            }
         
            people.forEach(person -> System.out.printf(
                   "ID: %d \nName: %s \nAge: %d \n\n", person.getId(), person.getName(), person.getAge()));
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
