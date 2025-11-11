enum SplitType {
    
    SPLIT_TYPE_EQUAL("EQUAL"),
    SPLIT_TYPE_EXACT("EXACT");
    
    private String splitType;
    
    SplitType(String splitType) {
        this.splitType = splitType;
    }

    String getString() {
        return splitType;
    }
}
