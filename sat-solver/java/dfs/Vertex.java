package dfs;

public class Vertex {
    private int id;
    private int index;
    private int lowLink;
    private boolean inStack;

    public int getID() {
        return this.id;
    }

    public int getIndex() {
        return this.index;
    }

    public int getLowLink() {
        return this.lowLink;
    }

    public boolean getInStack() {
        return this.inStack;
    }

    public void setID(int newID) {
        this.id = newID;
    }

    public void setIndex(int newIndex) {
        this.index = newIndex;
    }

    public void setLowLink(int newLowLink) {
        this.lowLink = newLowLink;
    }

    public void setInStack(boolean inStackStatus) {
        this.inStack = inStackStatus;
    }
}
