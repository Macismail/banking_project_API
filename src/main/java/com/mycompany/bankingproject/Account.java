/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
package com.mycompany.bankingproject;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ismail
 */
@Entity
@Table
@XmlRootElement(name="Account")
public class Account implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int accountID;
   private int accountNumber;
   private int sortCode;
   private String accountType;
   private double curentBalance;
   private int customerID;

   public int getAccountID() {
      return accountID;
   }

   public void setAccountID(int accountID) {
      this.accountID = accountID;
   }
   
   @XmlElement
   public int getAccountNumber() {
      return accountNumber;
   }

   public void setAccountNumber(int accountNumber) {
      this.accountNumber = accountNumber;
   }

   public int getSortCode() {
      return sortCode;
   }

   public void setSortCode(int sortCode) {
      this.sortCode = sortCode;
   }

   public String getAccountType() {
      return accountType;
   }

   public void setAccountType(String accountType) {
      this.accountType = accountType;
   }
   
   @XmlElement(name="curentBalance")
   public double getCurentBalance() {
      return curentBalance;
   }

   public void setCurentBalance(double curentBalance) {
      this.curentBalance = curentBalance;
   }
   
   @XmlElement
   public int getCustomerID() {
      return customerID;
   }

   public void setCustomerID(int customerID) {
      this.customerID = customerID;
   }

}
