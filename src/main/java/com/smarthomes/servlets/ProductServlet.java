package com.smarthomes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.smarthomes.models.Product;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static HashMap<String, List<Product>> categorizedProducts = new HashMap<>();

    @Override
    public void init() throws ServletException {
        List<Product> smartDoorbells = new ArrayList<>();
        smartDoorbells.add(new Product("VisionPro5000", 149.99, "Smart Doorbells", "Sleek rectangular design with a black glossy surface."));
        smartDoorbells.add(new Product("RingGuardX1", 199.99, "Smart Doorbells", "Round smart doorbell with a central camera."));
        smartDoorbells.add(new Product("SlimSecure300", 129.99, "Smart Doorbells", "Minimalist smart doorbell with a slim design."));
        smartDoorbells.add(new Product("UltraViewCylindro", 249.99, "Smart Doorbells", "Cylindrical design with a modern black and silver combination."));
        smartDoorbells.add(new Product("HomeSafeElite", 179.99, "Smart Doorbells", "Sleek and modern smart doorbell with a rectangular finish."));

        List<Product> smartDoorlocks = new ArrayList<>();
        smartDoorlocks.add(new Product("SecureLockPro", 199.99, "Smart Doorlocks", "A modern smart door lock with a sleek rectangular design."));
        smartDoorlocks.add(new Product("CyberShield360", 249.99, "Smart Doorlocks", "A futuristic round smart door lock with a fingerprint sensor."));
        smartDoorlocks.add(new Product("TitanGuardTouch", 229.99, "Smart Doorlocks", "A high-tech rectangular smart door lock with a metallic silver finish."));
        smartDoorlocks.add(new Product("NeoSecureX1", 179.99, "Smart Doorlocks", "A minimalist smart door lock with a matte black finish."));
        smartDoorlocks.add(new Product("OptiLockInfinity", 269.99, "Smart Doorlocks", "A sleek smart door lock with a rectangular matte black design."));

        List<Product> smartSpeakers = new ArrayList<>();
        smartSpeakers.add(new Product("EchoBlast360", 149.99, "Smart Speakers", "A modern cylindrical smart speaker with a sleek matte black finish."));
        smartSpeakers.add(new Product("SoundWaveAura", 199.99, "Smart Speakers", "A futuristic oval-shaped smart speaker with a digital display."));
        smartSpeakers.add(new Product("CubeSoundMini", 129.99, "Smart Speakers", "A compact cube-shaped smart speaker with rounded edges."));
        smartSpeakers.add(new Product("PulseBeatVertical", 179.99, "Smart Speakers", "A sleek rectangular smart speaker with a vertical design."));
        smartSpeakers.add(new Product("SonicCoreNeo", 159.99, "Smart Speakers", "A cylindrical smart speaker with a fabric exterior."));

        List<Product> smartLights = new ArrayList<>();
        smartLights.add(new Product("LumiGlowPro", 29.99, "Smart Lightings", "A modern cylindrical smart light bulb with a sleek metallic finish."));
        smartLights.add(new Product("AuraBrightRGB", 49.99, "Smart Lightings", "A futuristic smart LED light bulb with a spherical top."));
        smartLights.add(new Product("FlexiLightStrip", 59.99, "Smart Lightings", "A smart light strip with a flexible design."));
        smartLights.add(new Product("HaloSmartCeiling", 79.99, "Smart Lightings", "A smart ceiling light with a circular design."));
        smartLights.add(new Product("EdgeGlowWall", 69.99, "Smart Lightings", "A futuristic smart wall light with a rectangular design."));

        List<Product> smartThermostats = new ArrayList<>();
        smartThermostats.add(new Product("TempSensePro", 199.99, "Smart Thermostats", "A modern smart thermostat with a circular design."));
        smartThermostats.add(new Product("ClimaGuardX1", 249.99, "Smart Thermostats", "A futuristic smart thermostat with a square design."));
        smartThermostats.add(new Product("OvalAirElite", 179.99, "Smart Thermostats", "A smart thermostat with an oval design."));
        smartThermostats.add(new Product("EcoControlS2", 229.99, "Smart Thermostats", "A sleek rectangular smart thermostat with a digital display."));
        smartThermostats.add(new Product("ThermoCoreNeo", 269.99, "Smart Thermostats", "A premium smart thermostat with a circular digital display."));

        // Store categorized products in the HashMap
        categorizedProducts.put("Smart Doorbells", smartDoorbells);
        categorizedProducts.put("Smart Doorlocks", smartDoorlocks);
        categorizedProducts.put("Smart Speakers", smartSpeakers);
        categorizedProducts.put("Smart Lightings", smartLights);
        categorizedProducts.put("Smart Thermostats", smartThermostats);
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); 
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);

        String category = request.getParameter("category");
        List<Product> productList = new ArrayList<>();

        if (category == null || category.equals("All Products")) {
            for (List<Product> products : categorizedProducts.values()) {
                productList.addAll(products);
            }
        } else {
            productList = categorizedProducts.getOrDefault(category, new ArrayList<>());
        }

        JSONArray productArray = new JSONArray();
        for (Product product : productList) {
            JSONObject productJSON = new JSONObject();
            productJSON.put("name", product.getName());
            productJSON.put("price", product.getPrice());
            productJSON.put("category", product.getCategory());
            productJSON.put("description", product.getDescription());
            productArray.put(productJSON);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(productArray.toString());
        out.flush();
    }
}



