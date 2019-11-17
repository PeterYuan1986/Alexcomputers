package alexcomputer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Incoming {
    String lable;
    String trackingNumber;
    String post;
    ArrayList<Good> incomingGoods;
    Date date;        
    int GoodsAmount;
    double bill;
    int paid;
    
    Incoming(String s)
    {
        lable =s;
        incomingGoods=new ArrayList();
        date = new Date();
        GoodsAmount =0;
        paid = 0;
    }
    
    
    void addGood(String s, int n)
    {
        Good entry = new Good(s, n);
        int flag =find(entry);
        if(flag==-1)
            incomingGoods.add(entry);
        else
            incomingGoods.get(flag).increase(n);
        GoodsAmount+=n;
    }
    
    void setDate()
    {
        date= Calendar.getInstance().getTime();
    }
    
    Date getDate()
    {
        return date;
    }
    
    double getBill()
    {
        return bill;
    }
     
    String getLable()
    {
        return lable;
    }     
        
    void setTrackNo(String s)
    {
        trackingNumber = s;
    }
     
    String getTrackNo()
    {
        return trackingNumber ;
    }      
    
    void setPost(String s)
    {
        post = s;
    }
     
    String getPost()
    {
        return post ;
    }  
    
    void setPaid()
    {
        paid = 1;
    }
     
    int getPaid()
    {
        return paid;
    }  
    
    ArrayList getIncomingList()
    {
        return incomingGoods;
    }
    
    void updateBill()
    {
        bill = 0.5* GoodsAmount;
    }
     
    int find(Good o)
     {
         int result =-1;
         for(int i =0; i<incomingGoods.size();i++)
         {
             if(incomingGoods.get(i).eaquals(o))
             {
                 result=i;
                 break;
             }             
         }
         return result;
     }
}
