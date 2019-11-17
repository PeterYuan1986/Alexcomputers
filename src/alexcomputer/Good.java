package alexcomputer;

class Good {
    private String name;
    private int number;
    
   public Good(String n){
       name=n;
       number=0;
   }
   
    public Good(String n, int s){
       name=n;
       number=s;
   }
   
   public void changeName(String name){
       this.name= name;       
   }
    
   public String getName()
   {
       return name;
   }
   
   public void setNum(int count){
       number= count;
   }
   
   public int getNum(){       
       return number;
   }   
   
   public boolean eaquals(Good o){
       return name.equals(o.getName());
   }

    void increase(int n) {
        number = number+n;
    }
   
}
