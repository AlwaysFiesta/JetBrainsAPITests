# JetBrains Licenses API Test Suite

Suite of tests for JetBrains' licenses public API as my homework. 

## Getting Started

Tests are taking API auth details from environment variables. 

**Initial setup**: 

1. **ApiTest_Team1** with available licenses to assign.
    * Available Expired license
    * Available Active license
    * Unavailable Expired License
    * Unavailable Active License
    * Enable API for Org Admin -> put to env variable X_API_KEY_ORG_ADMIN
    * Add Viewer to Team
    * Enable API for Viewer -> put to env variable X_API_KEY_VIEWER
   
   

2. **ApiTest_Team2** with another available license to assign:
    * Add Team Admin to Team.
    * Enable API for Team Admin -> put to env variable X_API_KEY_TEAM_ADMIN

 
3. Put organisation ID to env variables X_CUSTOMER_CODE


### Prerequisites

- Java 17
- Maven