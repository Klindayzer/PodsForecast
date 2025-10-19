# XFashion Forecasting Service
##### _Predict Kubernetes pod requirements based on business activity data._

## 
## Overview
This service helps X Fashion’s DevOps team plan daily front-end and back-end pod capacity.
It reads historical and budgeted business metrics from Google Sheets such as GMV, users, and marketing cost and learns from past pod usage, and forecasts how many pods will be needed for upcoming budgeted days.
## 
## Key Features
- Reads data directly from Google Sheets
- Forecasts daily front-end & back-end pods using June as training data
- Considers GMV, users, marketing cost, and weekday patterns
- Ridge regression ensures stable predictions with limited training data
- REST API endpoint for DevOps consumption

## 
## Tech Stack
- Spring Boot (Kotlin)
-  Google Sheets API v4
-  Apache Commons Math3 (for regression)
-  Gradle (KTS)

## 
## Prerequisites
Credentials will be provided in a private email.

## 
## Running the App
Add credentials.json file and xfashion-sa.json file in resources folder.
Open XfashionForecastApplication class and run the main() methods.

## 
## API Endpoints
```sh
http://localhost:8080/api/v1/forecast/pods?startDate=2024-07-01&endDate=2024-07-31
```
