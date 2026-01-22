Hub Master Upload Documentation
This module allows administrators to bulk upload Hub (Location) data using an Excel file (.xlsx). The system parses the file, checks for duplicates, and saves the data to the hub_master database table.
1. Excel File Format
File Name: hubs.xlsx (or any name) Format: .xlsx (Excel Workbook) Data Structure: The data must start from the first row (Header) and the first column (Column A).
⚠️ Important Rules:

No Empty Columns: Ensure "Hub Name" is in Column A. Do not leave Column A blank.

Valid IDs: The City ID and State ID must already exist in your database tables (city_master, state_master).

2. Backend Logic (How it works)
Endpoint: POST /api/locations/upload

1. Repository Layer (HubMasterRepository.java)
Change: Added a method to find existing hubs by name and city. We return a List to prevent crashes if duplicates already exist in the database.
// Returns a List<HubMaster> to safely handle cases where duplicates might already exist.
    List<HubMaster> findByHubNameAndCity_Id(String hubName, Long cityId);

2. Service Layer (HubService.java)
Change: Added the saveHubsFromExcel method. It now contains "Upsert Logic": it looks for an existing hub first. If found, it updates it; if not, it creates a new one.

3. Controller Layer (HubController.java)
Change: Added the POST endpoint to accept the file and trigger the service.

4. Security Config (SecurityConfig.java)
Change: Allowed access to the /api/locations/** endpoints so Postman doesn't get a 403 Forbidden error.

3. Postman Testing Instructions
Follow these steps to upload data using Postman.

Step 1: Configure Request
Method: Select POST.

URL: http://localhost:8080/api/locations/upload

Step 2: Set Body (The File)
Click the Body tab.

Select form-data.

Add a new Key-Value pair:

Key: Type file.

Action: Hover over the right side of the Key cell. Change the dropdown from Text to File.

Value: Click Select Files and choose your hubs.xlsx file.

Step 3: Send & Verify
Click Send.

Expected Response (Status 200 OK):
