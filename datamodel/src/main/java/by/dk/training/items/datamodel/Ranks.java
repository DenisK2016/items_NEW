package by.dk.training.items.datamodel;

public enum Ranks {
	STATE_ADVISER_FIRST("государственный советник таможенной службы I ранга"), 
	STATE_ADVISER_SECOND("государственный советник таможенной службы II ранга"), 
	STATE_ADVISER_THIRD("государственный советник таможенной службы III ранга"), 
	ADVISER_FIRST("советник таможенной службы I ранга"), 
	ADVISER_SECOND("советник таможенной службы II ранга"), 
	ADVISER_THIRD("советник таможенной службы III ранга"), 
	INSPECTOR_FIRST("инспектор таможенной службы I ранга"), 
	INSPECTOR_SECOND("инспектор таможенной службы II ранга"), 
	INSPECTOR_THIRD("инспектор таможенной службы III ранга"),
	INSPECTOR_FOURTH("инспектор таможенной службы IV ранга");
	
	private String rank;
	
	private Ranks(String rank){
		this.rank=rank;
	}

	public String getRank() {
		return rank;
	}

}
