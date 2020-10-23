import java.util.ArrayList;
import java.util.Arrays;
import java.util.*; 

public class CLOCK {

    // Updates second chance and returns whether the page is in storage or not
    public int updateSecondChance(int page, ArrayList<Integer> arr, ArrayList<Integer> secondChance, int frames) {
        for (int i = 0; i < frames; i++) {
            if (arr.get(i).equals(page)) {
                // indicates page gets second chance
                secondChance.set(i,1);
                //System.out.println("Second Chance for set for page number " + arr.get(i));
                // shows there was a hit and no need to replace page
                return 1;
            }
        }
        // shows that a page for replacement is selected and the requested paage doesn't exist in memory
        return 0;
    }

    // Replaces page into physical memory and updates pointer to next frame
    public int replaceAndUpdatePointer(int page, ArrayList<Integer> arr, ArrayList<Integer> secondChance, int frames,
            int pointer) {
        while (true) {
            if (secondChance.get(pointer).equals(0) ) {
                // adds page into storage
                arr.set(pointer,page);
                //System.out.println("Placing page " + arr.get(pointer));
                //System.out.println("Storage is now " + Arrays.toString(arr.toArray()));
                // updates pointer to next frame
                pointer = (pointer + 1) % frames;
                return pointer;
            } else {
                // sets page in storage to have second chance of 0
                secondChance.set(pointer,0);
                // updates pointer to next frame
                pointer = (pointer + 1) % frames;
            }
        }
    }

    // Clock algorithm
    public void clockAlgo(ArrayList<Integer> pages, int frames) {
        int pointer = 0;

        // stores pages that are in physical memory
        ArrayList<Integer> arr= new ArrayList<Integer>();
        for (int i = 0; i < frames; i++) {
            arr.add(-1);
        }

        // remembers which if page is given second chance
        ArrayList<Integer> secondChance = new ArrayList<Integer>(frames);
        for (int i = 0; i < frames; i++) {
            secondChance.add(0);
        }

        // runs clock page replacement algorithms
        for (int i = 0; i < pages.size(); i++) {
            if (updateSecondChance(pages.get(i), arr, secondChance, frames) == 0) {
                pointer = replaceAndUpdatePointer(pages.get(i), arr, secondChance, frames, pointer);
                // update page faults
            }
        }
        System.out.println("FINAL: " + Arrays.toString(arr.toArray()));
    }

    // Runs equiprobable request for any of the n pages
    public void equiprobableRequest(ArrayList<Integer> pages, int frames) {
        // randomizes order of pages
        Collections.shuffle(pages);
        System.out.println("shuffled array: " + Arrays.toString(pages.toArray()));

        clockAlgo(pages, frames);

    }

    // Runs strongly biased probability for lower-numbered pages to be requested
    public void exponentialCLOCK(ArrayList<Integer> pages, int frames){
        //arraylist of frequences for each value
        ArrayList<Integer> expFreq = new ArrayList<>();
        //arraylist with all instances of frequences
        ArrayList<Integer> expDist = new ArrayList<>();
        //arraylist with all page values
        ArrayList<Integer> pageValues = getPageValues(pages);
        //calculate exponential distribution for each page number
        System.out.print("generated page numbers: "+pageValues.toString()+"\n");
        // loops through each page number to get the probability in an exponential distribution
        // using exponential function y = 1/(.02x + 0.001)
        for(int i = 0; i < pageValues.size(); i++){
            double a = 0.06*pageValues.get(i)+0.001;
            double eq = 1 / a;
            System.out.print("eq val for "+pageValues.get(i)+": "+eq+"\n");
            eq = eq * 100;
            int eqInt = (int)Math.round(eq);
            System.out.print("freq val after multiplying by 100 and rounding to int: "+eqInt+"\n");
            expFreq.add(eqInt);
        }
        System.out.print("frequencies for each page number: \n"+expFreq.toString()+"\n");
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
        ArrayList<Integer> pagesTracker = new ArrayList<>();
        for(int i = 0; i < pages.size(); i++){
            pagesTracker.add(pages.get(i));
            //empty  pages array to prepare for new ordering
            pages.set(i, -1);
        }
        Random rand = new Random();
        //randomly choose from arraylist of all frequencies
        int index = 0;
        // while thre are still pages that need to be chosen
        while(pagesTracker.isEmpty() == false){
            // randomly pick an index fromthe exponential distribution
            int randInt = rand.nextInt(expDist.size());
            //if page number at index chosen is still available to be picked
            if(pagesTracker.contains(expDist.get(randInt))){
                //assign to array of pages as next in the order
                pages.set(index, expDist.get(randInt));
                index++;
                // remove page number from pages because it has been added too ordering allready
                pagesTracker.remove(Integer.valueOf(expDist.get(randInt)));
            }
        }
        System.out.print("new ordering of pages with exponential probability: "+Arrays.toString(pages.toArray())+"\n");

        clockAlgo(pages, frames);
    }

    // Gets only the page values without repeats (helper method for exponentialCLOCK method)
    public ArrayList<Integer> getPageValues(ArrayList<Integer> pages){
        ArrayList<Integer> pageVal = new ArrayList<>();
        for(int i = 0; i < pages.size(); i++){
            //page number hasn't been recorded yet
            if(pageVal.contains(pages.get(i)) == false){
                pageVal.add(pages.get(i));
            }
        }
        return pageVal;
    }


     // Generates random pages used for testing
    public ArrayList<Integer> generatePages(int numPages, int maxVal){
        Random rand = new Random();
        ArrayList<Integer> pages = new ArrayList<Integer>(numPages);
        for(int i = 0; i < numPages; i++){
            int randInt = rand.nextInt(maxVal);
            pages.add(randInt);
        }
        System.out.println("genereated array: " + Arrays.toString(pages.toArray()));
        return pages;
    }

    public static void main (String args[]) {

        CLOCK test = new CLOCK();

        // strongly biased probability for lower-numbered pages to be requested 
        ArrayList<Integer> generatedPages = test.generatePages(1024, 10);
        test.exponentialCLOCK(generatedPages, 4);


        // // equiprobable request
        // ArrayList<Integer> generatedPages = test.generatePages(1024, 10);
        // test.equiprobableRequest(generatedPages, 4);

        // ArrayList<Integer> pages = new ArrayList<Integer>();
        // pages.add(0);
        // pages.add(1);
        // pages.add(1);
        // System.out.println(Arrays.toString(pages.toArray()));
        // HW2 test = new HW2();
        // //test.regularOrderRequest(pages,2);
        // test.equiprobableRequest(pages, 2);


        // ArrayList<Integer> pages = new ArrayList<Integer>();
        // pages.add(0);
        // pages.add(1);
        // pages.add(1);
        // pages.add(4);
        // pages.add(4);
        // pages.add(8);
        // System.out.println(Arrays.toString(pages.toArray()));
        // // test.regularOrderRequest(pages,2);
        // test.equiprobableRequest(pages, 4);

    }


}
