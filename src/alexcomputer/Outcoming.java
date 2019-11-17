
package alexcomputer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Outcoming {
    String lable;
    String post;
    String trackingNumber;
    ArrayList<Good> outcomingGoods;
    Date date;    
    boolean international; 
    int GoodsAmount;
    double  bill;
    int paid;
    
    Outcoming(String s)
    {
        lable =s;
        outcomingGoods=new ArrayList();
        date = new Date();
        GoodsAmount=0;
        international =false;
        bill=0;
        paid=0;
    }        
    
    void addGood(String s, int n)
    {
        Good entry = new Good(s, n);
        int flag =find(entry);
        if(flag==-1)
            outcomingGoods.add(entry);
        else
            outcomingGoods.get(flag).increase(n);
        GoodsAmount+=n;
    }
    
    void updateBill()
    {
        bill=  international||GoodsAmount>=3? 4:2.5;
    }
    
    double getBill()
    {
        return bill;
    }
    
    void setDate()
    {
        date= Calendar.getInstance().getTime();
    }
    
    Date getDate()
    {
        return date;
    }
    
    void setTrackNo(String s)
    {
        trackingNumber = s;
    }   
    
    void setInternational()
    {
        international=true;
    }
    
    boolean  getInternational()
    {
        return international;
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
    
    String getTrackNo()
    {
        return trackingNumber ;
    }
    
    ArrayList getOutcomingList()
    {
        return outcomingGoods;
    }
    
    int find(Good o)
     {
         int result =-1;
         for(int i =0; i<outcomingGoods.size();i++)
         {
             if(outcomingGoods.get(i).eaquals(o))
             {
                 result=i;
                 break;
             }             
         }
         return result;
     }
}
