package fiuba.tpp.reactorapp.entities;

public class Information {

    private boolean free;

    public Information() {
        this.free = false;
    }

    public Information(boolean free) {
        this.free = free;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
