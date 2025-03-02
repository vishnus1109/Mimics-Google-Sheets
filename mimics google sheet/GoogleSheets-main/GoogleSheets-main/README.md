# GoogleSheets
Web Application Mimicking Google Sheets

Project Overview:
This project is a Google Sheets-like application that allows users to create, view, and edit
spreadsheets using a web-based interface. It supports formula evaluation, cell updates, and dynamic data handling.
Features Implemented:
- Create new spreadsheets
- View and edit spreadsheet data
- Update individual cells with values or formulas
- Formula evaluation based on existing cell values
- Integration with Thymeleaf templates for UI rendering
- Spring Boot-based backend handling CRUD operations
- MySQL database for storing spreadsheets and cells
Technologies Used:
- Spring Boot (Java)
- Thymeleaf (for frontend templating)
- MySQL (Database)
- Hibernate/JPA (ORM for database handling)
- HTML, CSS, JavaScript (Frontend)
- Apache Tomcat (Server)
Issues Faced:
- Thymeleaf template rendering error due to null values
- Foreign key constraint preventing deletion of spreadsheets
- Handling formula evaluations efficiently
- Ensuring proper database relationships between spreadsheets and cells
- Fix the Thymeleaf template error by ensuring proper data binding

