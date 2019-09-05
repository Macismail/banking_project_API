package com.mycompany.bankingproject;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement(name="Customer")
public class Customer implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int customerID;
   private String name;
   private String address;
   private String email;
   private String contact;
   private int securityCode;

   public int getCustomerID() {
      return customerID;
   }

   public void setCustomerID(int customerID) {
      this.customerID = customerID;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getContact() {
      return contact;
   }

   public void setContact(String contact) {
      this.contact = contact;
   }

   public int getSecurityCode() {
      return securityCode;
   }

   public void setSecurityCode(int securityCode) {
      this.securityCode = securityCode;
   }
     
}
