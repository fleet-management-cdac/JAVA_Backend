Image Handling: Static Folder Method
This project now uses Spring Boot's built-in Static Resource Handling.
Instead of reading files from a specific drive (like C: or D:),
images are stored inside the project folder itself. This makes the code portable and easier to deploy.

1. Project Changes
A. Storage Location
Path: src/main/resources/static/images/

Action: All vehicle type images (e.g., sedan.jpg, suv.png) must be pasted into this folder manually.

Behavior: Spring Boot automatically serves any file inside static at the root URL (e.g., localhost:8080/images/filename).

B. Backend Code Updates
Updated: SecurityConfig.java
Change: Added .requestMatchers("/images/**").permitAll()
Reason: Ensures the frontend can load images without being logged in.

C. Database Strategy
Data Stored: The database only stores the filename (e.g., suv.jpg), not the full path.
DataBase Query:
UPDATE vehicle_types SET img_url = 'economy.jpg' WHERE type_name = 'Economy';
UPDATE vehicle_types SET img_url = 'sedan.jpg' WHERE type_name = 'Sedan';
UPDATE vehicle_types SET img_url = 'suv.jpg' WHERE type_name = 'SUV';
UPDATE vehicle_types SET img_url = 'luxury.jpg' WHERE type_name = 'Luxury';
UPDATE vehicle_types SET img_url = 'van.jpg' WHERE type_name = 'Van';

D. Endpoint : Fetch Image (Direct Access)
Used by the browser/frontend to display the image.

Method: GET

URL Pattern: http://localhost:8080/images/{filename}

Example: http://localhost:8080/images/swift.jpg

E. Frontend Integration (Next.js)
Since the API only returns the filename, the frontend must construct the full URL.

Logic in vehicleService.js:

JavaScript

const SERVER_ROOT = "http://localhost:8080";

// When mapping the data:
let finalImageUrl = `${SERVER_ROOT}/images/${backendItem.imgUrl}`;
