package com.shoppingcart.util;

import com.shoppingcart.model.Order;
import com.shoppingcart.model.OrderItem;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLOrderManager {
    private static final String ORDERS_XML_FILE = "orders.xml";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void saveOrderToXML(Order order) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;
            Element root;
            
            File file = new File(ORDERS_XML_FILE);
            if (file.exists()) {
                doc = builder.parse(file);
                root = doc.getDocumentElement();
            } else {
                doc = builder.newDocument();
                root = doc.createElement("orders");
                doc.appendChild(root);
            }
            
            Element orderElement = doc.createElement("order");
            orderElement.setAttribute("id", String.valueOf(order.getOrderId()));
            orderElement.setAttribute("userId", String.valueOf(order.getUserId()));
            orderElement.setAttribute("orderDate", DATE_FORMAT.format(order.getOrderDate()));
            orderElement.setAttribute("status", order.getStatus());
            orderElement.setAttribute("totalAmount", order.getTotalAmount().toString());
            orderElement.setAttribute("paymentMethod", order.getPaymentMethod());
            orderElement.setAttribute("paymentStatus", order.getPaymentStatus());
            
            Element shippingElement = doc.createElement("shippingAddress");
            shippingElement.setTextContent(order.getShippingAddress());
            orderElement.appendChild(shippingElement);
            
            Element itemsElement = doc.createElement("items");
            for (OrderItem item : order.getItems()) {
                Element itemElement = doc.createElement("item");
                itemElement.setAttribute("productId", String.valueOf(item.getProductId()));
                itemElement.setAttribute("productName", item.getProductName());
                itemElement.setAttribute("quantity", String.valueOf(item.getQuantity()));
                itemElement.setAttribute("price", item.getPrice().toString());
                itemElement.setAttribute("subtotal", item.getSubtotal().toString());
                itemsElement.appendChild(itemElement);
            }
            orderElement.appendChild(itemsElement);
            
            root.appendChild(orderElement);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(ORDERS_XML_FILE));
            transformer.transform(source, result);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<Order> loadOrdersFromXML() {
        List<Order> orders = new ArrayList<>();
        try {
            File file = new File(ORDERS_XML_FILE);
            if (!file.exists()) {
                return orders;
            }
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            
            NodeList orderNodes = doc.getElementsByTagName("order");
            for (int i = 0; i < orderNodes.getLength(); i++) {
                Node orderNode = orderNodes.item(i);
                if (orderNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element orderElement = (Element) orderNode;
                    Order order = new Order();
                    order.setOrderId(Integer.parseInt(orderElement.getAttribute("id")));
                    order.setUserId(Integer.parseInt(orderElement.getAttribute("userId")));
                    order.setOrderDate(DATE_FORMAT.parse(orderElement.getAttribute("orderDate")));
                    order.setStatus(orderElement.getAttribute("status"));
                    order.setTotalAmount(new BigDecimal(orderElement.getAttribute("totalAmount")));
                    order.setPaymentMethod(orderElement.getAttribute("paymentMethod"));
                    order.setPaymentStatus(orderElement.getAttribute("paymentStatus"));
                    
                    NodeList shippingNodes = orderElement.getElementsByTagName("shippingAddress");
                    if (shippingNodes.getLength() > 0) {
                        order.setShippingAddress(shippingNodes.item(0).getTextContent());
                    }
                    
                    NodeList itemNodes = orderElement.getElementsByTagName("item");
                    List<OrderItem> items = new ArrayList<>();
                    for (int j = 0; j < itemNodes.getLength(); j++) {
                        Element itemElement = (Element) itemNodes.item(j);
                        OrderItem item = new OrderItem();
                        item.setProductId(Integer.parseInt(itemElement.getAttribute("productId")));
                        item.setProductName(itemElement.getAttribute("productName"));
                        item.setQuantity(Integer.parseInt(itemElement.getAttribute("quantity")));
                        item.setPrice(new BigDecimal(itemElement.getAttribute("price")));
                        item.setSubtotal(new BigDecimal(itemElement.getAttribute("subtotal")));
                        items.add(item);
                    }
                    order.setItems(items);
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public static String exportOrdersToXMLString(List<Order> orders) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            
            Element root = doc.createElement("orders");
            doc.appendChild(root);
            
            for (Order order : orders) {
                Element orderElement = doc.createElement("order");
                orderElement.setAttribute("id", String.valueOf(order.getOrderId()));
                orderElement.setAttribute("userId", String.valueOf(order.getUserId()));
                orderElement.setAttribute("orderDate", DATE_FORMAT.format(order.getOrderDate()));
                orderElement.setAttribute("status", order.getStatus());
                orderElement.setAttribute("totalAmount", order.getTotalAmount().toString());
                orderElement.setAttribute("paymentMethod", order.getPaymentMethod());
                orderElement.setAttribute("paymentStatus", order.getPaymentStatus());
                
                Element shippingElement = doc.createElement("shippingAddress");
                shippingElement.setTextContent(order.getShippingAddress());
                orderElement.appendChild(shippingElement);
                
                Element itemsElement = doc.createElement("items");
                for (OrderItem item : order.getItems()) {
                    Element itemElement = doc.createElement("item");
                    itemElement.setAttribute("productId", String.valueOf(item.getProductId()));
                    itemElement.setAttribute("productName", item.getProductName());
                    itemElement.setAttribute("quantity", String.valueOf(item.getQuantity()));
                    itemElement.setAttribute("price", item.getPrice().toString());
                    itemElement.setAttribute("subtotal", item.getSubtotal().toString());
                    itemsElement.appendChild(itemElement);
                }
                orderElement.appendChild(itemsElement);
                root.appendChild(orderElement);
            }
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

