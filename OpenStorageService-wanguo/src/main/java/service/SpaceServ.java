package service;

import model.Space;

/**
 * Created by Tyfunwang on 2015/1/31.
 */
public class SpaceServ {
    private static SpaceServ spaceServ = new SpaceServ();

    private SpaceServ() {

    }

    public static SpaceServ me() {
        return spaceServ;
    }

    private Space space = new Space();


    //open a new space
    public boolean addSpace(String email) {
        return space.addSpace(email);
    }


    //check the sapce
    public boolean isHas(String email) {
        return !space.isHas(email).isEmpty();
    }

    //get current User`s space info
    public Space getSp(String email) {
        return space.findFirst("select * from space where " + Space.USEREMAIL + " = '" + email + "'");
    }

    //upload file
    public boolean reduce(long size, String email) {
        return space.reduce(size, email);
    }
    //shared file move  to list
    public boolean moveReduce(float size, String email){
        return space.moveReduce(size,email);
    }
    //delete file
    public boolean updateSp(long size, String email) {
        return space.updateSp(size, email);
    }

    //check
    public boolean checkSp(float size, String email) {
        Space space1 = space.checkSp(email);
        float all = space1.getFloat(Space.ALLSPACE) ;
        float used = space1.getFloat(Space.USEDSPACE);
        if ((all - used) > size) {
            return true;
        }
        return false;
    }
}
