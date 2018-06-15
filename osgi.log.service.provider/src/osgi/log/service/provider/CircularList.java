package osgi.log.service.provider;

/**
 * Simple circular list implementation. Should probably make this more generic
 * and make it implement the List interface at some stage. Methods are
 * synchronized for concurrent access.
 */
public class CircularList
{
    /** Back array containing references to the list of objects */
    private Object[]    list;

    /** Maximum size of the list */
    private int         maxSize;

    /** Index to store next object */
    private int         next;

    /** true if the circular list has wrapped back to the beginning */
    private boolean     wrapped;

    // prevent instantiation without a size
    protected CircularList() {
        //default to prevent problems with empty list
        this(100);
    }

    /**
     * Constructs a circular list of the required capacity.
     *
     * @param size  required capacity of the list
     */
    public CircularList(int size) {
        if (size <= 0) {
            //default to prevent problems with empty list
            maxSize = 100;
        }
        else {
            this.maxSize = size;
        }

        this.list = new Object[this.maxSize];
        this.next = 0; 
        this.wrapped = false;
    }

    /**
     * Add a new object into the list.
     *
     * @param   a   object to add.
     * @return  copy of list in supplied array, or new array if insufficient 
     *          capacity
     */
    public void add(Object o) {
        if (o == null)  throw new IllegalArgumentException("null object reference");

        synchronized(this.list) {
            this.list[this.next++] = o;

            if (this.next >= this.maxSize) {
                this.next = 0;
                this.wrapped = true;
            }
        }
    }
    
    /**
     * Determine the current size of the list.
     *
     * @return  number of entries in the list.
     */
    public int size() {
        if (this.wrapped) {
            return this.maxSize;
        }
        else {
            return this.next;
        }
    }

    /**
     * Get a copy of the list as a new object array.
     *
     * @return  new array containing copy of the list
     */
    public Object[] toArray() {
        synchronized(this.list) {
            Object copy[] = new Object[this.size()];
            this.copyListTo(copy);
            return copy;
        }
    }

    /**
     * Get a copy of the list as an array of the specified type, using the
     * supplied array if it has sufficent capacity or creating a new one of
     * not
     *
     * @param   a   array of type required
     * @return  copy of list in supplied array, or new array if insufficient 
     *          capacity
     */
    public Object[] toArray(Object[] a) {
    	synchronized(this.list) {
    		int size = size();

    		if (a.length < size) {
    			a = (Object[])java.lang.reflect.Array.newInstance(
    					a.getClass().getComponentType(), size);
    		}

    		this.copyListTo(a);

    		return a;
    	}
    }

    /**
     * Utility method to copy the list into an array.
     *
     * @param a     array to receive copy of list.
     */
    protected void copyListTo(Object[] a) {
        synchronized(this.list) {
            if (!this.wrapped) {
                // log hasn't wrapped, just do simple copy
                System.arraycopy(this.list, 0, a, 0, this.next);
            }
            else {
                int len = this.maxSize-this.next;
                
                //first copy old entries (next and beyond)
                System.arraycopy(this.list, this.next, a, 0, len);
                
                //then copy newest entries (zero to next)
                System.arraycopy(this.list, 0, a, len, this.next);
            }
        }
    }
}
