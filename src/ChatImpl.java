import java.util.ArrayList;

public class ChatImpl extends talk.chatPOA{
	
	private ArrayList<String> memberList;
	
	ChatImpl(){
		this.memberList = null;
	}
	
	@Override
	public void diffuserMessage(String message, talk.client t) {
		int i;
		
		for(i = 0; i < memberList.size(); i++) {
			
		}
	}

	@Override
	public void enregistrer(String idObj) {
		this.memberList.add(idObj);
	}

}
