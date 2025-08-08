package com.test.kavya.product;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import java.util.*;
import java.sql.*;
@Controller
public class Productcontroller {
	static final String DB_URL = "jdbc:mysql://localhost/admin?useSSL=false";
	static final String USER = "root";
	static final String PASS = "admin";
	@GetMapping("/product")
    public String ProductDetails(Model model,HttpServletRequest request) {
        List<Product> products = new ArrayList<>();
        String id_String= request.getParameter("admin_id");
         int Id_int = Integer.parseInt(id_String);
        String query = "SELECT id,name,cost,type,description FROM product WHERE admin_id = '"+Id_int+"'";
         try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                // Extract data from result set
                while (rs.next()) {
                    // Retrieve by column name
                    Product product=new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setCost(rs.getString("cost"));
                    product.setDescription(rs.getString("description"));
                    product.setType(rs.getString("type"));

                    products.add(product);


                }
                model.addAttribute("products", products);
                 model.addAttribute("admin_id", Id_int);
                stmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

    return "product";
    }

	@PostMapping("/addproduct")
    public String addproduct(HttpServletRequest request) {
         String name = request.getParameter("name");
         String type = request.getParameter("type");
         String description= request.getParameter("description");
         String cost = request.getParameter("cost");
         String adminIdStr = request.getParameter("admin_id");

            int adminId = Integer.parseInt(adminIdStr);

            String QUERY = "INSERT INTO product (name, type, description,cost, admin_id) VALUES ('"+name+"','"+type+"','"+description+"','"+cost+"','"+adminIdStr+"')";
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(QUERY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "redirect:/product?admin_id=" + adminId;
    }
	@PostMapping("/delete-product")
    public String deleteproduct(HttpServletRequest request) {
        String ProductIdStr=request.getParameter("product_id");
        String AdminIdStr=request.getParameter("admin_id");

        int productId=Integer.parseInt(ProductIdStr);
        int adminId=Integer.parseInt(AdminIdStr);

         String query1 = "DELETE FROM product WHERE id = '"+productId+"' AND admin_id = '"+adminId+"'";
         try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query1);



            } catch (Exception e) {
                e.printStackTrace();
            }
         return "redirect:/product?admin_id=" + adminId;
    }

	
}
