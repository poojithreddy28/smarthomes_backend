package com.smarthomes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Function to add CORS headers for cross-origin requests
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // Change to your frontend URL
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        // Set CORS headers for preflight requests (OPTIONS method)
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set CORS headers for POST requests
        setCorsHeaders(response);

        // Get product name from the request
        String productName = request.getParameter("productName");

        // Get session and retrieve or create a cart list
        HttpSession session = request.getSession();
        List<String> cart = (List<String>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Add the product to the cart
        cart.add(productName);

        // Save the updated cart in the session
        session.setAttribute("cart", cart);

        // Respond with a success message
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", "Product added to cart successfully");
        out.print(jsonResponse);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set CORS headers for GET requests
        setCorsHeaders(response);

        // Get session and retrieve the cart list
        HttpSession session = request.getSession();
        List<String> cart = (List<String>) session.getAttribute("cart");

        // Convert cart list to JSON
        JSONArray cartArray = new JSONArray();
        if (cart != null) {
            for (String product : cart) {
                cartArray.put(product);
            }
        }

        // Send cart as JSON response
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(cartArray.toString());
        out.flush();
    }
}
