package com;


import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;
/*
*   This class represents a single node in the tree. Each node has Event ID
*   and it count as members.
*
*/
class TreeNode{
    public static TreeNode Tnil = new TreeNode();
    public int eventId, count;
    public TreeNode leftChild;
    public TreeNode rightChild;
    public TreeNode parent;
    public boolean colorRed = true;
    /*
    *   Constructor for TreeNode
    *   @param eventId The integer representing the eventId
     *  @param count   The count for the given event ID
    */
    public TreeNode(int eventId, int count){
        this.eventId = eventId;
        this.count = count;
        colorRed = true;
        leftChild = Tnil;
        rightChild = Tnil;
        parent = Tnil;
    }

    /*
    *   Overloaded Constructor for TreeNode to build the nil node
    *   This constructor
    */
    public TreeNode(){
        eventId = 0;
        count = 0;
        colorRed =  false;
    }
    /*
    *   This method increases the count of the Event objects that's calling it
     *   by the value given in byValue.
     *
     *   @param byValue     Integer value by which the count should be raised.
     */
    public int increaseNodeCount(int byValue){
        this.count += byValue;
        return this.count;
    }

    /*
    *   This method decreases the count of the Event objects that's calling it
     *   by the value given in byValue.
     *
     *   @param byValue     Integer value by which the count should be decreased.
     */
    public int decreaseNodeCount(int byValue){
        this.count-=byValue;
        return this.count;
    }


}

/*
* This class represents
 */
class RBTree{
    public TreeNode treeRoot;

    public RBTree() {
        treeRoot = TreeNode.Tnil;
    }


    public TreeNode standardSearch(int pEventID){
        TreeNode tempNode = this.treeRoot;
        while(tempNode != TreeNode.Tnil && tempNode.eventId != pEventID){
            if(pEventID <= tempNode.eventId)
                tempNode = tempNode.leftChild;
            else
                tempNode = tempNode.rightChild;
        }
        return tempNode;
    }

    public void insertRBTree(TreeNode newTreeNode){
        TreeNode temp = TreeNode.Tnil;
        TreeNode start = this.treeRoot;
        while(start!=TreeNode.Tnil){
            temp = start;
            if(newTreeNode.eventId<start.eventId){
                start = start.leftChild;
            }else{
                start = start.rightChild;
            }
        }
        newTreeNode.parent = temp;
        if(temp==TreeNode.Tnil){
            this.treeRoot = newTreeNode;
        }else if(newTreeNode.eventId < temp.eventId){
            temp.leftChild = newTreeNode;
        }else{
            temp.rightChild = newTreeNode;
        }
        newTreeNode.leftChild = TreeNode.Tnil;
        newTreeNode.rightChild = TreeNode.Tnil;
        newTreeNode.colorRed = true;
        RBFixTree(treeRoot, newTreeNode);
    }

    public boolean deleteRBTree(TreeNode deleteNode){
        TreeNode x = TreeNode.Tnil;
        TreeNode tempNode = deleteNode;
        boolean tempOriginalColor = tempNode.colorRed;
        if(deleteNode.leftChild==TreeNode.Tnil){
            x = deleteNode.rightChild;
            this.transplantNode(deleteNode,deleteNode.rightChild);

        }else if(deleteNode.rightChild==TreeNode.Tnil){
            x = deleteNode.leftChild;
            this.transplantNode(deleteNode, deleteNode.leftChild);
        }else{
            tempNode = this.treeMinimum(deleteNode.rightChild);
            tempOriginalColor = tempNode.colorRed;
            x = tempNode.rightChild;
            if(tempNode.parent==deleteNode){
                x.parent = tempNode;
            }else{
                this.transplantNode(tempNode,tempNode.rightChild);
                tempNode.rightChild = deleteNode.rightChild;
                tempNode.rightChild.parent = tempNode;
            }
            this.transplantNode(deleteNode,tempNode);
            tempNode.leftChild = deleteNode.leftChild;
            tempNode.leftChild.parent = tempNode;
            tempNode.colorRed = deleteNode.colorRed;
        }
        if(!tempOriginalColor){
            fixDeleteRB(x);
        }
        return true;
    }

    private void fixDeleteRB(TreeNode fixNode){
        TreeNode uncleNode = TreeNode.Tnil;
        while(fixNode!=this.treeRoot && fixNode.colorRed == false){
            if(fixNode == fixNode.parent.leftChild){
                uncleNode = fixNode.parent.rightChild;
                if(uncleNode.colorRed==true){
                    uncleNode.colorRed = false;
                    fixNode.parent.colorRed = true;
                    leftRotate(fixNode.parent);
                    uncleNode = fixNode.parent.rightChild;
                }
                if(uncleNode.leftChild.colorRed==false && uncleNode.rightChild.colorRed==false){
                    uncleNode.colorRed = true;
                    fixNode = fixNode.parent;
                }else{
                    if(uncleNode.rightChild.colorRed==false){
                        uncleNode.leftChild.colorRed = false;
                        uncleNode.colorRed = true;
                        rightRotate(uncleNode);
                        uncleNode = fixNode.parent.rightChild;
                    }
                    uncleNode.colorRed = fixNode.parent.colorRed;
                    fixNode.parent.colorRed = false;
                    uncleNode.rightChild.colorRed = false;
                    leftRotate(fixNode.parent);
                    fixNode = this.treeRoot;
                }
            }else{
                uncleNode = fixNode.parent.leftChild;
                if(uncleNode.colorRed==true){
                    uncleNode.colorRed = false;
                    fixNode.parent.colorRed = true;
                    rightRotate(fixNode.parent);
                    uncleNode = fixNode.parent.leftChild;
                }
                if(uncleNode.leftChild.colorRed==false && uncleNode.rightChild.colorRed==false){
                    uncleNode.colorRed = true;
                    fixNode = fixNode.parent;
                }else{
                    if(uncleNode.leftChild.colorRed==false){
                        uncleNode.rightChild.colorRed = false;
                        uncleNode.colorRed = true;
                        leftRotate(uncleNode);
                        uncleNode = fixNode.parent.leftChild;
                    }
                    uncleNode.colorRed = fixNode.parent.colorRed;
                    fixNode.parent.colorRed = false;
                    uncleNode.leftChild.colorRed = false;
                    rightRotate(fixNode.parent);
                    fixNode = this.treeRoot;
                }
            }
        }
        fixNode.colorRed = false;
    }
    private void RBFixTree(TreeNode root, TreeNode newNodeZ){
        while(newNodeZ.parent.colorRed){
            if(newNodeZ.parent.parent.leftChild == newNodeZ.parent){
                TreeNode y = newNodeZ.parent.parent.rightChild;
                if(y.colorRed){
                    newNodeZ.parent.colorRed = false;
                    y.colorRed = false;
                    newNodeZ.parent.parent.colorRed = true;
                    newNodeZ = newNodeZ.parent.parent;
                }else {
                    if (newNodeZ == newNodeZ.parent.rightChild) {
                        newNodeZ = newNodeZ.parent;
                        leftRotate(newNodeZ);
                    }
                    newNodeZ.parent.colorRed = false;
                    newNodeZ.parent.parent.colorRed = true;
                    rightRotate(newNodeZ.parent.parent);
                }
            }
            else{
                TreeNode y = newNodeZ.parent.parent.leftChild;
                if(y.colorRed){
                    newNodeZ.parent.colorRed = false;
                    y.colorRed = false;
                    newNodeZ.parent.parent.colorRed = true;
                    newNodeZ = newNodeZ.parent.parent;
                }else {
                    if (newNodeZ == newNodeZ.parent.leftChild) {
                        newNodeZ = newNodeZ.parent;
                        rightRotate(newNodeZ);
                    }
                    newNodeZ.parent.colorRed = false;
                    newNodeZ.parent.parent.colorRed = true;
                    leftRotate(newNodeZ.parent.parent);
                }
            }

        }
        this.treeRoot.colorRed = false;
    }
    private void leftRotate(TreeNode pivot){
        TreeNode y = pivot.rightChild;
        pivot.rightChild = y.leftChild;
        if(y.leftChild!=TreeNode.Tnil){
            y.leftChild.parent = pivot;
        }
        y.parent = pivot.parent;
        if(pivot.parent==TreeNode.Tnil){
            this.treeRoot = y;
        }else if(pivot.parent.leftChild == pivot){
            pivot.parent.leftChild = y;
        }else if(pivot.parent.rightChild == pivot){
            pivot.parent.rightChild = y;
        }
        y.leftChild = pivot;
        pivot.parent = y;
    }

    private void rightRotate(TreeNode pivot){
        TreeNode x = pivot.leftChild;
        pivot.leftChild = x.rightChild;
        if(x.rightChild!=TreeNode.Tnil)
            x.rightChild.parent = pivot;

        x.parent = pivot.parent;

        if(pivot.parent==TreeNode.Tnil){
            this.treeRoot = x;
        }else if(pivot.parent.leftChild == pivot){
            pivot.parent.leftChild = x;
        }else if(pivot.parent.rightChild == pivot){
            pivot.parent.rightChild = x;
        }

        x.rightChild = pivot;
        pivot.parent = x;
    }

    private TreeNode treeMinimum(TreeNode startNode){
        TreeNode temp = startNode;
        while(temp.leftChild!=TreeNode.Tnil){
            temp = temp.leftChild;
        }
        return temp;
    }

    private TreeNode treeMaximum(TreeNode startNode){
        TreeNode temp = startNode;
        while(temp.rightChild!=TreeNode.Tnil){
            temp=temp.rightChild;
        }
        return temp;
    }
    private void transplantNode(TreeNode originalNode, TreeNode newNode){
        if(originalNode.parent== TreeNode.Tnil){
            this.treeRoot = newNode;
        }else if(originalNode.parent.leftChild == originalNode){
            originalNode.parent.leftChild = newNode;
        }else if(originalNode.parent.rightChild == originalNode){
            originalNode.parent.rightChild = newNode;
        }
        newNode.parent = originalNode.parent;
    }
    int count = 0;
    public void inRange(TreeNode root, int lowerValue, int higherValue){
        if(root.leftChild != TreeNode.Tnil&&
                root.eventId > lowerValue){
            inRange(root.leftChild,lowerValue, higherValue);

        }

        if(root.eventId >= lowerValue &&
                root.eventId <= higherValue){
            count += root.count;
        }

        if(root.rightChild!=TreeNode.Tnil &&
                root.eventId < higherValue){
            inRange(root.rightChild, lowerValue, higherValue);
        }
    }
    public void inOrder(TreeNode root,int i, PrintWriter pw){
        if(root==TreeNode.Tnil){
            return;
        }

       for(int j=0;j<i;j++)
            pw.print(".");
            //System.out.print(".");

        pw.println(root.eventId+"("+((root.colorRed)?"r":"b")+")");
        //System.out.println(root.eventId+"("+((root.colorRed)?"r":"b")+")");

        inOrder(root.leftChild,i+1,pw);

        inOrder(root.rightChild,i+1,pw);

    }

    public TreeNode getSuccessor(TreeNode nodeInput){
        if(nodeInput.rightChild!=TreeNode.Tnil){
            return this.treeMinimum(nodeInput.rightChild);
        }
        TreeNode tempNode = nodeInput.parent;
        while(tempNode!=TreeNode.Tnil && nodeInput == tempNode.rightChild){
            nodeInput = tempNode;
            tempNode = tempNode.parent;
        }

        return tempNode;
    }

    public TreeNode getPredecessor(TreeNode nodeInput){
        if(nodeInput.leftChild!=TreeNode.Tnil){
            return this.treeMaximum(nodeInput.leftChild);
        }

        TreeNode tempNode = nodeInput.parent;
        while(tempNode!=TreeNode.Tnil && nodeInput == tempNode.leftChild){
            nodeInput = tempNode;
            tempNode = tempNode.parent;

        }
        return tempNode;
    }




}

class FileLoader{
    //This class will load the file and build the tree.
    //I will not be creating an initial array to save space. I will build the tree directly instead.

    String fileName;
    Scanner inputFileScanner;
    InputStream is = null;
    BufferedReader br;
    double height;
    private void initScanner(){
        try {
            inputFileScanner = new Scanner(new FileInputStream(new File(this.fileName)));
            is = new BufferedInputStream(new FileInputStream(fileName),8*2*1024*1024);
            br = new BufferedReader(new InputStreamReader(is));
        }catch (FileNotFoundException e){
            System.out.println("Error opening file");
            System.exit(-1);
        }
    }
    
    
    private int[][] getRBTree(int x){

        int[][] data = null;
        try {
            String line = br.readLine();
            StringTokenizer st = new StringTokenizer(line," ");
            int n = Integer.parseInt(st.nextToken());
            this.height = (Math.log(n)/Math.log(2));
            data = new int[n][2];
            int locationCounter = 0;
            while((line = br.readLine())!=null){
                st = new StringTokenizer(line," ");
                while(st.hasMoreTokens()){

                    data[locationCounter][0] = Integer.parseInt(st.nextToken());
                    data[locationCounter][1] = Integer.parseInt(st.nextToken());
                    locationCounter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	//System.exit(0);
        return data;
    }

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    public RBTree loadFile(){
        initScanner();

        int[][] data = getRBTree(1);
	
        return buildTree(data);
        //return getRBTree();
    }

    public RBTree buildTree(int[][] data){
        RBTree localRBTreeHandler = new RBTree();
        localRBTreeHandler.treeRoot = getTree(data,0,data.length-1,1);

        return localRBTreeHandler;
    }

    public TreeNode getTree(int[][] data, int start, int end, int count){
        if(start>end){
            return TreeNode.Tnil;
        }
        int mid = (start + end)/2;
	
        TreeNode newNode = new TreeNode(data[mid][0],data[mid][1]);
        newNode.leftChild = getTree(data,start,mid-1,count+1);
        newNode.rightChild = getTree(data,mid+1,end,count+1);

        if(newNode.leftChild!=TreeNode.Tnil){
            newNode.leftChild.parent = newNode;
            newNode.colorRed = false;
        }
        if(newNode.rightChild!=TreeNode.Tnil){
            newNode.rightChild.parent = newNode;
            newNode.colorRed = false;
        }
        if(newNode.colorRed&&count!=((int)height+1)){
            
            newNode.colorRed = false;
        }
        return newNode;
    }
}

class CommandExecutor {

    RBTree localRBTreeObject;

    public CommandExecutor(String fileName){
        localRBTreeObject = new FileLoader(fileName).loadFile();

    }

    public void increaseKey(int eventId, int count){
        TreeNode tempNode = localRBTreeObject.standardSearch(eventId);
        if(tempNode==TreeNode.Tnil){
            localRBTreeObject.insertRBTree(new TreeNode(eventId,count));
            System.out.println(count);
        }else{
            System.out.println(tempNode.increaseNodeCount(count));
        }
    }

    public void decreaseKey(int eventId, int count){
        TreeNode tempNode = localRBTreeObject.standardSearch(eventId);
        if(tempNode==TreeNode.Tnil){
            System.out.println(0);
        }else{
            int newValue = tempNode.decreaseNodeCount(count);
            if(newValue<1){
                localRBTreeObject.deleteRBTree(tempNode);
                System.out.println(0);
            }else{
                System.out.println(newValue);
            }
        }
    }

    public void getCount(int eventId){
        TreeNode temp = localRBTreeObject.standardSearch(eventId);
        if(temp==TreeNode.Tnil){
            System.out.println(0);

        }else{
            System.out.println(temp.count);
        }
    }

    public void inRange(int eventId1, int eventId2){
        localRBTreeObject.count = 0;
        localRBTreeObject.inRange(localRBTreeObject.treeRoot,eventId1,eventId2);
        System.out.println(localRBTreeObject.count);
    }

    public void next(int eventId){
        TreeNode target = findNext(localRBTreeObject.treeRoot,eventId);
        System.out.println(target.eventId + " " + target.count);
    }
    private TreeNode findNext(TreeNode startRoot, int eventId){
        if(startRoot == TreeNode.Tnil){
            return TreeNode.Tnil;
        }
        if(startRoot.eventId==eventId){
            return(localRBTreeObject.getSuccessor(startRoot));
        }
        if(startRoot.eventId<eventId){
            return findNext(startRoot.rightChild,eventId);
        }
        TreeNode foundNode = findNext(startRoot.leftChild,eventId);
        return ((foundNode!=TreeNode.Tnil&&foundNode.eventId>=eventId)?foundNode:startRoot);
    }

    public void previous(int eventId){
        TreeNode target = findPrevious(localRBTreeObject.treeRoot,eventId);
        System.out.println(target.eventId + " " + target.count);
    }

    private TreeNode findPrevious(TreeNode startNode, int eventId){
        if(startNode==TreeNode.Tnil){
            return startNode;
        }
        if(startNode.eventId==eventId){
            return(localRBTreeObject.getPredecessor(startNode));
        }

        if(startNode.eventId>eventId){
            return findPrevious(startNode.leftChild,eventId);
        }

        TreeNode foundNode = findPrevious(startNode.rightChild,eventId);
        return ((foundNode!=TreeNode.Tnil&&foundNode.eventId<=eventId)?foundNode:startNode);
    }
 /*  public void previous(int eventId){
        TreeNode temp = localRBTreeObject.standardSearch(eventId);
        if(temp!=TreeNode.Tnil){
            TreeNode predecessor = localRBTreeObject.getPredecessor(temp);
            if(predecessor!=TreeNode.Tnil)
                System.out.println(predecessor.eventId+" "+predecessor.count);
            else
                System.out.println(0 + " " + 0);
        }else{
            TreeNode predecessor = localRBTreeObject.getPredecessor(localRBTreeObject.treeRoot,eventId);
            if(predecessor!=null) {
                if (predecessor!= TreeNode.Tnil)
                    System.out.println(predecessor.eventId + " " + predecessor.count);
                else
                    System.out.println(0 + " " + 0);
            }else{
                System.out.println(0 + " " + 0);
            }
        }
    }
    */

    public boolean isBalanced()
    {
        int black = 0;
        TreeNode temp = localRBTreeObject.treeRoot;
        while(temp!=TreeNode.Tnil){
            if(!temp.colorRed) black++;
            temp = temp.leftChild;
        }
        return isBalanced(localRBTreeObject.treeRoot,black);
    }

    public boolean isBalanced(TreeNode root, int black){
        if(root==TreeNode.Tnil) return black == 0;
        if(!root.colorRed) black--;
        return isBalanced(root.leftChild,black) && isBalanced(root.rightChild,black);
    }

    public void printTreeToFile(PrintWriter pw){
        localRBTreeObject.inOrder(localRBTreeObject.treeRoot,0,pw);
    }
}
public class RB_Jay {

    public static void main(String[] args) {
        if(args.length<0){
            System.out.println("Please enter an input file name");
            System.exit(-1);
        }
        String file = "src/test_100.txt";
        CommandExecutor localCommandExecuter = new CommandExecutor(file);
        BufferedReader localBr = new BufferedReader(new InputStreamReader(System.in));
       // Scanner commandReader = new Scanner(System.in);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter("TreeOutput.txt");
        }catch (Exception e){

        }
        while(true){
          //  System.out.println(localCommandExecuter.isBalanced());

            try{
                String line[] = localBr.readLine().split(" ");

                switch (line[0]){
                    case "increase":

                        localCommandExecuter.increaseKey(Integer.parseInt(line[1]),Integer.parseInt(line[2]));
                        break;
                    case "reduce":

                        localCommandExecuter.decreaseKey(Integer.parseInt(line[1]),Integer.parseInt(line[2]));
                        localCommandExecuter.printTreeToFile(pw);
                        break;
                    case "count":

                        localCommandExecuter.getCount(Integer.parseInt(line[1]));
                        break;
                    case "next":

                        localCommandExecuter.next(Integer.parseInt(line[1]));
                        break;
                    case "previous":

                        localCommandExecuter.previous(Integer.parseInt(line[1]));
                        break;
                    case "inrange":

                        localCommandExecuter.inRange(Integer.parseInt(line[1]),Integer.parseInt(line[2]));
                        break;
                    case "quit":
                        pw.close();
                        System.exit(0);
                        break;
                    default: System.out.println("Invalid Command");
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }



    }
}
