package osgi.data.service.interfaces;

public interface DataService {
	/**
     * @param id
     * The id of the requested data value.
     * @return The data value for the given id.
     */
    String getData(int id);
}