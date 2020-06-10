package com.vibe.parse;

public class AtzgbService extends BaseService {

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "insert into t_service(id, parent, type, name, caption)values (" + getId() + "," + getParent()
                + ", \"AtzgbService\", \"" + getName() + "\" , \"" + getCaption()
                + "\");";
    }
}
