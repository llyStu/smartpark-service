package com.vibe.parse;

public class Space extends BaseBean {
    private String parent;
    private String name;
    private String caption;
    private int seqence;


    public int getSeqence() {
        return seqence;
    }

    public void setSeqence(int seqence) {
        this.seqence = seqence;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "insert into t_space(id, parent, type, name, caption,seqence)values ("
                + getId() + "," + parent + ", \"3DSpace\", \"" + name + "\" , \"" + caption + "\"," + getSeqence() + ");";
    }

}
