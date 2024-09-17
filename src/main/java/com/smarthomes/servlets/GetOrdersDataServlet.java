package com.smarthomes.servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.smarthomes.models.Order;

@WebServlet("/orders")
public class GetOrdersDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String ORDER_FILE_PATH = "D:\\Docs\\Fall2024\\EWA\\smarthomes_backend\\orders.ser";  // Path to order data

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set CORS headers if necessary
        setCorsHeaders(response);

        // Get the username from the request parameters
        String username = request.getParameter("username");
        
        if (username == null || username.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error", "Username is missing.");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            return;
        }

        // Load orders from file
        List<Order> orders = loadOrdersFromFile();

        if (orders == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error", "Failed to load orders.");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            return;
        }

        // Filter orders by username
        List<Order> userOrders = orders.stream()
                                        .filter(order -> order.getUsername().equals(username))
                                        .collect(Collectors.toList());

        // Convert the filtered orders to JSON and send the response
        JSONArray ordersArray = new JSONArray();

        for (Order order : userOrders) {
            JSONObject orderJson = new JSONObject();
            orderJson.put("orderId", order.getOrderId());
            orderJson.put("firstName", order.getFirstName());
            orderJson.put("lastName", order.getLastName());
            orderJson.put("phone", order.getPhone());
            orderJson.put("email", order.getEmail());
            orderJson.put("address", order.getAddress());
            orderJson.put("city", order.getCity());
            orderJson.put("state", order.getState());
            orderJson.put("postalCode", order.getPostalCode());
            orderJson.put("shippingMethod", order.getShippingMethod());
            orderJson.put("storeLocation", order.getStoreLocation());
            orderJson.put("deliveryDate",order.getDeliveryDate());

            // Add product details
            JSONObject productsJson = new JSONObject(order.getProducts());
            orderJson.put("products", productsJson);

            ordersArray.put(orderJson);
        }

        // Send the JSON response
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(ordersArray.toString());
    }

    // Load orders from file
    @SuppressWarnings("unchecked")
    private List<Order> loadOrdersFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE_PATH))) {
            return (List<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
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
