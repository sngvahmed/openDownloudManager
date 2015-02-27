package utills;

public enum Message {
	URL_ERROR("An error occured while parsing url or this url does not a file"),
	FILE_NOT_EXISTS("Wrong file path"),
	MULTI_PART_ERROR("May be one of file parts is corrupted");

	private String message;

	Message(String msg) {
		this.message = msg;
	}
	public String getMessage(){
		return message;
	}
	public void setMessage(String msg){
		message = msg;
	}
}
