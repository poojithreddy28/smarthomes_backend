package com.smarthomes.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.smarthomes.models.Cart;
import com.smarthomes.models.Order;

@WebServlet("/place_order")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CART_FILE_PATH = "D:\\Docs\\Fall2024\\EWA\\smarthomes_backend\\cart_data.ser"; // Path to cart data
    private static final String ORDER_FILE_PATH = "D:\\Docs\\Fall2024\\EWA\\smarthomes_backend\\orders.ser";   // Path to order data
    
    private static HashMap<String, HashMap<String, Cart>> userCarts = new HashMap<>();
    private static List<Order> orders = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        // Load cart data from file when the servlet initializes
        File cartFile = new File(CART_FILE_PATH);
        if (cartFile.exists()) {
            userCarts = loadCartsFromFile();
        }

        // Load existing orders from file if any
        File orderFile = new File(ORDER_FILE_PATH);
        if (orderFile.exists()) {
            orders = loadOrdersFromFile();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Set CORS headers if necessary
        setCorsHeaders(response);
        
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }
        String requestData = sb.toString();
        
        try {
            // Parse incoming JSON data
            JSONObject json = new JSONObject(requestData);
            String username = json.getString("username");

            // Extract checkout details
            String firstName = json.getString("firstName");
            String lastName = json.getString("lastName");
            String phone = json.getString("phone");
            String email = json.getString("email");
            String address = json.getString("address");
            String city = json.getString("city");
            String state = json.getString("state");
            String postalCode = json.getString("postalCode");
            String cardNumber = json.getString("cardNumber");
            String expiry = json.getString("expiry");
            String cvv = json.getString("cvv");
            String shippingMethod = json.getString("shippingMethod");
            String storeLocation = json.has("storeLocation") ? json.getString("storeLocation") : null;

            // Retrieve the cart for the given user
            if (userCarts.containsKey(username)) {
                HashMap<String, Cart> userCart = userCarts.get(username);
                
                // Create a new Order object
                Order newOrder = new Order(username, firstName, lastName, phone, email, address, city, state, postalCode, cardNumber, expiry, cvv, shippingMethod, storeLocation, userCart);

                // Add the new order to the list
                orders.add(newOrder);
                
                // Save updated order data to file
                saveOrdersToFile();

                // Clear the user's cart after placing the order
                userCarts.remove(username);
                saveCartsToFile();

                // Respond with success message
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Order placed successfully.");
                jsonResponse.put("orderId", newOrder.getOrderId());  // Respond with the order ID
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
            } else {
                // If cart not found, send error
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("error", "Cart not found for user.");
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error", "Failed to place order.");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
        }
    }

    // Load carts from file
    @SuppressWarnings("unchecked")
    private static HashMap<String, HashMap<String, Cart>> loadCartsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CART_FILE_PATH))) {
            return (HashMap<String, HashMap<String, Cart>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Save carts to file
    private static void saveCartsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CART_FILE_PATH))) {
            oos.writeObject(userCarts);
        } catch (IOException e) {
            e.printStackTrace();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Set CORS headers
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
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
