This is a first draft of android app for  DB manangement class, as I designed a simple bank managment system for checking users' balance , credit status and transaction with fiiltering function.
## List of Tables

- **Customer**: Members of the bank who have opened accounts, loans, and/or credit cards.  
- **Loan**: Contains all information on loans taken out by customers.  
- **Account**: Contains all information on deposit accounts owned by customers.  
- **Transaction**: Contains all information on transactions made against accounts, whether that be withdrawals, deposits, or transfers.  
- **Credit Card**: Contains all information on credit cards opened by customers.  
- **Credit Card Transaction**: Contains all information on transactions performed by customers using their credit cards.  

## Entity Relationship Diagram
## Data Dictionary

| Data Field              | Data Description                                             | Data Type         |
|-------------------------|-------------------------------------------------------------|-------------------|
| Customer_ID            | Unique ID for the Customer                                  | CHAR(8)          |
| First_Name             | Customer’s First Name                                       | NVARCHAR(15)     |
| Last_Name              | Customer’s Last Name                                        | NVARCHAR(15)     |
| Date_of_Birth          | Customer’s Date of Birth                                    | Date/Time        |
| Address                | Customer’s Physical Address                                 | NVARCHAR(35)     |
| Email                  | Customer’s Email Address                                   | NVARCHAR(35)     |
| Phone_Number           | Customer’s Phone Number                                    | NVARCHAR(15)     |
| Account_ID             | Unique ID for the account                                  | CHAR(8)          |
| Account_Type           | Type of account (Checking, Savings)                        | NVARCHAR(15)     |
| Balance               | Current balance of the Account                             | DECIMAL(9, 2)    |
| Open_Date              | Date the account was opened                                | Date/Time        |
| Card_ID                | Unique ID of the Credit Card                              | CHAR(8)          |
| Card_Limit             | Credit limit of the card                                  | DECIMAL(6, 2)    |
| Balance                | Outstanding balance on the card                           | DECIMAL(6, 2)    |
| Issue_Date             | Date the card was issued                                  | Date/Time        |
| Expiry_Date            | Date the card expires                                     | Date/Time        |
| Card_Status            | The current status of the card (Active, Expired, Frozen) | NVARCHAR(15)     |
| CC_Transaction_ID      | Unique ID of the credit card transaction                  | CHAR(8)          |
| CC_Transaction_Date    | Date the transaction occurred                            | Date/Time        |
| CC_Transaction_Type    | Type of transaction (Purchase, Payment, Interest Charge) | NVARCHAR(15)     |
| CC_Amount              | Total amount of the purchase                             | DECIMAL(6, 2)    |
| CC_Interest_Rate       | Interest Rate on the Credit Card                         | DECIMAL(2, 6)    |
| Merchant_Details       | Information about the merchant (Name, Location)         | NVARCHAR(35)     |
| Description            | Description of the transaction                           | NVARCHAR(35)     |
| Transaction_ID         | Unique ID for the account transaction                    | CHAR(8)          |
| Transaction_Date       | Date the transaction occurred                            | Date/Time        |
| Transaction_Type       | Type of transaction (Deposit, Withdrawal, Transfer)     | NVARCHAR(15)     |
| From_Account_ID        | In case of transfer, the account money is coming from   | CHAR(8)          |
| To_Account_ID          | In case of transfer, the account money is going to      | CHAR(8)          |
| Amount                | Amount of the transaction                                | DECIMAL(6, 2)    |
| Loan_ID               | Unique ID of the loan                                   | CHAR(8)          |
| Loan_Amount           | Amount the loan is for                                  | DECIMAL(8, 2)    |
| Interest_Rate         | Interest Rate on the loan                              | DECIMAL(2, 6)    |
| Start_Date            | Date the loan began                                    | Date/Time        |
| End_Date              | Date the loan is scheduled to be paid off              | Date/Time        |



