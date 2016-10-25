
public class RBTree {

	// Constants
	final public static char RED = 'r';
	final public static char BLACK = 'b';

	// height of RB Tree
	int deepestLevel = 0;
	private int input[][];

	// Root of RB Tree
	public Event root;

	/**
	 * Takes array as input and builds tree from the array.
	 * 
	 * @param arr
	 */
	public void initilize(int arr[][]) {
		this.input = arr;
		arr = null;
		// Math function to calculate the lowest level so that they can be set
		// to red color.
		deepestLevel = 1 + (int) (Math.log(input.length) / Math.log(2));
		root = buildTree(0, input.length - 1, 1);
		input = null;
	}

	/**
	 * Build Tree takes median of array and sets it as root left part will be
	 * recursively called to create left sub tree and similarly right part is
	 * processed.
	 * 
	 * @param start
	 * @param end
	 * @param depth
	 * @return
	 */
	public Event buildTree(int start, int end, int depth) {
		if (start > end) {
			return null;
		}
		int mid = (start + end) / 2;
		Event root = new Event(input[mid][0], input[mid][1]);
		Event left = buildTree(start, mid - 1, depth + 1);
		Event right = buildTree(mid + 1, end, depth + 1);
		root.setLeft(left);
		root.setRight(right);
		root.setColor(BLACK);
		if (left != null) {
			left.setParent(root);
		}
		if (right != null) {
			right.setParent(root);
		}
		// Set color to red for lowest level nodes to maintain
		// black height property
		if (left == null && right == null && depth == deepestLevel) {
			root.setColor(RED);
		}
		return root;
	}

	// Calls insert for theId, if node is present increment by m
	public int increase(int theId, int m) {
		Event curr = new Event(theId, m);
		return insert(curr);
	}

	/**
	 * Finds the element if present reduces by m if count goes below 0, calls
	 * delete returns final count
	 * 
	 * @param theId
	 * @param m
	 * @return
	 */
	public int reduce(int theId, int m) {
		Event curr = count(theId);
		if (null == curr) {
			return 0;
		}
		curr.reduce(m);
		if (curr.getCount() <= 0) {
			delete(curr.getId());
			return 0;
		}
		return curr.getCount();
	}

	/**
	 * BST Search for the event if found call increase function else insert in
	 * tree
	 * 
	 * @param e
	 * @return
	 */
	public int insert(Event e) {
		int id = e.getId();
		if (root == null) {
			root = e;
			e.setColor(BLACK);
			return e.getCount();
		}
		Event ptr = root;
		// Loops to find the node or position where to insert
		while (true) {
			if (ptr.getId() == id) {
				ptr.increase(e.getCount());
				return ptr.getCount();
			} else if (ptr.getId() > id) {
				if (ptr.getLeft() == null) {
					ptr.setLeft(e);
					e.setParent(ptr);
					recoloring(e);
					return e.getCount();
				} else {
					ptr = ptr.getLeft();
				}
			} else if (ptr.getId() < id) {
				if (ptr.getRight() == null) {
					ptr.setRight(e);
					e.setParent(ptr);
					recoloring(e);
					return e.getCount();
				} else {
					ptr = ptr.getRight();
				}
			}
		}
	}

	/**
	 * Gives the sibling of node passed as parameter
	 * 
	 * @param e
	 * @return
	 */
	public Event sibling(Event e) {
		Event parent = e.getParent();
		if (parent == null) {
			return null;
		}
		return (parent.getLeft() == e) ? parent.getRight() : parent.getLeft();
	}

	/**
	 * Gives sibling of parent node
	 * 
	 * @param e
	 * @return
	 */
	public Event uncle(Event e) {
		Event parent = e.getParent();
		return sibling(parent);
	}

	/**
	 * Calls after insert is done. Balances the tree by color flip or rotation
	 * 
	 * @param curr:
	 *            Pointer to the newly inserted node
	 */
	private void recoloring(Event curr) {

		// If event is root
		if (root == curr) {
			curr.setColor(BLACK);
			return;
		}

		// Find Uncle of this node
		Event uncle = this.uncle(curr);
		// If uncle is null, assign black color
		char uncleColor = (null != uncle) ? uncle.getColor() : BLACK;
		Event parent = curr.getParent();

		if (parent.getColor() == BLACK) {
			// DONE !!
		} else if (parent.getColor() == RED && uncleColor == RED) {
			// Case when both parent and uncle are red
			// flip their colors and make grandparent as red
			parent.setColor(BLACK);
			uncle.setColor(BLACK);
			parent.getParent().setColor(RED);
			recoloring(parent.getParent());
		} else if (parent.getColor() == RED && uncleColor == BLACK) {

			// Grand Parent
			Event gp = parent.getParent();

			// LL Case
			if (parent.getLeft() == curr && gp.getLeft() == parent) {
				RightRotation(gp);
				gp.setColor(RED);
				parent.setColor(BLACK);
				curr.setColor(RED);
			} // LR Case
			else if (parent.getRight() == curr && gp.getLeft() == parent) {
				LeftRotation(parent);
				RightRotation(gp);
				gp.setColor(RED);
				parent.setColor(RED);
				curr.setColor(BLACK);
			} // RR Case
			else if (parent.getRight() == curr && gp.getRight() == parent) {
				LeftRotation(gp);
				gp.setColor(RED);
				parent.setColor(BLACK);
				curr.setColor(RED);
			} // LR Case
			else if (parent.getLeft() == curr && gp.getRight() == parent) {
				RightRotation(parent);
				LeftRotation(gp);
				gp.setColor(RED);
				parent.setColor(RED);
				curr.setColor(BLACK);
			}
		}
	}

	/**
	 * Right Rotation when LL Case occurs
	 * 
	 * @param gp
	 */
	public void RightRotation(Event gp) {
		// Grand parent and parents
		Event ggp = gp.getParent();
		Event parent = gp.getLeft();

		if (ggp == null) {
			root = parent;
		} else if (ggp.getLeft() == gp) {
			ggp.setLeft(parent);
		} else {
			ggp.setRight(parent);
		}

		gp.setLeft(parent.getRight());
		if (gp.getLeft() != null) {
			gp.getLeft().setParent(gp);
		}
		parent.setRight(gp);

		parent.setParent(gp.getParent());
		gp.setParent(parent);
	}

	/**
	 * LeftRotation RR-Case
	 * 
	 * @param gp
	 */
	public void LeftRotation(Event gp) {
		Event ggp = gp.getParent();
		Event parent = gp.getRight();

		if (ggp == null) {
			root = parent;
		} else if (ggp.getRight() == gp) {
			ggp.setRight(parent);
		} else {
			ggp.setLeft(parent);
		}

		gp.setRight(parent.getLeft());
		if (gp.getRight() != null) {
			gp.getRight().setParent(gp);
		}
		parent.setLeft(gp);

		parent.setParent(gp.getParent());
		gp.setParent(parent);
	}

	/**
	 * Returns count for the given id if flag is true and id is not present in
	 * tree, then return the parent of last external node if flag is false,
	 * return null if id is not present in the tree
	 * 
	 * @param id
	 * @param flag
	 * @return
	 */
	public Event count(int id, boolean... flag) {
		if (root == null) {
			return null;
		}
		Event ptr = root;
		while (true) {
			if (ptr.getId() == id) {
				return ptr;
			} else if (ptr.getId() > id) {
				if (ptr.getLeft() == null) {
					if (flag.length > 0 && flag[0])
						return ptr;
					else
						return null;
				} else {
					ptr = ptr.getLeft();
				}
			} else if (ptr.getId() < id) {
				if (ptr.getRight() == null) {
					if (flag.length > 0 && flag[0])
						return ptr;
					else
						return null;
				} else {
					ptr = ptr.getRight();
				}
			}
		}
	}

	/**
	 * Is called to delete element
	 * 
	 * @param theId
	 */
	public void delete(int theId) {
		Event curr = count(theId);
		Event succ = successor(curr);
		// Swap with successor so that removed element has either degree 0 or 1
		if (succ != null) {
			curr = swapWithSuccessor(curr, succ);
		}
		// call remove
		RemoveElement(curr, true);
	}

	/**
	 * Balances the tree before delete with rotations and color flip
	 * 
	 * @param curr
	 * @param rflag:
	 *            Parameter decides if curr event needs to be detached/delete or
	 *            it is recursive call and needs to rebalance only
	 */
	private void RemoveElement(Event curr, boolean rflag) {
		// Base case : Curr element is root
		if (curr == root && !rflag) {
			curr.setColor(BLACK);
			return;
		} else if (curr == root && rflag) {
			root = curr.getLeft();
			if (root != null)
				root.setColor(BLACK);
			return;
		}

		// Base case: right and left are null, curr is red node
		if (curr.getRight() == null && curr.getLeft() == null && curr.getColor() == RED && rflag) {
			detach(curr, true);
		} else if ((curr.getLeft() != null && curr.getColor() == RED && curr.getLeft().getColor() == BLACK && rflag)
				|| (curr.getLeft() != null && curr.getColor() == BLACK && curr.getLeft().getColor() == RED && rflag)) {
			// case: one of parent or left node is red node and other is black.
			curr.getLeft().setColor(BLACK);
			detach(curr, rflag);
		} else if ((curr.getRight() != null && curr.getColor() == RED && curr.getRight().getColor() == BLACK && rflag)
				|| (curr.getRight() != null && curr.getColor() == BLACK && curr.getRight().getColor() == RED
						&& rflag)) {
			// case: one of parent or right node is red node and other is black.
			curr.getRight().setColor(BLACK);
			detach(curr, rflag);
		} else if (isBlack(curr.getLeft()) && isBlack(curr.getRight()) || !rflag) {
			// when both children are black
			Event s = sibling(curr);
			// check if any sibling is red
			boolean isSibChildRed = isRed(s.getLeft()) || isRed(s.getRight());
			// if one of sibling is red
			if (isBlack(s) && isSibChildRed) {
				// curr is left child, right child of sibling is red,
				// Left rotation
				if (IsLeftChild(curr) && isRed(s.getRight())) {
					s.setColor(curr.getParent().getColor());
					curr.getParent().setColor(BLACK);
					if (s.getRight() != null)
						s.getRight().setColor(BLACK);
					LeftRotation(curr.getParent());
					detach(curr, rflag);
				} else if (IsLeftChild(curr) && isRed(s.getLeft())) {
					// curr is left child, only left child of sibling is red,
					// Right and Left rotation: RL Case
					if (s.getLeft() != null)
						s.getLeft().setColor(BLACK);
					s.setColor(RED);
					RightRotation(s);

					s = curr.getParent().getRight();
					s.setColor(curr.getParent().getColor());
					curr.getParent().setColor(BLACK);
					if (s.getRight() != null)
						s.getRight().setColor(BLACK);

					LeftRotation(curr.getParent());
					detach(curr, rflag);
				} else if (IsRightChild(curr) && isRed(s.getLeft())) {
					// curr is right child, left child of sibling is red,
					// Right rotation: LL Case
					s.setColor(curr.getParent().getColor());
					curr.getParent().setColor(BLACK);
					if (s.getLeft() != null)
						s.getLeft().setColor(BLACK);
					RightRotation(curr.getParent());
					detach(curr, rflag);
				} else if (IsRightChild(curr) && isRed(s.getRight())) {
					// Left and Right rotation: LR Case
					s.getRight().setColor(BLACK);
					s.setColor(RED);
					LeftRotation(s);
					s = curr.getParent().getLeft();

					s.setColor(curr.getParent().getColor());
					curr.getParent().setColor(BLACK);
					if (s.getLeft() != null)
						s.getLeft().setColor(BLACK);
					RightRotation(curr.getParent());
					detach(curr, rflag);
				}
			} else if (isBlack(s) && !isSibChildRed) {
				// Sibling is black and none of its child are red
				// set the sibling to red and make the parent as curr node
				// Call remove element on parent, with rflag as false
				// If parent is red, make it black and stop
				s.setColor(RED);
				if (isBlack(curr.getParent())) {
					RemoveElement(curr.getParent(), false);
				} else {
					curr.getParent().setColor(BLACK);
				}
				detach(curr, rflag);
			} else if (isRed(s)) {
				// If sibling is red, make parent red and do either left or
				// right rotation
				// Recursively call remove element till curr reaches root
				curr.getParent().setColor(RED);
				s.setColor(BLACK);
				if (IsLeftChild(curr)) {
					LeftRotation(curr.getParent());
				} else {
					RightRotation(curr.getParent());
				}
				RemoveElement(curr, false);
				detach(curr, rflag);
			}
		}
	}

	/**
	 * Is called from remove element Maintains pointer after node is detached
	 * from the root
	 * 
	 * @param e
	 * @param rflag
	 */
	private void detach(Event e, boolean rflag) {
		if (!rflag) {
			return;
		}
		if (e.getLeft() == null && e.getRight() == null) {
			if (IsLeftChild(e)) {
				e.getParent().setLeft(null);
			} else {
				e.getParent().setRight(null);
			}
			e.setParent(null);
		} else if (IsLeftChild(e) && e.getLeft() != null) {
			e.getParent().setLeft(e.getLeft());
			e.getLeft().setParent(e.getParent());
		} else if (IsLeftChild(e) && e.getRight() != null) {
			e.getParent().setLeft(e.getRight());
			e.getRight().setParent(e.getParent());
		} else if (IsRightChild(e) && e.getLeft() != null) {
			e.getParent().setRight(e.getLeft());
			e.getLeft().setParent(e.getParent());
		} else if (IsRightChild(e) && e.getRight() != null) {
			e.getParent().setRight(e.getRight());
			e.getRight().setParent(e.getParent());
		}
		e.setParent(null);
	}

	/**
	 * Utility function which returns true if node's color is black or node is
	 * null
	 * 
	 * @param curr
	 * @return
	 */
	private boolean isBlack(Event curr) {
		if (curr == null || curr.getColor() == BLACK) {
			return true;
		}
		return false;
	}

	/**
	 * If node is red, returns true
	 * 
	 * @param curr
	 * @return
	 */
	private boolean isRed(Event curr) {
		return !isBlack(curr);
	}

	// Swap data with the successor, so it can be removed from bottom of tree
	private Event swapWithSuccessor(Event curr, Event succ) {
		Event temp = new Event();
		Event.copy(curr, temp);
		Event.copy(succ, curr);
		Event.copy(temp, succ);
		return succ;
	}

	/**
	 * Returns true if current node is left child of parent
	 * 
	 * @param curr
	 * @return
	 */
	private boolean IsLeftChild(Event curr) {
		if (curr != root && curr.getParent().getLeft() == curr) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if current node is right child of parent
	 * 
	 * @param e
	 * @return
	 */
	private boolean IsRightChild(Event e) {
		return !IsLeftChild(e);
	}

	/**
	 * Returns successor from the right subtree
	 * 
	 * @param temp
	 * @return
	 */
	public Event successor(Event temp) {
		if (temp.getRight() == null) {
			return null;
		}
		Event curr = temp.getRight();
		while (curr.getLeft() != null) {
			curr = curr.getLeft();
		}
		return curr;
	}

	/**
	 * Returns next element bigger than passed id if element present return
	 * successor if not present traverse back from leaf to root and stops when
	 * node is bigger
	 * 
	 * @param id
	 * @return
	 */
	public Event next(int id) {
		Event loc = count(id, true);
		Event succ;
		if (loc.getId() == id) {
			succ = successor(loc);
			if (succ != null) {
				return succ;
			}
		}
		while (loc.getId() <= id) {
			loc = loc.getParent();
			if (loc == null) {
				return new Event(0, 0);
			}
		}
		return loc;
	}

	/**
	 * Returns element smaller than the id. looks if id is present in tree, if
	 * finds predecessor in its left subtree if is is not present, trace back
	 * from leaf node to parent and compare with parent first parent with
	 * smaller id
	 * 
	 * @param id
	 * @return
	 */
	public Event previous(int id) {
		Event loc = count(id, true);
		if (loc.getId() == id && loc.getLeft() != null) {
			loc = loc.getLeft();
			while (loc.getRight() != null) {
				loc = loc.getRight();
			}
		} else {
			while (loc.getId() >= id) {
				loc = loc.getParent();
				if (loc == null) {
					return new Event(0, 0);
				}
			}
		}
		return loc;
	}

	/**
	 * Calls getSum
	 * 
	 * @param lrange
	 * @param rrange
	 * @return
	 */
	public int inrange(int lrange, int rrange) {
		return getSum(lrange, rrange, root);
	}

	/**
	 * 
	 * @param lrange
	 * @param rrange
	 * @param ptr
	 * @return
	 */
	public int getSum(int lrange, int rrange, Event ptr) {
		if (ptr == null) {
			return 0;
		}
		int currid = ptr.getId();
		int sum = 0;
		// check if current id lies between range
		if (currid >= lrange && currid <= rrange) {
			sum += ptr.getCount();
		}
		// if current id is bigger than left range recursively call in left
		// subtree
		if (currid > lrange) {
			sum += getSum(lrange, rrange, ptr.getLeft());
		}
		// if current id is smaller than right range recursively call in right
		// subtree
		if (currid < rrange) {
			sum += getSum(lrange, rrange, ptr.getRight());
		}
		return sum;
	}

}
