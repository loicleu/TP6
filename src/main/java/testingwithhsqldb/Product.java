/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testingwithhsqldb;

/**
 *
 * @author pedago
 */
public class Product {
    private int PRODUCT_ID;
    private String name;
    private float Price;

	public Product(int Id, String name, float Price) {
		this.PRODUCT_ID = Id;
		this.name = name;
		this.Price = Price;
    
        }




        public int getID(){
               return PRODUCT_ID;}
        
        public String getname(){
               return name;}
        
        public float getprice(){
               return Price;

}}