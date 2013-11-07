package com.example.acelerometro;

/*
 |--------------------------------------------
 |  Tipos de movimientos variable type
 |--------------------------------------------
 | 0: Posicionado solo en x (horizontalmente)
 | 1: Posicionado solo en Y (verticalmente)
 | 2: Posicionado solo en Y (en profundidad)
 |
 |
 */



public class Movement {

    private float x,y,z;
    private long currentTime;
    private final double GRAVITY=9.8;
    private int type;
    private int prevType, maxMove, countMove;

    public Movement(float x, float y, float z, long currentTime, int type, int maxMove){
        this.x=x;
        this.y=y;
        this.z=z;
        this.currentTime= currentTime;
        this.type=type;
        this.prevType=-1;
        this.maxMove=maxMove;
        this.countMove=0;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getZ(){
        return this.z;
    }

    public long getCurrentTime(){
        return this.currentTime;
    }

    public int getType(){
        return this.type;
    }

    public int getPrevType(){
        return this.prevType;
    }

    public int getMaxMove(){
        return this.maxMove;
    }

    public int getCountMove(){
        return this.countMove;
    }

    public void setX(float x){
        this.x=x;
    }

    public void setY(float y){
        this.y=y;
    }

    public void setZ(float z){
        this.z=z;
    }

    public void setType(int type){
        this.type=type;
    }

    public void setPrevType(int prevType){
        this.prevType=prevType;
    }

    public void setCountMove(int countMove){
        this.countMove=countMove;
    }

    public void setCurrentTime(long currentTime){
        this.currentTime=currentTime;
    }

    public int isMovement(float x, float y, float z, long lastTime){


        if(!(this.getX()==x && this.getY()==y && this.getZ()==z)){
            switch (this.getType()){
                case 0:
                    if(y > 7 && y < 11 && x < 2 && x > -2){
                        if(this.deltaTime(lastTime)){
                            this.update(x, y, z, 1, lastTime);
                            return this.getActionY();
                        }

                    }
                    break;
                case 1:
                    if(x > 7 && y < 3 && y > -1){
                        if(this.deltaTime(lastTime)){
                            this.update(x, y, z, 0, lastTime);
                            return this.getActionX();
                        }
                    }
                    break;

                case 2:
                    break;
            }
        }


        return -1;
    }

    public void update(float x, float y, float z, int type, long lastTime){

        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setPrevType(this.getType());
        this.setType(type);
        this.setCountMove(this.getCountMove()+1);
        this.setCurrentTime(lastTime);

    }

    public int getActionX(){
        switch (this.getPrevType()){
            case -1:
                return -1;
            case 1:
                if(this.getCountMove()==this.getMaxMove()){
                    setCountMove(0);
                    return 0;
                }


                break;
            case 2:
                break;

        }
        return -1;
    }

    public int getActionY(){
        switch (this.getPrevType()){
            case -1:
                return -1;
            case 0:
                if(this.getCountMove()==this.getMaxMove()){
                    setCountMove(0);
                    return 10;
                }
                break;
            case 2:
                break;

        }
        return -1;
    }

    public int getActionZ(){
        switch (this.getPrevType()){
            case -1:
                return -1;
            case 0:
                break;
            case 1:
                break;

        }
        return -1;
    }

    public boolean deltaTime(long lastTime){

        //return (((lastTime-this.getCurrentTime())/50000000)>=1);
        return true;

    }


}
