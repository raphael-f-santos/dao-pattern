package com.daopattern.application;

import com.daopattern.dao.Connecter;
import com.daopattern.dao.PersonDAO;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        
        Connecter.getConnection();
        
        PersonDAO person = new PersonDAO();
        
        person.insert("name", 20);
        
        person.consult("name");
        
        person.update(1, "name", 20);
        
        person.delete(0);
        
    }
}
