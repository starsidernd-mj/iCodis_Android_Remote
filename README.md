# iCodis_Android_Remote

I am living in Japan at the moment and was able to find a second hand portable projector that I wanted to use for making a little home theater for my kids. The price was decent at around USD50 but since it was second hand I didn't know what was inside the box. It was missing the remote control, without which the device is completely unusable. Amazon claims around USD20 for a replacement and the company doesn't seem to answer their emails anymore in HK. So, not wanting to have wasted money and was really unmotivated to return the device, I instead decided to find a way to brute force the IR codes. I am posting the walkthrough here for all of you in case you ever find yourselves in the same situation as well as to help others with this device so that they don't have to buy a replacement remote.

## Apps

I have an old Xiaomi phone with an IR port built in so I am able to use IR apps on Android to do this. The app I used was: IrCode Finder Universal Remote on the app store. It has an Auto Test feature that basically steps through various device addresses and tests a single command on them. It also has a Brute Force feature that does the same but tests 256 commands per address. That takes about 3 hours. I went the quicker route and did the Auto Test, which only tests the first command (0x00FF).

## Step 1

Turn on the projector and it will idle in the Source Select menu from which there is no escape using the only (power) button. Instead, it requires the remote.

## Step 2

Using Auto Test, it will play through all devices and the first command. At this point I just point the phone at it and run the test, then watch the screen for any changes or any popup messages. When I saw a quick message pop on the screen then disappear, I made a mental note of the address it was on, but let the test complete (about 6 minutes) to see if any other device would have the same effect.

## Step 3

With the 1 device in mind, I used the app to cycle back to that device in the list and manually tested it. The popup reappeared, so I had narrowed it down to 1 device. Now was the long process of decoding the behavior of 256 commands.

## Step 4

I basically incremented the commands one by one and tested them to see what they would do. In the end, I did this several times in different modes. Ex. in the menu mode some buttons did not do anything while in video mode they modified aspect ratio, volume, or color temperature.

When I found buttons that did something, I made a list of their potential behaviors and attempted to use them in a normal way (ex. up/down/left/right arrows, enter button, menu select, etc).

## Step 5

Creating a remote control app. This was the tricky part as I don't have much experience coding an Android apk. But, with the help of various abandoned projects I'll link later, I cobbled together a remote control app.
The protocol used was NEC with MSB format. The device address and commands in hex were transferred as 16 bit hex values.

### Android Main Activity

The main activity instantiates a couple linear layouts for placing buttons on the screen. One LinearLayout is for the 5 rows of buttons, and another LinearLayout is for the buttons themselves. This allows for placing resizable buttons in rows without defining the size of each button individually. 
It also sets up the phone's vibration for each button press, the IR Controller and OnTouchListener.

#### IRController

This class sets up the ConsumerIRManager, which is what handles the IR hardware communication. Because it uses threading, the class will start a worker that handles whether to send a new message or wait.

#### IRNECFactory

The NEC factory generates the messages to be sent in the specific NEC format. The timing is obtained from the official NEC spec with the base unit timing being 560ns. This is used for generating the header, footer and each individual bit. Essentially, it creates a 1 or 0 for a certain time, which is interpretted by the receiver. 

A command address is passed to the decodeInt method, which will generate a list of integers based on the value of each bit in the message.
For example:

address: 0x10ef
in binary: 0b0001_0000_1110_1111
msb
Zero = 560ns
One = 1680ns

header: 560 560 560 560 560 560 1680 560 _ 560 560 560 560 560 560 560 560 _ 1680 560 1680 560 1680 560 560 560 _ 1680 560 1680 560 1680 560 1680 560

Note: if sending this in 8 bit mode, must use the format ~address address, ~command command otherwise the message will be out of order.

address 0xef		0b1110_1111
~address 0x10	0b0001_0000

decoded: 	1680 560 1680 560 1680 560 560 560 _ 1680 560 1680 560 1680 560 1680 560 
~decoded:	560 560 560 560 560 560 1680 560 _ 560 560 560 560 560 560 560 560 


The footer will look like this:

footer: 560

And sending a message like Power (0xd02f) will generate this message:

message: 0xd02f
in binary: 0b1101_0000_0010_1111
msb

message: 1680 560 1680 560 560 560 1680 560 _ 560 560 560 560 560 560 560 560 _ 560 560 560 560 1680 560 560 560 _ 1680 560 1680 560 1680 560 1680 560 

So this final message with header and footer will look like this (_ is just for separating nibbles visually): 

560 560 560 560 560 560 1680 560 _ 560 560 560 560 560 560 560 560 _ 1680 560 1680 560 1680 560 560 560 _ 1680 560 1680 560 1680 560 1680 560 _ 1680 560 1680 560 560 560 1680 560 _ 560 560 560 560 560 560 560 560 _ 560 560 560 560 1680 560 560 560 _ 1680 560 1680 560 1680 560 1680 560 _ 560

Once generated, these values are added to each button as a tag. Upon pressing a button, this is sent to the IRController class, which queues it into the messageQueue for sending.

## IR Codes

Below is a list of the IR codes for anyone else wishing to use them.

Device Address (0x10EF)
Command Address    |    App #    |    Function
-----------------------------|--------------|--------------------------
D02F                          |    11         |    Power
30CF                          |    12         |    Menu
B04F                          |    13         |    Enter/Src select (?)
708F                          |    14         |    Freeze frame
F00F                          |    15         |    Rotate screen
48B7                          |    18         |    Vol down
C837                          |    19         |    Vol up
28D7                          |    20         |    Mute
A857                          |    21         |    Back
E817                          |    23         |    Settings
58A7                          |    26         |    Screen Aspct Ratio
38C7                          |    28         |    Enter
7887                          |    30         |    Picture/Video settings (Std/Dynamic/Vivid/User)
F20D                          |    79         |    Screen color temperature
0AF5                          |    80         |    Up
8A75                          |    81         |    Left
4AB5                          |    82         |    Right
CA35                          |    83         |    Down
05FA                          |    160        |    Channel list (?)


End of line.