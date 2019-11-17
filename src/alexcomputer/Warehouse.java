package alexcomputer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class Warehouse {
    TreeMap<String,Integer> goodsList;
    ArrayList<Incoming> incomingList;
    static int incomingIndex;
    ArrayList<Outcoming> outcomingList;
    static int outcomingIndex;
    
    public Warehouse(){
        goodsList=new TreeMap<>();
        incomingList=new ArrayList();
        outcomingList=new ArrayList();
        incomingIndex=0;
        outcomingIndex=0;
    }
    //add goods type
    public void addGoods(Good newgood){
        if(newgood!=null)
        {
            if(goodsList.containsKey(newgood.getName()))
        {
            goodsList.put(newgood.getName(), goodsList.get(newgood.getName())+newgood.getNum());
        }
        else
            goodsList.put(newgood.getName(),newgood.getNum());}
    }
    
    //delete good type
    public void reduceGoods(Good newgood){
     if(goodsList.containsKey(newgood.getName()))
     {
        goodsList.put(newgood.getName(), goodsList.get(newgood.getName()) - newgood.getNum());
     }
     else
         System.out.print(newgood.getName()+"is out of stock!");
    }
    //入库
    public void addIncoming(Incoming entry){
        incomingList.add(entry);
        Iterator s = entry.getIncomingList().iterator();
        Good temp;
        while(s.hasNext())
        {
            temp = (Good)s.next();
            addGoods(temp);
        }
    }
    //出库
    public void addOutcoming(Outcoming entry){
        outcomingList.add(entry);
        Iterator s = entry.getOutcomingList().iterator();
        Good temp;
        while(s.hasNext())
        {
            temp = (Good)s.next();
            reduceGoods(temp);
        }
    }
    
    double calculateIncomingBalance(){
        double result=0;
        int tempIndex = incomingIndex;
        for(;tempIndex<incomingList.size();tempIndex++)
        {
            result += incomingList.get(tempIndex).getBill();
        }
        return result;
    }
    
    double calculateOutcomingBalance(){
        double result=0;
        int tempIndex = outcomingIndex;
        for(;tempIndex<outcomingList.size();tempIndex++)
        {
            result += outcomingList.get(tempIndex).getBill();
        }
        return result;
    }
    
    void payBalance(){
        for(;outcomingIndex<outcomingList.size();outcomingIndex++)
        {
            outcomingList.get(outcomingIndex).setPaid();
        }
        
        for(;incomingIndex<incomingList.size();incomingIndex++)
        {
            incomingList.get(incomingIndex).setPaid();
        }
    }
    
    double totalRevenue()
    {
        double result =0;
        for(int i=0;i<outcomingIndex;i++)
        {
            result += outcomingList.get(i).getBill();
        }
        
        for(int i=0;i<incomingIndex;i++)
        {
            result += incomingList.get(i).getBill();
        }
        return result;
    }
    
    double calculateTotalBalance()
    {
        return calculateOutcomingBalance()+calculateIncomingBalance();
    }
    
    //统计，列出所有商品及库存   
    public void inventory(){
        Iterator s = goodsList.keySet().iterator();
        System.out.println("Name\t\tBalance");
        System.out.println("-------------------------");
        String temp;
        while(s.hasNext()){
            temp = (String) s.next();
            System.out.println(temp+"\t\t"+goodsList.get(temp));
        }            
    }
    
    String[] goodslist(){
        String[] result = new String[goodsList.size()];
        goodsList.keySet().toArray(result);
        return result;
    }
    
    /*public void showIncomingList()
    {
        Iterator ss = incomingList.iterator();
        Incoming temp;
        while(ss.hasNext())
        {
            temp = (Incoming)ss.next();
            System.out.println(temp.getLable()+"\n"+
                    temp.getPost()+"\t"+temp.getTrackNo()+"\t"+temp.getDate());
            temp.getIncomingList()
        }
    }*/
    
    
       
    
}
