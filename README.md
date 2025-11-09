# Online Shopping Cart System

A comprehensive web-based Online Shopping Cart system built with Java Servlets, JSP, and XML for order storage.

## Features

### User Authentication & Roles
- Secure login for customers and admin
- Role-based access control (Customer/Admin)
- Session management
- Password encryption using BCrypt

### Product Catalog Management
- Add, update, and delete products
- Product details: name, price, category, stock, description
- Search products by name, category, or description
- Filter products by category and price range
- Low stock alerts

### Shopping Cart
- Add products to cart
- Update quantities
- Remove items from cart
- Real-time cart total calculation
- Stock validation

### Checkout & Payment
- Secure checkout process
- Multiple payment methods (Credit Card, Debit Card, PayPal, Cash on Delivery)
- Order confirmation
- Stock reduction on order placement

### XML-based Order History
- Store orders in XML format
- Retrieve order history for customers and admins
- Export orders to XML/Excel format

### Customer Management
- Customer registration
- Profile management
- View order history
- Track order status

### Admin Dashboard
- Sales statistics
- Product management
- Order management
- Reports and analytics
- Low stock alerts

### Reports & Analytics
- Sales reports by product, category, and time period
- Revenue tracking
- Export to Excel format
- Sales by category analysis

### Notifications & Alerts
- Email confirmation for orders (configurable)
- Low stock alerts for admin
- Order status updates

### Security Features
- Password hashing with BCrypt
- Session-based authentication
- Role-based access control
- Input validation

## Technology Stack

- **Backend**: Java Servlets, JSP
- **Frontend**: HTML, CSS, JavaScript, JSTL
- **Build Tool**: Maven
- **Data Storage**: In-memory (DAO pattern) + XML files
- **Libraries**:
  - Jackson (JSON processing)
  - Apache POI (Excel export)
  - JavaMail (Email notifications)
  - BCrypt (Password hashing)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/shoppingcart/
│   │       ├── model/          # Data models
│   │       ├── dao/            # Data Access Objects
│   │       ├── servlet/        # Servlets
│   │       ├── service/        # Business logic
│   │       ├── util/           # Utilities
│   │       └── filter/         # Filters
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml         # Web configuration
│       ├── admin/              # Admin pages
│       ├── css/                # Stylesheets
│       ├── error/              # Error pages
│       └── *.jsp              # JSP pages
└── pom.xml                     # Maven configuration
```

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- Servlet container (Tomcat 9+ recommended)

### Installation

1. Clone or download the project
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean package
   ```
4. Deploy the WAR file to your servlet container
5. Access the application at `http://localhost:8080/online-shopping-cart`

### Default Credentials

**Admin:**
- Username: `admin`
- Password: `admin123`

**Customer:**
- Username: `customer`
- Password: `customer123`

## Configuration

### Email Configuration
To enable email notifications, update the email settings in `EmailService.java`:
- `FROM_EMAIL`: Your email address
- `FROM_PASSWORD`: Your email password or app password
- `SMTP_HOST`: SMTP server host (default: smtp.gmail.com)
- `SMTP_PORT`: SMTP server port (default: 587)

### XML Order Storage
Orders are automatically saved to `orders.xml` in the application root directory. The file is created automatically when the first order is placed.

## Usage

### For Customers
1. Register a new account or login
2. Browse products
3. Add items to cart
4. Proceed to checkout
5. View order history in "My Orders"

### For Admins
1. Login with admin credentials
2. Access admin dashboard
3. Manage products (add, edit, delete)
4. View and manage orders
5. Generate sales reports
6. Monitor low stock alerts

## Features in Detail

### Product Management
- Admins can add products with details
- Search and filter functionality
- Category-based organization
- Stock management with alerts

### Order Processing
- Orders are stored in XML format
- Automatic stock reduction
- Email confirmations (if configured)
- Order status tracking

### Reports
- Sales reports by date range
- Category-wise sales analysis
- Product-wise sales tracking
- Excel export functionality

## Security Considerations

- Passwords are hashed using BCrypt
- Session-based authentication
- Role-based access control
- Input validation on forms

## Future Enhancements

- Database integration (MySQL/PostgreSQL)
- Payment gateway integration
- Advanced search with filters
- Product reviews and ratings
- Wishlist functionality
- Coupon/discount system
- Multi-language support

## License

This project is provided as-is for educational purposes.

## Support

For issues or questions, please refer to the project documentation or contact the development team.

