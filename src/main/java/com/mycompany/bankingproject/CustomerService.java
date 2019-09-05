package com.mycompany.bankingproject;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.hibernate.HibernateException;

@Path("/customers")
public class CustomerService {

   EntityManager entityManager;

   public CustomerService() {
      EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("test-connection");
      entityManager = emfactory.createEntityManager();
   }

   // get all customers details: curl http://localhost:8080/api/customers
   @GET
   @Produces({MediaType.APPLICATION_JSON})
   public Response getCustomers() {
      List<Customer> list = allEntries();
      GenericEntity entity = new GenericEntity<List<Customer>>(list) {
      };
      if (entity == null) {
         throw new NotFoundException("No data Available");
      }
      return Response.ok(entity).build();
   }

   public List<Customer> allEntries() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
      Root<Customer> rootEntry = cq.from(Customer.class);
      CriteriaQuery<Customer> all = cq.select(rootEntry);
      TypedQuery<Customer> allQuery = entityManager.createQuery(all);
      return allQuery.getResultList();
   }

   // get customer details: curl http://localhost:8080/api/customers/2
   @GET
   @Produces({MediaType.APPLICATION_JSON})
   @Path("/{id}")
   public Customer getCustomer(@PathParam("id") int id) {
      Customer cust = entityManager.find(Customer.class, id);
      System.out.println(cust);
      if (cust == null) {
         throw new NotFoundException("No data Available");
      }
      return cust;
   }

   // Update Customer email 
   @PUT
   @Path("/updateEmail/{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateEmail(@PathParam("id") int id, Customer cust) {
      
      try {
         entityManager.getTransaction().begin();
         Customer cus = entityManager.find(Customer.class, id);
         if (cust.getEmail() == null || cust.getEmail().equals(cus.getEmail())) {
            throw new NotFoundException("Try other Email");
         }
         cus.setEmail(cust.getEmail());
         entityManager.merge(cus);
         entityManager.getTransaction().commit();
         return Response.status(200).entity(cus).build();
      } finally {
         entityManager.close();
      }
   }

   // Update Customer Contact Number 
   @PUT
   @Path("updateContact/{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateContact(@PathParam("id") int id, Customer cust) {
      try {
         entityManager.getTransaction().begin();
         Customer cus = entityManager.getReference(Customer.class, id);
         if (cust.getContact() == null || cust.getContact().equals(cus.getContact())) {
            throw new NotFoundException("Try other contact Number");
         }
         cus.setContact(cust.getContact());
         entityManager.merge(cus);
         entityManager.getTransaction().commit();
         return Response.status(200).entity(cus).build();
      } finally {
         entityManager.close();
      }
   }

   // Update Customer Contact Number 
   @PUT
   @Path("updateAddress/{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateAddress(@PathParam("id") int id, Customer cust) {
      System.out.println(cust);
      try {
         entityManager.getTransaction().begin();
         Customer cus = entityManager.getReference(Customer.class, id);
         if (cust.getAddress() == null || cust.getAddress().equals(cus.getAddress())) {
            throw new NotFoundException("Enter New Address");
         }
         cus.setAddress(cust.getAddress());
         entityManager.merge(cus);
         entityManager.getTransaction().commit();
         return Response.status(200).entity(cus).build();
      } finally {
         entityManager.close();
      }
   }
   
   // get all Accounts for One customer
   // curl http://localhost:8080/api/customers/1/accounts/
   @GET
   @Path("/{id}/accounts")
   @Produces({MediaType.APPLICATION_JSON})
   public Response getAccounts(@PathParam("id") int id) {
      List<Account> list = allAccounts(id);
      GenericEntity entity = new GenericEntity<List<Account>>(list) {
      };
      if (entity == null) {
         throw new NotFoundException("No data Available");
      }
      return Response.ok(entity).build();
   }

   public List<Account> allAccounts(int custID) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Account> cq = cb.createQuery(Account.class);
      Root<Account> rootEntry = cq.from(Account.class);
      CriteriaQuery<Account> all = cq.select(rootEntry);
      cq.where(cb.equal(rootEntry.get("customerID"), custID));
      TypedQuery<Account> allQuery = entityManager.createQuery(all);
      return allQuery.getResultList();
   }

   // get account details: curl http://localhost:8080/api/customers/account/1
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/account")
   public Response getAccount(@Context UriInfo info) {
      String id = info.getQueryParameters().getFirst("id");
      if (id == null) {
         throw new NotFoundException("No data Available");
      }
      Account acc = entityManager.find(Account.class, Integer.parseInt(id));
      return Response.status(200).entity(acc).build();
   }

   // get all transactions for customer account
   // curl http://localhost:8080/api/customers/accountTrans/1
   @GET
   @Path("/accountTrans")
   @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
   public Response getTransactions(@Context UriInfo info) {
      String tid = info.getQueryParameters().getFirst("id");
      List<Transaction> list = allTrans(Integer.parseInt(tid));
      GenericEntity entity = new GenericEntity<List<Transaction>>(list) {
      };
      if (entity == null) {
         throw new NotFoundException("No data Available");
      }
      return Response.ok(entity).build();
   }

   public List<Transaction> allTrans(int accID) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
      Root<Transaction> rootEntry = cq.from(Transaction.class);
      CriteriaQuery<Transaction> all = cq.select(rootEntry);
      cq.where(cb.equal(rootEntry.get("accountID"), accID));
      TypedQuery<Transaction> allQuery = entityManager.createQuery(all);
      return allQuery.getResultList();
   }

   // get transaction Details
   @GET
   @Produces({MediaType.APPLICATION_JSON})
   @Path("/account/trans")
   public Response getTransaction(@Context UriInfo info) {
      String trid = info.getQueryParameters().getFirst("id");
      Transaction tran = entityManager.find(Transaction.class, Integer.parseInt(trid));
      System.out.println(tran);
      if (tran == null) {
         throw new NotFoundException("No data Available");
      }
      return Response.status(200).entity(tran).build();
   }

   // get balance for specific Account
   // curl http://localhost:8080/api/customers/account/balance/1
   @GET
   @Produces({MediaType.APPLICATION_JSON})
   @Path("/account/balance")
   public Response getAccountBalance(@Context UriInfo info) {
      String aid = info.getQueryParameters().getFirst("id");
      Account ac = entityManager.find(Account.class, Integer.parseInt(aid));
      if (ac == null) {
         throw new NotFoundException("Account not Available");
      }
      double bal = ac.getCurentBalance();
      Balance b = new Balance();
      b.setCurentBalance(bal);
      System.out.println(b);

      return Response.ok(b).build();
   }

   // Create Customer
   @POST
   @Path("/create")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   @Produces(MediaType.APPLICATION_JSON)
   public Response saveCustomer(@FormParam("name") String name
               , @FormParam("email") String email
               , @FormParam("address") String address
               , @FormParam("contact") String contact
               , @FormParam("securityCode") int securityCode){
      Customer c = new Customer();
      c.setName(name); c.setEmail(email); c.setAddress(address);
      c.setContact(contact); c.setSecurityCode(securityCode);
      entityManager.getTransaction().begin();
      if (email == null || contact == null) {
         throw new NotFoundException("No Data Entred");
      }
      entityManager.persist(c);
      entityManager.getTransaction().commit();
      entityManager.close();
      return Response.status(200).entity(c).build();
   }

   // Create  Account Post
   @POST
   @Path("/account/create")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   @Produces(MediaType.APPLICATION_JSON)
   public Response saveAccount(@FormParam("accountType") String accountType,
            @FormParam("accountN") int accountN,
            @FormParam("sortCode") int sortCode,
            @FormParam("customerID") int customerID) {
      Account a = new Account();
      a.setAccountNumber(accountN); a.setAccountType(accountType);
      a.setSortCode(sortCode); a.setCustomerID(customerID);
      entityManager.getTransaction().begin();
      entityManager.persist(a);
      entityManager.getTransaction().commit();
      entityManager.close();
      return Response.status(200).entity(a).build();
   }

   // make bank lodgement
   @POST
   @Path("/account/lodgement")
   @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
   @Produces(MediaType.APPLICATION_JSON)
   public Response saveLodgement(@FormParam("id") int id,
           @FormParam("credit") double credit,
           @FormParam("description") String description) {
      Transaction tr = new Transaction();
      try {
         entityManager.getTransaction().begin();
         Account acc = entityManager.find(Account.class, id);
         double bal = acc.getCurentBalance();
         double cb = bal + credit;
         acc.setCurentBalance(cb);
         entityManager.merge(acc);
         tr.setAccountID(id); tr.setCredit(credit); tr.setDescription(description); tr.setPostTB(cb);
         entityManager.persist(tr);
         entityManager.getTransaction().commit();
         entityManager.close();

      } catch (HibernateException ex) {
         ex.getMessage();
      }
      return Response.status(200).entity(tr).build();
   }

   // Withdrawal from bank Account
   @POST
   @Path("/account/withdrawal")
   @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
   @Produces(MediaType.APPLICATION_JSON)
   public Response withdrawal(@FormParam("id") int id,
           @FormParam("debit") double debit,
           @FormParam("description") String description) {
      Transaction tr = new Transaction();
      try {
         entityManager.getTransaction().begin();
         Account acc = entityManager.find(Account.class, id);
         double bal = acc.getCurentBalance();
         if (bal < debit) {
            throw new NotFoundException("No enough found available");
         }
         double cb = bal - debit;
         acc.setCurentBalance(cb);
         entityManager.merge(acc);
         tr.setAccountID(id); tr.setDebit(debit); tr.setDescription(description); tr.setPostTB(cb);
         entityManager.persist(tr);
         entityManager.getTransaction().commit();
         entityManager.close();

      } catch (HibernateException ex) {
         ex.getMessage();
      }
      return Response.status(200).entity(tr).build();
   }

   // Withdrawal from bank Account
   @POST
   @Path("/account/transfer")
   @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
   @Produces(MediaType.APPLICATION_JSON)
   public Response MakeTrasfer(@FormParam("id1") int id1, @FormParam("id2") int id2,
           @FormParam("amount") double amount,
           @FormParam("description") String description) {
      Transaction tr1 = new Transaction();
      Transaction tr2 = new Transaction();
      try {
         entityManager.getTransaction().begin();
         Account ac = entityManager.find(Account.class, id1);
         Account acc = entityManager.find(Account.class, id2);
         double bal1 = ac.getCurentBalance();
         double bal2 = acc.getCurentBalance();
         if (bal1 < amount) {
            throw new NotFoundException("No enough found available");
         }
         double cb1 = bal1 - id1;
         double cb2 = bal2 + id2;
         ac.setCurentBalance(cb1);
         acc.setCurentBalance(cb2);
         entityManager.merge(ac); entityManager.merge(acc);
         tr1.setAccountID(id1); tr1.setDebit(amount); tr1.setDescription(description); tr1.setPostTB(cb1);
         tr2.setAccountID(id2); tr2.setCredit(amount); tr2.setDescription(description); tr2.setPostTB(cb2);
         entityManager.persist(tr1); entityManager.persist(tr2);
         entityManager.getTransaction().commit();
         entityManager.close();

      } catch (HibernateException ex) {
         ex.getMessage();
      }
      return Response.status(200).entity(tr1).build();
   }

   // Delete Customer
   @DELETE
   @Path("/delete/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response DeleteCustomer(@PathParam("id") int id) {
      entityManager.getTransaction().begin();
      Customer cust = entityManager.find(Customer.class, id);
      System.out.println(cust);
      if (cust == null) {
         throw new NotFoundException("customer not exist");
      }
      entityManager.remove(cust); 
      entityManager.getTransaction().commit();
      entityManager.close();
      return Response.status(200).entity(cust).build();
   }

}
