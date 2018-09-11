package com.stackroute.datamunger;

import java.util.*;


/*There are total 5 DataMungertest files:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class DataMunger {

	/*
	 * This method will split the query string based on space into an array of words
	 * and display it on console
	 */

	public String[] getSplitStrings(String queryString) {

	    queryString=queryString.toLowerCase();
	    String [] splitted = queryString.split(" ");
        return splitted;



	}

	/*
	 * Extract the name of the file from the query. File name can be found after a
	 * space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc.
	 * 
	 * Please consider this while extracting the file name in this method.
	 */

	public String getFileName(String queryString) {

	    queryString=queryString.toLowerCase();
	    String[] splitted=queryString.split("from");
	    String temp=splitted[1];
	    String[] result=temp.split(" ");
	    String answer = result[1];
	    return answer;



	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */
	
	public String getBaseQuery(String queryString) {

	    queryString=queryString.toLowerCase();
	    int index= -1;
	    index=queryString.indexOf("where");
	    if(index==-1){
	        index=queryString.indexOf("order by");
        }

        if (index==-1){
            index=queryString.indexOf("group by");
        }


        int i;
        String answer="";
        if(index!=-1) {
            answer=queryString.substring(0,index-1);
        }

        if(index!=-1){
            return answer;
        }
        else
        {
            return queryString;
        }



	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 * 
	 * Note: 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
	 * name can contain '*'
	 * 
	 */
	
	public String[] getFields(String queryString) {

		queryString=queryString.toLowerCase();
		String[] temp=queryString.split("from");
		//after splitting based on from we can remove the select from the string and give the rest,
		String[] result=temp[0].split(" ");
		String answer=result[1];
		String[] last=answer.split(",");
		return last;
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note:  1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */
	
	public String getConditionsPartQuery(String queryString) {

	    queryString=queryString.toLowerCase();
	    int index_where=-1;
	    int index_order=-1;
	    int index_group=-1;
	    String[] temp;
	    String condition ;
	    index_where=queryString.indexOf("where");

	    if(index_where==-1){
	        return null;
        }
        else {
            temp=queryString.split("where");
            index_order=temp[1].indexOf("order by");
            index_group=temp[1].indexOf("group by");
            if(index_order!=-1){

              condition=temp[1].substring(1,index_order);
            }
            else {
                if(index_group!=-1){
                    condition=temp[1].substring(1,index_group);
                }
                else{
                    condition=temp[1].substring(1);
                }
            }

            return condition;
        }

	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014","city ='bangalore'"]
	 * and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String[] getConditions(String queryString) {

	    String condition = getConditionsPartQuery(queryString);
	    if(condition==null){
	        return null;
        }
        else {


            condition = condition.trim();


            String[] result = condition.split(" and | or ", 0);
            return result;
        }
	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note:  1. AND/OR
	 * keyword will exist in the query only if where conditions exists and it
	 * contains multiple conditions. 2. AND/OR can exist as a substring in the
	 * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
	 * these as well when extracting the logical operators.
	 * 
	 */

	public String[] getLogicalOperators(String queryString) {

        String condition = getConditionsPartQuery(queryString);
        int i=0;
        if(condition==null){
            return null;
        }
        else{
              String[] result = condition.split(" ");
              int length =result.length;
              String[] temp = new String[length];
              for(String s:result){
                  if(s.equals("and")){
                      temp[i]="and";
                      i++;

                  }
                  else if(s.equals("or")){
                      temp[i]="or";
                      i++;
                  }

            }
            String[] answer = new String[i];

              for(i=0;i<answer.length;i++){
                  answer[i]=temp[i];

              }
              return answer;
        }


	}

	/*
	 * This method extracts the order by fields from the query string. Note: 
	 * 1. The query string can contain more than one order by fields. 2. The query
	 * string might not contain order by clause at all. 3. The field names,condition
	 * values might contain "order" as a substring. For eg:order_number,job_order
	 * Consider this while extracting the order by fields
	 */

	public String[] getOrderByFields(String queryString) {

        queryString = queryString.trim();
        queryString = queryString.toLowerCase();
        int order_index = -1;
        order_index = queryString.indexOf("order by");
        if (order_index != -1) {
            String answer = queryString.substring(order_index + 9, queryString.length());
            String[] result=answer.split(",");
            return result;
        }
        else {
            return null;
        }

    }

	/*
	 * This method extracts the group by fields from the query string. Note:
	 * 1. The query string can contain more than one group by fields. 2. The query
	 * string might not contain group by clause at all. 3. The field names,condition
	 * values might contain "group" as a substring. For eg: newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */

	public String[] getGroupByFields(String queryString) {
        queryString = queryString.trim();
        queryString = queryString.toLowerCase();
        String answer="";
        int index_group= -1;
        int index1= -1;
        index_group=queryString.indexOf("group by");
        index1=queryString.indexOf("order by");
        if(index_group!=-1) {
            answer=queryString.substring(index_group+9,queryString.length());
        }
        if(index1!=-1 && index_group!=-1) {
            answer=queryString.substring(index_group+9,index1-1);
        }
        String[] result = answer.split(",");
        if(index_group!=-1){
            return result;
        }
        else
        {
            return null;
        }
    }


	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
	 * followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */

	public String[] getAggregateFunctions(String queryString) {

        String[] splitStringArray=queryString.trim().split(" ",0);
        String[] fieldsList=splitStringArray[1].trim().split(",",0);
        String[] tempAggregatelist=new String[fieldsList.length];
        int count=0;
        for(int i=0;i<fieldsList.length;i++){
            if( (fieldsList[i].indexOf( "(" ) >0 ) && (fieldsList[i].indexOf( ")" ) >0)  ){
                tempAggregatelist[count]=fieldsList[i];
                count++;
            }
        }
        if(count == 0)
            return null;
        else{
            String[] aggregateList=new String[count];
            for(int i=0;i<count;i++)
                aggregateList[i]=tempAggregatelist[i];
            return aggregateList;
        }
    }

}