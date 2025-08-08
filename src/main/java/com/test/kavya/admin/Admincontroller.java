package com.test.kavya.admin;

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
public class Admincontroller{
	static final String DB_URL = "jdbc:mysql://localhost/admin?useSSL=false";
	static final String USER = "root";
	static final String PASS = "admin";
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	@PostMapping("/register")
    public String register(Model model,HttpServletRequest request) {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String query="INSERT INTO admins (username,password) VALUES ('"+username+"','"+password+"')";
         try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return "register_sucess";
    }
	@GetMapping("/delete")
	public String delete_account() {
		return "delete";
	}
	@PostMapping("/delete_confirm")
	public String delete(HttpServletRequest request) {
	        String name_str = request.getParameter("username");
	        String password=request.getParameter("password");
	        String id=request.getParameter("id");
	        String QUERY1="SELECT id FROM admins WHERE username = '"+name_str+"' AND password = '"+password+"'";
	        String QUERY="DELETE FROM admins WHERE id= ?";
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(QUERY1);

	            if(rs.next()) {
	                int admin_id=rs.getInt("id");
	                 PreparedStatement deleteStmt = conn.prepareStatement(QUERY);
	                 deleteStmt.setInt(1, admin_id);
	                 deleteStmt.executeUpdate();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return "delete_confirm";

	}
	@GetMapping("/forgot")
	public String forgot() {
		return "forgot";
	}
	@PostMapping("/forgot_confirm")
    public String forgot(HttpServletRequest request) {
        String name_str=request.getParameter("username");

        String newpwd_str=request.getParameter("new_password");
        String query1="SELECT id FROM admins WHERE username = '"+name_str+"'";
        String query2="UPDATE admins SET password ='"+newpwd_str+"' WHERE id = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query1);

            if(rs.next()) {
                int id=rs.getInt("id");
             PreparedStatement pstmt = conn.prepareStatement(query2);
             pstmt.setInt(1, id);

            pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "forgot_confirm";
    }
	@PostMapping("/login")
    public String logindata(HttpServletRequest request) {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String Query="SELECT * FROM admins WHERE username = '"+username+"' AND password = '"+password+"'";
         try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

                Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(Query);

                    if (rs.next()) {
                        int adminId = rs.getInt("id");

                         return "redirect:/product?admin_id=" + adminId;

                    } else {
                        return "redirect:/login?error=true";

                    }



            } catch (Exception e) {
                e.printStackTrace();
            }

         return "redirect:/login?error=exception";
    }

	

}

