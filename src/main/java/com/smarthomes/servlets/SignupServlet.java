package com.smarthomes.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.smarthomes.models.NewUser;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private static final String FILE_PATH = "D:\\Docs\\Fall2024\\EWA\\smarthomes_backend\\users.ser"; // Adjust the path
    private static HashMap<String, NewUser> users = new HashMap<>();

    @Override
    public void init() throws ServletException {
        // Load serialized user data from file
        users = loadUsersFromFile();
    }

    // Handle preflight CORS request (for the OPTIONS method)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(response);  // Set CORS headers
        
        try {
            // Parse incoming JSON data
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            String requestData = sb.toString();

            JSONObject json = new JSONObject(requestData);
            String fullName = json.getString("fullName");
            String email = json.getString("email").toLowerCase();
            String password = json.getString("password");

            if (users.containsKey(email)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("error", "User with this email already exists.");
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                return;
            }

            // Register new user
            NewUser newUser = new NewUser(fullName, email, password);
            users.put(email, newUser);
            saveUsersToFile(); // Save the updated users map

            // Respond with success
            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Sign-up successful for " + fullName);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error", "Something went wrong. Please try again.");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            e.printStackTrace();
        }
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true"); // For credentials
    }

    private void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving users to file.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, NewUser> loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (HashMap<String, NewUser>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public void destroy() {
        // Save user data to file when the server is shutting down
        saveUsersToFile();
    }
}
