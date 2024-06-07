# JetBrains Licenses API Test Suite

Suite of tests for JetBrains' licenses public API as my homework. 

## Getting Started

Tests are taking API auth details from environment variables. 

**Initial setup**: 

1. Team1 with available license to assign.
    * Enable API for Org Admin.
    * Add Viewer to Team
    * Enable API for Viewer
   

2. Team2 with another available license to assign:
    * Add Team Admin to Team.
    * Enable API for Team Admin.

 
3. Put tokens for users and you organisation ID to env variables in your system:

   X_API_KEY_ORG_ADMIN

   X_API_KEY_TEAM_ADMIN
   
   X_API_KEY_VIEWER

   X_CUSTOMER_CODE


### Prerequisites

- Java 17
- Maven