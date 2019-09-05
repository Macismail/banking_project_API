/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bankingproject;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table
@XmlRootElement(name = "Transaction")
public class Transaction implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int transactionID;
   private int accountID;
   private double credit;
   private double debit;
   private Date date;
   private String description;
   private double postTB;

   public int getTransactionID() {
      return transactionID;
   }

   public void setTransactionID(int transactionID) {
      this.transactionID = transactionID;
   }

   public int getAccountID() {
      return accountID;
   }

   public void setAccountID(int accountID) {
      this.accountID = accountID;
   }

   public double getCredit() {
      return credit;
   }

   public void setCredit(double credit) {
      this.credit = credit;
   }

   public double getDebit() {
      return debit;
   }

   public void setDebit(double debit) {
      this.debit = debit;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public double getPostTB() {
      return postTB;
   }

   public void setPostTB(double postTB) {
      this.postTB = postTB;
   }

}
