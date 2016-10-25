// Event class as POJO
public class Event {

	// Represents id
	private int id;
	// Represents count
	private int count = 0;
	// Left child
	private Event left = null;
	// Ptr to righ child
	private Event right = null;
	// Ptr to parent
	private Event parent;
	// Color property
	private char color;

	// default constructor
	public Event() {

	}

	public Event(int id, int count) {
		this.id = id;
		this.count = count;
		this.color = 'r';
	}

	// Copy Constructor
	public static void copy(Event e1, Event e2) {
		e2.count = e1.count;
		e2.id = e1.id;
	}

	public char getColor() {
		return color;
	}

	public void setColor(char color) {
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int event) {
		this.count = event;
	}

	public Event getLeft() {
		return left;
	}

	public void setLeft(Event left) {
		this.left = left;
	}

	public Event getRight() {
		return right;
	}

	public void setRight(Event right) {
		this.right = right;
	}

	public Event getParent() {
		return parent;
	}

	public void setParent(Event parent) {
		this.parent = parent;
	}

	public int increase(int increaseAmnt) {
		this.count += increaseAmnt;
		return getCount();
	}

	public int reduce(int decreaseAmnt) {
		this.count -= decreaseAmnt;
		return getCount() > 0 ? getCount() : 0;
	}

}
