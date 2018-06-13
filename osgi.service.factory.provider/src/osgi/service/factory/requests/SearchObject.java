package osgi.service.factory.requests;

public class SearchObject extends com.uc4.communication.requests.SearchObject {
//	private com.uc4.communication.requests.SearchObject search;
	
//	public com.uc4.communication.requests.SearchObject getSearch() {
//		return search;
//	}

//	public void setSearch(com.uc4.communication.requests.SearchObject search) {
//		this.search = search;
//	}

	public SearchObject(String filter) {
//		this.search = new com.uc4.communication.requests.SearchObject();
		
//		search.selectAllObjectTypes();
		this.setTypeJOBS(true);
		this.setSearchLocation("/", true);
		this.setName(filter);
		
//		search.setTextSearch(args[0], false, true, false, false);
		
//		search.setSearchUseOfObjects(true);
	}
}