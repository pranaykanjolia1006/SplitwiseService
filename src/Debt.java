public class Debt {

    private User owesTo; // gives the money
    private User owedTo; // get the money
    private float amount;

    public Debt(User owesTo, User owedTo, float amount) {
        this.owesTo = owesTo;
        this.owedTo = owedTo;
        this.amount = amount;
    }
    public User getOwesTo() {
        return owesTo;
    }
    public void setOwesTo(User owesTo) {
        this.owesTo = owesTo;
    }
    public User getOwedTo() {
        return owedTo;
    }
    public void setOwedTo(User owedTo) {
        this.owedTo = owedTo;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }

}
