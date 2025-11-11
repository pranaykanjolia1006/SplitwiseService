package service.splitwise.domain;

public enum SplitType {
    
    SPLIT_TYPE_EQUAL("EQUAL"),
    SPLIT_TYPE_EXACT("EXACT");
    
    private final String splitType;
    
    SplitType(String splitType) {
        this.splitType = splitType;
    }

    String getString() {
        return splitType;
    }
}
