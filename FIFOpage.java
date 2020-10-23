import java.util.*;

public class FIFOpage{
    ArrayList<Node> priorities = new ArrayList<>();
    int[] currPages;
    int[] pagesToAdd;
    int seed = 1;
    int pageFaults;

    public class Node{
        private int page;
        private int index;

        public Node(int page, int index){
            this.page = page;
            this.index = index;
        }

        public void setPage(int page){
            this.page = page;
        }

        public void setIndex(int index){
            this.index = index;
        }

        public int getPage(){
            return this.page;
        }

        public int getIndex(){
            return this.index;
        }
    }

    public boolean arrContains(int[] arr, int page){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == page){
                return true;
            }
        }
        return false;
    }

    public int[] fifo(int[] currPages){
        int openSlot = 0;
        pageFaults = 0;
        int priorityIndex = 0;
        for(int i = 0; i < this.pagesToAdd.length; i++){
            //if there are slots empty
            if(openSlot < currPages.length){
                //page doesn't already exist
                if(arrContains(currPages, pagesToAdd[i]) == false){
                    //add to arraylist keeping track of index and page
                    priorities.add(new Node(pagesToAdd[i], priorityIndex));
                    //add to open slot
                    currPages[openSlot] = pagesToAdd[i];
                    pageFaults++;
                    openSlot++;
                    priorityIndex++;
//                   System.out.print(Arrays.toString(currPages)+"\n");
                }
                //else don't do anything bc page already is in a slot
            }
        
        //slot is full
        else{
            //page doesn't already exist in the slots
            if(arrContains(currPages, pagesToAdd[i]) == false){
                //get the index of the oldest page and add the new page to that slot
                currPages[priorities.get(0).getIndex()] = pagesToAdd[i];
                //add new page to the list of pages and their priorities
                priorities.add(new Node(pagesToAdd[i], priorities.get(0).getIndex()));
                //remove the old page from the list of pages
                priorities.remove(0);
                pageFaults++;
 //               System.out.print(Arrays.toString(currPages)+"\n");
            }
        }
    }
    System.out.print("final pages: "+Arrays.toString(currPages)+"\n");
    return currPages;
    }

    public int[] equiprobableFIFO(int seed){
        Random rand = new Random(seed);
        ArrayList<Integer> pages = new ArrayList<>();
        for(int i = 0; i < pagesToAdd.length; i++){
            pages.add(pagesToAdd[i]);
        }
        for(int i = 0; i < pagesToAdd.length; i++){
            int randInt = rand.nextInt(pages.size());
            pagesToAdd[i] = pages.get(randInt);
            pages.remove(randInt);
        }
        System.out.print("new ordering of pages in equiprobable order (random): "+Arrays.toString(pagesToAdd)+"\n");
        return pagesToAdd;
    }


    public ArrayList<Integer> getPageValues(){
        ArrayList<Integer> pageVal = new ArrayList<>();
        for(int i = 0; i < pagesToAdd.length; i++){
            //page number hasn't been recorded yet
            if(pageVal.contains(pagesToAdd[i]) == false){
                pageVal.add(pagesToAdd[i]);
            }
        }
        return pageVal;
    }

    public int[] exponentialFIFO(int seed){
        //arraylist of frequences for each value
        ArrayList<Integer> expFreq = new ArrayList<>();
        //arraylist with all instances of frequences
        ArrayList<Integer> expDist = new ArrayList<>();
        //arraylist with all page values
        ArrayList<Integer> pageValues = this.getPageValues();
        //calculate exponential distribution for each page number
        System.out.print("generated page numbers: "+pageValues.toString()+"\n\n");
        //loop through each page number to get the probability in an exponential distribution
        //using exponential function y = 1/(0.02x + 0.001)
        for(int i = 0; i < pageValues.size(); i++){
            double a = 0.06*pageValues.get(i)+0.001;
            double eq = 1 / a;
            System.out.print("freq val for page "+pageValues.get(i)+": "+eq+"\n");
            eq = eq * 100;
            int eqInt = (int)Math.round(eq);
            System.out.print("freq val after multiplying by 100 and rounding to int: "+eqInt+"\n\n");
            expFreq.add(eqInt);
        }
        System.out.print("generated page numbers: "+pageValues.toString()+"\n\n");
        System.out.print("frequencies for each page number: "+expFreq.toString()+"\n\n");
        //looping through arraylist of frequencies
        for(int i = 0; i < expFreq.size(); i++){
            //looping through the number of frequencies for each value
            for(int j = 0; j < expFreq.get(i); j++){
                //add to distribution list including all duplicates
                expDist.add(pageValues.get(i));
            }
        }
        //add generated pages to arraylist
        //arraylist pages keeps track of which pages need to be added to ordering
        ArrayList<Integer> pages = new ArrayList<>();
        for(int i = 0; i < pagesToAdd.length; i++){
            pages.add(pagesToAdd[i]);
            //emtpy pages array to prepare for new ordering
            pagesToAdd[i] = -1;
        }
        Random rand = new Random(seed);
        //randomly choose from arraylist of all frequencies
        int index = 0;
        //while there are still pages that need to be chosen
        while(pages.isEmpty() == false){
            //randomly pick an index from the exponential distribution
            int randInt = rand.nextInt(expDist.size());
            //if page number at index chosen is still available to be picked
            if(pages.contains(expDist.get(randInt))){
                //assign to array of pages as next in the order
                pagesToAdd[index] = expDist.get(randInt);
                index++;
                //remove page number from pages because it has been added to ordering already
                pages.remove(Integer.valueOf(expDist.get(randInt)));
              //  System.out.print("ordering of pages: "+Arrays.toString(pagesToAdd)+"\n");
            }
        }
        System.out.print("new ordering of pages with exponential probability: "+Arrays.toString(pagesToAdd)+"\n\n");
        return pagesToAdd;
    }


    public void generatePages(int numPages, int maxVal, int seed){
        Random rand = new Random(seed);
        this.pagesToAdd = new int[numPages];
        for(int i = 0; i < numPages; i++){
            int randInt = rand.nextInt(maxVal);
            pagesToAdd[i] = randInt;
        }
        System.out.print(numPages+" generated pages: "+Arrays.toString(pagesToAdd)+"\n");
    }

    public void generateSlots(int numSlots){
        this.currPages = new int[numSlots];
        for(int i = 0; i < numSlots; i++){
            currPages[i] = -1;
        }
    }

    public int getNumSlots(){
        return this.currPages.length;
    }

    public int getPageFaults(){
        return this.pageFaults;
    }

    public String priorities(ArrayList<Node> priorities){
        StringBuilder builder = new StringBuilder();
        builder.append("list of nodes: \n");
        for(int i = 0; i < priorities.size(); i++){
            builder.append("page: "+priorities.get(i).getPage()+" index: "+priorities.get(i).getIndex()+"\n");
        }
        return builder.toString();
    }

    public int[] getCurrPages(){
        return this.currPages;
    }

    public int getNumPagesToAdd(){
        return this.pagesToAdd.length;
    }

    public static void main(String[] args) {

        FIFOpage a = new FIFOpage();
        //FIFO method on 10 pages
        a.generateSlots(4);
        System.out.print("empty memory with "+a.getNumSlots()+" slots: "+Arrays.toString(a.currPages)+"\n");
        a.generatePages(10, 10, 1);
        a.fifo(a.getCurrPages());
        System.out.print("FIFO Total number of page faults: "+ a.getPageFaults()+"\n\n");
       
        a.priorities.clear();

        //FIFO method on 2^10 pages
        a.generateSlots(4);
        System.out.print("empty memory with "+a.getNumSlots()+" slots: "+Arrays.toString(a.currPages)+"\n");
        a.generatePages(1024, 10, 2);
        a.fifo(a.getCurrPages());
        System.out.print("FIFO Total number of page faults: "+ a.getPageFaults()+"\n\n");
       
        a.priorities.clear();

        //FIFO method with equiprobable paging on 2^10 pages
        a.generateSlots(4);
        System.out.print("empty memory with "+a.getNumSlots()+" slots: "+Arrays.toString(a.currPages)+"\n");
        a.equiprobableFIFO(4);
        a.fifo(a.getCurrPages());
        System.out.print("FIFO Total number of page faults: "+ a.getPageFaults()+"\n");
        a.priorities.clear();

        //FIFO method with exponentially biased probability for lower-numbered pages with 2^10 pages
        a.generateSlots(4);
        System.out.print("\n");
        a.generatePages(1024, 10, 2);
        System.out.print("\n");
        System.out.print("empty memory with "+a.getNumSlots()+" slots: "+Arrays.toString(a.currPages)+"\n");
        a.exponentialFIFO(4);
        a.fifo(a.getCurrPages());
        System.out.print("FIFO Total number of page faults: "+ a.getPageFaults()+"\n");
        a.priorities.clear();
    }



}