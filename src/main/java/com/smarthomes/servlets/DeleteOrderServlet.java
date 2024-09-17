package com.smarthomes.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.smarthomes.models.Order;

@WebServlet("/delete_order")
public class DeleteOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String ORDER_FILE_PATH = "D:\\Docs\\Fall2024\\EWA\\smarthomes_backend\\orders.ser"; // Path to orders file
    
    private static List<Order> orders = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        // Load existing orders from file when the servlet initializes
        File orderFile = new File(ORDER_FILE_PATH);
        if (orderFile.exists()) {
            orders = loadOrdersFromFile();
        }
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Extract orderId from query parameter
        String orderId = request.getParameter("orderId");
        
        // Ensure orderId is not null
        if (orderId != null) {
            // Find and remove the order with the given orderId
            boolean orderDeleted = orders.removeIf(order -> order.getOrderId().equals(orderId));
    
            if (orderDeleted) {
                saveOrdersToFile(); // Save the updated order list
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(new JSONObject().put("message", "Order deleted successfully").toString());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().print(new JSONObject().put("error", "Order not found").toString());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(new JSONObject().put("error", "Invalid request: Missing orderId").toString());
        }
    }
    

    // Load orders from file
    @SuppressWarnings("unchecked")
    private static List<Order> loadOrdersFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE_PATH))) {
            return (List<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save orders to file
    private static void saveOrdersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDER_FILE_PATH))) {
            oos.writeObject(orders);
            System.out.println("Order data written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Set CORS headers
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    // Handle preflight CORS requests
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
