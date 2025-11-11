package service.splitwise.request;

import service.splitwise.domain.PaidBy;
import service.splitwise.domain.PaidFor;
import service.splitwise.domain.SplitType;

public class SplitBillRequest {
    private PaidBy paidBy;
    private PaidFor paidFor;
    private SplitType splitType;
    private Double totalAmount;

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaidFor getPaidFor() {
        return paidFor;
    }

    public void setPaidFor(PaidFor paidFor) {
        this.paidFor = paidFor;
    }

    public PaidBy getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(PaidBy paidBy) {
        this.paidBy = paidBy;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }
}
