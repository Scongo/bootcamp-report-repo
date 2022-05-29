# bootcamp-report-repo

**Requirements** 
* Basic understanding of java https://www.tutorialspoint.com/java/
* Basic understanding of git https://www.atlassian.com/git/tutorials/learn-git-with-bitbucket-cloud
* Basic understanding of mysql https://www.tutorialspoint.com/mysql/
* Basic understanding of Maven https://maven.apache.org/guides/getting-started/

 


**Console Calculator**
 
Build a calculator that has the basic functionality of a calculator. The java application should be runnable from the command line(main method) and prompt you for inputs.  When the application starts it should prompt you for a value. After you enter a value the program should ask you which operation you would like to perform and ask for a second value. After the user enter the second value the system should output the result and prompt the user to try again or quit. You need to cater for non numeric values and general validation (eg you cannot divide by zero).  At any stage if the user types in “exit” your application should quit.
 
Possible Operations
* Plus (+)
* Minus (-)
* Devide (/)
* Multiply (*)
 
Below is an example of how the console output should look like when you run the application.
 
```
Enter value: {user enter value}
Enter Operation: {user enters operation}
Enter value: {user enter value}
Result: {system outputs result}
Do you want to start over?(y/n): {user enter y or n}
```
 




**Hibernate Report**
 
Requirements
You must use hibernate entity management and hql to build your report.
Good place to start would be https://docs.jboss.org/hibernate/orm/3.3/reference/en/html/tutorial.html
 
We need to build a report and output the content to the console in a predefined format. You will be provided with a json object that needs to be parsed and inserted into a database table. Your database needs to look like the ER diagram provided.
 
Generate 2 reports. One to return all orders that have been paid and another to find all orders that has no payment attempted.
 
Example Output
```
##################
{Report name}
##################
ORDER_ID, DESC, AMOUNT, NUMBER_OF_ITEMS, NUMBER_OF_PAYMENTS, PAYMENT_STATUS, AMOUNT
 
```





**Rabbit Publisher and consumer**
 
Requirements
Basic understanding of rabbitMq https://www.rabbitmq.com/tutorials/tutorial-one-java.html and mavne dependencies https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html
 

This following section requires you to understand and complete the previous section first.  Using the database in the previous section,  build a console application that allows you to do a payment on one of the orders that does not have a successful payment.  This console should ask you to enter the required details to complete a payment. (you can dummy out the payment process part).  You then need to publish this data onto a rabbit queue and consume this message and insert a payment record for that specific order.
 

You will notice that only the rabbit dependency is included in the pom.xml.  Its up to you to find out what dependencies you will need to include to be able to do this question (hint: see previous project for more details).
