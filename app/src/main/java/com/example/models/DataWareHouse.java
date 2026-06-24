package com.example.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DataWareHouse {
    private static ArrayList<Category> categories;
    private static ArrayList<Product> products;
    private static ArrayList<Employee> employees;
    private static ArrayList<Customer> customers;
    private static ArrayList<Order> orders;
    private static ArrayList<OrderDetail> orderDetails;

    public static ArrayList<Category> getCategories() {
        if (categories == null) {
            categories = new ArrayList<>();
            categories.add(new Category("c1", "Mì các loại", "Mì chống đói"));
            categories.add(new Category("c2", "Rau củ quả", "Rau củ quả tươi"));
            categories.add(new Category("c3", "Nước uống có ga", "Nước uống có ga"));
            categories.add(new Category("c4", "Trái cây", "Trái cây Vietgap"));
            categories.add(new Category("c5", "Thịt", "Thịt các loại"));
        }
        return categories;
    }

    public static ArrayList<Product> getProducts() {
        if (products == null) {
            products = new ArrayList<>();
            ArrayList<Category> cats = getCategories();

            // c1: Mì các loại
            products.add(new Product("p1", "Mì Omachi sườn hầm", 20000, 100, 0.05, 0.1, cats.get(0).getCategoryId()));
            products.add(new Product("p2", "Mì Hảo Hảo chua cay", 5000, 200, 0, 0.1, cats.get(0).getCategoryId()));
            products.add(new Product("p3", "Mì Indomie trộn", 6000, 150, 0.02, 0.1, cats.get(0).getCategoryId()));
            products.add(new Product("p4", "Mì Koreno vị bò", 15000, 80, 0.1, 0.1, cats.get(0).getCategoryId()));

            // c2: Rau củ quả
            products.add(new Product("p5", "Cải thìa tươi", 12000, 50, 0, 0, cats.get(1).getCategoryId()));
            products.add(new Product("p6", "Bắp cải Đà Lạt", 15000, 40, 0.05, 0, cats.get(1).getCategoryId()));
            products.add(new Product("p7", "Cà rốt hữu cơ", 25000, 60, 0, 0, cats.get(1).getCategoryId()));
            products.add(new Product("p8", "Khoai tây Đà Lạt", 22000, 100, 0.03, 0, cats.get(1).getCategoryId()));

            // c3: Nước uống có ga
            products.add(new Product("p9", "Coca Cola 330ml", 10000, 300, 0.05, 0.1, cats.get(2).getCategoryId()));
            products.add(new Product("p10", "Pepsi lon", 10000, 250, 0.05, 0.1, cats.get(2).getCategoryId()));
            products.add(new Product("p11", "7Up vị chanh", 9000, 200, 0, 0.1, cats.get(2).getCategoryId()));
            products.add(new Product("p12", "Mirinda cam", 9000, 180, 0.02, 0.1, cats.get(2).getCategoryId()));

            // c4: Trái cây
            products.add(new Product("p13", "Táo Envy Mỹ", 120000, 30, 0.1, 0, cats.get(3).getCategoryId()));
            products.add(new Product("p14", "Nho mẫu đơn", 350000, 20, 0.2, 0, cats.get(3).getCategoryId()));
            products.add(new Product("p15", "Cam sành", 35000, 100, 0, 0, cats.get(3).getCategoryId()));
            products.add(new Product("p16", "Xoài cát Hòa Lộc", 65000, 45, 0.05, 0, cats.get(3).getCategoryId()));

            // c5: Thịt
            products.add(new Product("p17", "Thịt heo ba chỉ", 150000, 25, 0.05, 0, cats.get(4).getCategoryId()));
            products.add(new Product("p18", "Thịt bò Mỹ", 450000, 15, 0.1, 0, cats.get(4).getCategoryId()));
            products.add(new Product("p19", "Ức gà phi lê", 85000, 40, 0, 0, cats.get(4).getCategoryId()));
            products.add(new Product("p20", "Sườn non heo", 180000, 20, 0.08, 0, cats.get(4).getCategoryId()));
        }
        return products;
    }

    public static Product downloadProduct(int i) {
        ArrayList<Product> products=getProducts();
        if(i<0||i>=products.size())
            return null;
        return products.get(i);
    }

    public static ArrayList<Employee> getEmployees() {
        if (employees == null) {
            employees = new ArrayList<>();
            employees.add(new Employee("e1", "Nguyễn Văn A", "0123456789", "Hà Nội"));
            employees.add(new Employee("e2", "Trần Thị B", "0987654321", "TP. Hồ Chí Minh"));
            employees.add(new Employee("e3", "Lê Văn C", "0111222333", "Đà Nẵng"));
            employees.add(new Employee("e4", "Phạm Minh D", "0444555666", "Hải Phòng"));
            employees.add(new Employee("e5", "Hoàng Thu E", "0777888999", "Cần Thơ"));
            employees.add(new Employee("e6", "Đỗ Văn F", "0666777888", "Huế"));
            employees.add(new Employee("e7", "Ngô Thị G", "0555444333", "Nam Định"));
            employees.add(new Employee("e8", "Lý Công H", "0333222111", "Nghệ An"));
            employees.add(new Employee("e9", "Vũ Minh I", "0222111000", "Quảng Ninh"));
            employees.add(new Employee("e10", "Bùi Thị K", "0999888777", "Bắc Ninh"));
            employees.add(new Employee("e11", "Trịnh Văn L", "0888777666", "Thanh Hóa"));
        }
        return employees;
    }

    public static ArrayList<Customer> getCustomers() {
        if (customers == null) {
            customers = new ArrayList<>();
            Calendar cal = Calendar.getInstance();

            String[] ho = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Phan", "Vũ", "Đặng", "Bùi", "Đỗ"};
            String[] dem = {"Văn", "Thị", "Minh", "Anh", "Đức", "Hồng", "Quang", "Ngọc", "Thanh", "Hoàng"};
            String[] ten = {"Anh", "Bình", "Chi", "Dũng", "Em", "An", "Giang", "Hương", "Khánh", "Linh"};
            String[] tinh = {"Hà Nội", "TP.HCM", "Đà Nẵng", "Cần Thơ", "Hải Phòng", "Bình Dương", "Đồng Nai", "Long An", "Tiền Giang", "Bến Tre"};

            for (int i = 1; i <= 100; i++) {
                int year = 1950 + (i % 61);
                int month = (i - 1) % 12;
                int day = 1 + ((i * 7) % 28);
                cal.set(year, month, day);

                String custId = "cust" + i;
                String fullName = ho[(i / 10) % ho.length] + " " + dem[i % dem.length] + " " + ten[(i + i / 3) % ten.length];
                String phone = "09" + String.format("%08d", Math.abs((long) i * 1234567) % 100000000);
                String email = "customer" + i + "@gmail.com";
                String address = tinh[i % tinh.length];

                customers.add(new Customer(custId, fullName, phone, email, cal.getTime(), address));
            }
        }
        return customers;
    }

    public static ArrayList<Order> getOrders() {
        if (orders == null) {
            orders = new ArrayList<>();
            ArrayList<Customer> custs = getCustomers();
            ArrayList<Employee> emps = getEmployees();
            Calendar baseCal = Calendar.getInstance();

            OrderStatus[] statuses = {
                    OrderStatus.COMPLETED,
                    OrderStatus.NOT_PAYMENT,
                    OrderStatus.ON_LOGISTIC,
                    OrderStatus.COMPLAINT
            };

            baseCal.set(2024, Calendar.JANUARY, 1, 8, 30, 0);

            for (int i = 0; i < 100; i++) {
                String orderId = "o" + (i + 1);
                String customerId = custs.get(i % custs.size()).getCustomerId();
                String employeeId = emps.get(i % emps.size()).getId();

                Calendar orderCal = (Calendar) baseCal.clone();
                orderCal.add(Calendar.DAY_OF_YEAR, i * 8);
                orderCal.add(Calendar.HOUR_OF_DAY, (i * 3) % 12);
                orderCal.add(Calendar.MINUTE, (i * 7) % 60);

                OrderStatus status = statuses[i % statuses.length];
                orders.add(new Order(orderId, customerId, employeeId, orderCal.getTime(), status));
            }
        }
        return orders;
    }

    public static ArrayList<OrderDetail> getOrderDetails() {
        if (orderDetails == null) {
            orderDetails = new ArrayList<>();
            ArrayList<Order> ords = getOrders();
            ArrayList<Product> prods = getProducts();

            int detailCounter = 1;
            for (int i = 0; i < ords.size(); i++) {
                Order order = ords.get(i);
                int numDetails = 1 + (i % 10);

                for (int j = 0; j < numDetails; j++) {
                    int productIndex = (i + j) % prods.size();
                    Product p = prods.get(productIndex);

                    String detailId = "d" + detailCounter++;
                    int quantity = 1 + ((i + j) % 5);
                    double price = p.getPrice();
                    double coupon = p.getCoupon();
                    double vat = p.getVAT();

                    orderDetails.add(new OrderDetail(detailId, order.getOrderId(), quantity, price, coupon, vat));
                }
            }
        }
        return orderDetails;
    }

    public static ArrayList<Order> filterOrders(Date fromDate, Date toDate, OrderStatus status) {
        ArrayList<Order> allOrders = getOrders();
        ArrayList<Order> results = new ArrayList<>();

        Calendar calFrom = Calendar.getInstance();
        if (fromDate != null) {
            calFrom.setTime(fromDate);
            calFrom.set(Calendar.HOUR_OF_DAY, 0);
            calFrom.set(Calendar.MINUTE, 0);
            calFrom.set(Calendar.SECOND, 0);
            calFrom.set(Calendar.MILLISECOND, 0);
        }

        Calendar calTo = Calendar.getInstance();
        if (toDate != null) {
            calTo.setTime(toDate);
            calTo.set(Calendar.HOUR_OF_DAY, 23);
            calTo.set(Calendar.MINUTE, 59);
            calTo.set(Calendar.SECOND, 59);
            calTo.set(Calendar.MILLISECOND, 999);
        }

        for (Order order : allOrders) {
            boolean matchesDate = true;
            if (fromDate != null || toDate != null) {
                Date orderDate = order.getOrderDate();
                if (fromDate != null && orderDate.before(calFrom.getTime())) matchesDate = false;
                if (toDate != null && orderDate.after(calTo.getTime())) matchesDate = false;
            }

            boolean matchesStatus = (status == null || status == OrderStatus.ALL || order.getOrderStatus() == status);

            if (matchesDate && matchesStatus) {
                results.add(order);
            }
        }
        return results;
    }

    public static String getEmployeeNameById(String id) {
        for (Employee e : getEmployees()) {
            if (e.getId().equals(id)) {
                return e.getName();
            }
        }
        return "Unknown";
    }

    public static double sumOfMoneyForOrder(Order od) {
        double sum = 0;
        ArrayList<OrderDetail> details = getOrderDetails();
        for (OrderDetail detail : details) {
            if (detail.getOrderId().equals(od.getOrderId())) {
                double lineTotal = detail.getQuantity() * detail.getPrice();
                double afterDiscount = lineTotal * (1 - detail.getCoupon());
                double afterVAT = afterDiscount * (1 + detail.getVAT());
                sum += afterVAT;
            }
        }
        return sum;
    }

    public static ArrayList<Order> filterOrdersByDate(Date fromDate, Date toDate) {
        ArrayList<Order> allOrders = getOrders();
        ArrayList<Order> results = new ArrayList<>();

        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(fromDate);
        calFrom.set(Calendar.HOUR_OF_DAY, 0);
        calFrom.set(Calendar.MINUTE, 0);
        calFrom.set(Calendar.SECOND, 0);
        calFrom.set(Calendar.MILLISECOND, 0);

        Calendar calTo = Calendar.getInstance();
        calTo.setTime(toDate);
        calTo.set(Calendar.HOUR_OF_DAY, 0);
        calTo.set(Calendar.MINUTE, 0);
        calTo.set(Calendar.SECOND, 0);
        calTo.set(Calendar.MILLISECOND, 0);

        for (Order order : allOrders) {
            Calendar calOrder = Calendar.getInstance();
            calOrder.setTime(order.getOrderDate());
            calOrder.set(Calendar.HOUR_OF_DAY, 0);
            calOrder.set(Calendar.MINUTE, 0);
            calOrder.set(Calendar.SECOND, 0);
            calOrder.set(Calendar.MILLISECOND, 0);

            if (!calOrder.before(calFrom) && !calOrder.after(calTo)) {
                results.add(order);
            }
        }
        return results;
    }

    public static ArrayList<Order> filterOrdersByStatus(OrderStatus status) {
        ArrayList<Order> allOrders = getOrders();
        if (status == null || status == OrderStatus.ALL) {
            return new ArrayList<>(allOrders);
        }
        ArrayList<Order> results = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getOrderStatus() == status) {
                results.add(order);
            }
        }
        return results;
    }
}
