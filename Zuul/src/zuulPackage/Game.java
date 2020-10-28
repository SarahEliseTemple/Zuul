package zuulPackage;

import java.util.ArrayList;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

class Game 
{
    private Parser parser;
    private Room currentRoom;
    Room outside, theatre, pub, lab, office, onetwenty, roomFifteen, roomSixteen, roomSeventeen,
    coolKidCorner, roomEighteen,  roomNinteen,  roomTwenty, secretRoom, dramaClub, hallway, library, juiceBoxRoom, oneTwentyTwo, janitorCloset;
    ArrayList<Item> inventory = new ArrayList<Item>();
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    public static void main(String[] args) {
    	Game mygame = new Game();
    	mygame.play();
    }
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
              
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        onetwenty = new Room("in the coolest place in the world(room one-twenty)");
        roomFifteen = new Room("in the fifteenth room");
        roomSixteen = new Room("in the next room");
        roomSeventeen = new Room("in the 17th room");
        roomEighteen = new Room("in the 18th room");
        roomNinteen = new Room("in the 19th room");
        roomTwenty = new Room("in the 20th room");
        coolKidCorner = new Room ("at the place where all the cool kids hang out after programming.");
        dramaClub = new Room ("in the room where a lot of kids just play D&D all day otherwise known as the Drama Club.");
        hallway= new Room("in the hallway");
        library = new Room("In the library. Books tower around you.");
        juiceBoxRoom = new Room ("in the room built for juice boxes");
        oneTwentyTwo = new Room ("in 122.");
        janitorCloset= new Room ("in the office of one of the more high risk jobs in this instetution. The Janitors Closet.");
        secretRoom = new Room ("in the secret room!");
        
        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", onetwenty);

        theatre.setExit("west", outside);
        theatre.setExit("north", dramaClub);
        
        dramaClub.setExit("south", theatre);
        

        pub.setExit("east", outside);
        pub.setExit("west", coolKidCorner);

        coolKidCorner.setExit("east", pub);
        
        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.setExit("south", roomFifteen);
        
        office.setExit("west", lab);

        onetwenty.setExit("south", outside);
        onetwenty.setExit("north", hallway);
        
        hallway.setExit("south", onetwenty);
        hallway.setExit("north", oneTwentyTwo);
        hallway.setExit("east", janitorCloset);
        hallway.setExit("west", library);
        
        oneTwentyTwo.setExit("south", hallway);
        oneTwentyTwo.setExit("east", juiceBoxRoom);
        
        library.setExit("east", hallway);
        library.setExit("west", secretRoom);
        
        roomFifteen.setExit("south", roomSixteen);
        roomFifteen.setExit("north", lab);
        
        roomSixteen.setExit("south", roomSeventeen);
        roomSixteen.setExit("north", roomFifteen);
        
        roomSeventeen.setExit("south", roomEighteen);
        roomSeventeen.setExit("north", roomSixteen);
        
        roomEighteen.setExit("south", roomNinteen);
        roomEighteen.setExit("north", roomSeventeen);
        
        roomNinteen.setExit("south", roomTwenty);
        roomNinteen.setExit("north", roomEighteen);
        
        roomTwenty.setExit("north", roomNinteen);
        
        currentRoom = outside;  // start game outside
        
        //adding items 
        inventory.add(new Item("Computer"));
        onetwenty.setItem(new Item("Robot"));
        roomTwenty.setItem(new Item("Juice Box"));
        secretRoom.setItem(new Item("Juice Box"));
        dramaClub.setItem(new Item("Juice Box"));
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Adventure!");
        System.out.println("Adventure is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go")) {
            wantToQuit= goRoom(command);//the want to quit part lets this one get the winning condition
        }
        
        else if (commandWord.equals("quit")){
            
        	wantToQuit = quit(command);
        }
        else if (commandWord.equals("inventory")) {
        	printInventory();
        }
        else if (commandWord.equals("get")) {
        	getItem(command);
        }
        else if (commandWord.equals("drop")) {
        	dropItem(command);
        }
        return wantToQuit;
    }

    private void dropItem(Command command) {
    	if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what they want to drop...
            System.out.println("Drop what????");
            return;
        }

        String item = command.getSecondWord();

        // Try to leave current room.
        Item newItem = null;
        int index = 0;
        for (int i= 0; i<inventory.size(); i++) {
        	newItem = inventory.get(i);

        }
        
        if (newItem == null)
            System.out.println("The item isn't in your inventory! :( ");
        else {
            inventory.remove(index);
            currentRoom.setItem(new Item(item));
            System.out.println("Dropped: " + item);
        }
	}

	private void getItem(Command command) {
    	if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what they want to pick up...
            System.out.println("Get what????");
            return;
        }

        String item = command.getSecondWord();

        // Try to leave current room.
        Item newItem = currentRoom.getItem(item);

        if (newItem == null)
            System.out.println("The item isn't here!");
        else {
            inventory.add(newItem);
            currentRoom.removeItem(item);
            System.out.println("Picked up: " + item);
        }
	}

	private void printInventory() {
		String output = "";
		for (int i=0; i<inventory.size();i++) {
			output += inventory.get(i).getDescription() + " ";
		}
		System.out.println("You are carrying");
		System.out.println(output);
		
	}

	// implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private boolean goRoom(Command command) {//this is a boolean because if you go to room 20 then you win. 
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null)
            System.out.println("There is no door!");
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            if (currentRoom == roomTwenty) {
            	System.out.println("You win!!! :)");
            	return true;
            }
        }
        return false;
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else
            return true;  // signal that we want to quit
    }
}
