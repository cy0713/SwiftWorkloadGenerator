package cat.urv.data;

public enum DataTypes {
	
	DOCS("docs"), PICS("pics"), CODE("code"), MEDIA("media"), BINARY("binary"), COMPRESSED("compressed");
	
	private final String text;

    /**
     * @param text
     */
    private DataTypes(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
